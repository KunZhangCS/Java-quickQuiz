package quickquiz;

/**
*   PROGRAM:   QuickQuiz
*   AUTHOR:    Kun Zhang
* 
*   FUNCTION:  A system that delivers quick quiz questions via a classroom-based network-to a group of students.

*   NOTES:     This the student(client) side program which contains two main parts: QuickQuizStudent.java and StudentThread.java.
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.HashSet;




public class QuickQuizStudent extends JFrame implements ActionListener
{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    
    private JLabel lblQuickQuizTitle, lblName, lblInstruc, lblTopic, lblQuestion, lblA, lblB, lblC, lblD, lblAns;
    private JButton btnSubmit, btnExit;
    private JTextField txtName, txtTopic, txtAns;
    private JTextArea txaQuestion, txaA, txaB, txaC, txaD;
    
    // parameters for connection
    private String serverName = "localhost";
    private int serverPort = 8888;
    private Socket socket = null;
    private StudentThread student = null;
    
    private String qNumber, qTopic, qCorrectAnswer;
    private HashSet<String> question = new HashSet<String>();
    
    SpringLayout springLayout = new SpringLayout();
    
//</editor-fold>

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        QuickQuizStudent quizApp = new QuickQuizStudent();
        quizApp.run();
    }
    
    public void run()
    {
        setBounds(100, 100, 350, 450);
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                dispose();
                //System.exit(0);
            }
        });
        
        displayGUI();
        setResizable(false);
        setVisible(true); 
        connect(serverName, serverPort);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Display GUI">
    
    private void displayGUI()
    {
        setLayout(springLayout);
        
        displayLables(springLayout);
        displayButtons(springLayout);
        displayTextFields(springLayout);
        displayTextAreas(springLayout);        
    }
    
    private void displayLables(SpringLayout layout)
    {
        lblQuickQuizTitle = Library.LocateAJLabel(this, layout, "Quick Quiz", 10, 10);
        lblQuickQuizTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblName = Library.LocateAJLabel(this, layout, "Your Name: ", 10, 50);
        lblInstruc = Library.LocateAJLabel(this, layout, "Enter your answer and click Submit", 10, 80);
        lblTopic = Library.LocateAJLabel(this, layout, "Topic:", 10, 110);
        lblQuestion = Library.LocateAJLabel(this, layout, "Qn:", 10, 130);
        lblA = Library.LocateAJLabel(this, layout, "A:", 10, 180);
        lblB = Library.LocateAJLabel(this, layout, "B:", 10, 215);
        lblC = Library.LocateAJLabel(this, layout, "C:", 10, 250);
        lblD = Library.LocateAJLabel(this, layout, "D:", 10, 285);
        lblAns = Library.LocateAJLabel(this, layout, "Your Answer: ", 10, 330);        
    }
    
    private void displayButtons(SpringLayout layout)
    {
        btnSubmit = Library.LocateAJButton(this, this, layout, "Submit", 10, 370, 100, 20);
        btnExit = Library.LocateAJButton(this, this, layout, "Exit", 210, 370, 100, 20);
    }
    
    private void displayTextFields(SpringLayout layout)
    {
        txtName = Library.LocateAJTextField(this, layout, 20, 90, 50);
        txtTopic = Library.LocateAJTextField(this, layout, 20, 90, 110);
        txtAns = Library.LocateAJTextField(this, layout, 5, 90, 330);
        
        txtAns.setEditable(true);
        txtName.setEditable(true);
    }
    
    private void displayTextAreas(SpringLayout layout)
    {
        txaQuestion = Library.LocateAJTextArea(this, layout, 90, 130, 3, 20);
        txaA = Library.LocateAJTextArea(this, layout, 90, 180, 2, 20);
        txaB = Library.LocateAJTextArea(this, layout, 90, 215, 2, 20);
        txaC = Library.LocateAJTextArea(this, layout, 90, 250, 2, 20);
        txaD = Library.LocateAJTextArea(this, layout, 90, 285, 2, 20);
    }
    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Action and Mouse Listener">
    @Override
    public void actionPerformed(ActionEvent e)
    {
        
        if (e.getSource() == btnExit) 
        {
            dispose();
            
            // Tell the server to close the server side socket
            student.send("quit");
            close();
            
        }
        if (e.getSource() == btnSubmit)
        {
            if (txtTopic.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "You have not got a question");
              
            } 
            else
            {
                  sendAnswer();
            }                        
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Connection">
    
    private void connect(String serverName, int serverPort)
    {
        System.out.println("Establishing connection. Please wait ...");
        
        try
        {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            student = new StudentThread(this, socket);            
        }
        catch (UnknownHostException uhe)
        {
            System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ex)
        {
            System.out.println("Unexpected exception: " + ex.getMessage());
        }        
    }
    
    private void sendAnswer()
    {
        if (question.add(qNumber))
        {
            String check = "-";
            if (qCorrectAnswer.equalsIgnoreCase(txtAns.getText()))
            {
                check = "+";
                JOptionPane.showMessageDialog(this, "Good Job!");
                txtAns.setText("");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Bad Luck!");
                txtAns.setText("");
            }

            String answer = qTopic + ", " + "Qn " + qNumber + ",_" + check;
            student.send(answer);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "You have answered this question");
            txtAns.setText("");
        }
        
    }
    
    public void displayQ(String q)
    {
        if (q.equals("quit"))
        {
            close();
            JOptionPane.showMessageDialog(this, "Server closed");
        }
        else
        {
            String temp[] = q.split("_");

            txtTopic.setText(temp[1]);        
            txaQuestion.setText(temp[2]);
            txaA.setText(temp[3]);
            txaB.setText(temp[4]);
            txaC.setText(temp[5]);
            txaD.setText(temp[6]); 

            qNumber = temp[0];
            qTopic = temp[1];
            qCorrectAnswer =temp[7]; 
        }               
    }
    
    private void close()
    {
        // close the socket and thread
        try 
        {
            socket.close();
            student.close();
        } 
        catch (IOException ex)
        {
            System.out.println("Socket error: " + ex.getMessage());
        }
    }
    
    
//</editor-fold>
    
    
    
}
