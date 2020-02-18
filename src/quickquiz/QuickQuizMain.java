package quickquiz;

/**
*   PROGRAM:   QuickQuiz
*   AUTHOR:    Kun Zhang
* 
*   FUNCTION:  A system that delivers quick quiz questions via a classroom-based network-to a group of students.  

*   INPUT:     Teachers question file: QuizQuestions.txt

*   OUTPUT:    Selected questions with hashing: preOrder.txt, postOrder.txt, inOder.txt

*   NOTES:     The teacher(server) side program contains three main  parts: QuickQuizMain.java, MainServer.java and MainServerThread.java.
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;





public class QuickQuizMain extends JFrame implements ActionListener, MouseListener
{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    
    private JLabel lblQuickQuizTitle, lblQuickQuizQ, lblSortBy, lblTopic, lblQuestion, lblA, lblB, lblC, lblD, lblCorrectAns, lblLinkedList, lblBinaryTree, lblPreOrder, lblInOrder, lblPostOrder;
    private JButton btnQuestionNum, btnTopic, btnQuestion, btnExit, btnSend, btnDisplay, btnDisplayPre, btnDisplayIn, btnDisplayPost, btnSavePre, btnSaveIn, btnSavePost;
    private JTextField txtTopic, txtCorrectAns, txtQuestionNum;
    private JTextArea txaQuestion, txaA, txaB, txaC, txaD, txaLinkedList, txaBinaryTree;    
    private JTable table;
    
    // Total column numbers to display
    private int columnNumbers = 3;
    private String columnNames[] = new String[columnNumbers];    
    ArrayList<Object[]> dataValues = new ArrayList();
    //ArrayList<Object[]> tableDataValues = new ArrayList();
    private MyModel wordModel = new MyModel(dataValues, columnNames);
    
    // Selected row number
    private int rowSelected = -1;
        
    //Initialize the Binary Search Tree
    private BST binaryTree = new BST();
    
    //Initialize the server as a thread with the port number
    private int port = 8888;
    private MainServer server = new MainServer(this, port);
    
    SpringLayout springLayout = new SpringLayout();  
    
//</editor-fold>

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        QuickQuizMain quizApp = new QuickQuizMain();
        quizApp.run();
    }
    
    private void run()
    {
        
        setBounds(100, 100, 700, 650);
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                // Tell students server closed
                server.qSend("quit");
                
                try
                {
                    server.close();
                } 
                catch (IOException ex)
                {
                    System.out.println("Server close error" + ex);
                }
                
                System.exit(0);                 
            }
        });
        readQuestions("QuizQuestions.txt");       
        displayGUI();
        setResizable(false);
        setVisible(true);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Display GUI">
    
    private void displayGUI()
    {
        setLayout(springLayout);
        
        displayLables(springLayout);
        displayButtons(springLayout);
        displayTextFields(springLayout);
        displayTextAreas(springLayout); 
        displayTable(springLayout);
    }
    
    private void displayLables(SpringLayout layout)
    {
        lblQuickQuizTitle = Library.LocateAJLabel(this, layout, "Quick Quiz by NetWork", 10, 10);
        lblQuickQuizQ = Library.LocateAJLabel(this, layout, "Quick Quiz Questions", 10, 60);
        lblSortBy = Library.LocateAJLabel(this, layout, "Sort by:", 10, 250);
        lblTopic = Library.LocateAJLabel(this, layout, "Topic:", 400, 60);
        lblQuestion = Library.LocateAJLabel(this, layout, "Qn:", 400, 80);
        lblA = Library.LocateAJLabel(this, layout, "A:", 400, 130);
        lblB = Library.LocateAJLabel(this, layout, "B:", 400, 165);
        lblC = Library.LocateAJLabel(this, layout, "C:", 400, 200);
        lblD = Library.LocateAJLabel(this, layout, "D:", 400, 235); 
        lblCorrectAns = Library.LocateAJLabel(this, layout, "Correct Ans:", 340, 280);
        lblLinkedList = Library.LocateAJLabel(this, layout, "Linked List:", 10, 320);
        lblBinaryTree = Library.LocateAJLabel(this, layout, "Binary Tree:", 10, 420);
        lblPreOrder = Library.LocateAJLabel(this, layout, "Pre-Order", 60, 530);
        lblInOrder = Library.LocateAJLabel(this, layout, "In-Order", 300, 530);
        lblPostOrder = Library.LocateAJLabel(this, layout, "Post-Order", 525, 530);
        lblQuickQuizTitle.setFont(new Font("Arial", Font.BOLD, 30));        
    }
    
    private void displayButtons(SpringLayout layout)
    {
        btnQuestionNum = Library.LocateAJButton(this, this, layout, "Qn#", 60, 250, 80, 20);
        btnTopic = Library.LocateAJButton(this, this, layout, "Topic", 140, 250, 80, 20);
        btnQuestion = Library.LocateAJButton(this, this, layout, "Question", 220, 250, 100, 20);
        btnExit = Library.LocateAJButton(this, this, layout, "Exit", 10, 280, 180, 20);
        btnSend = Library.LocateAJButton(this, this, layout, "Send", 480, 280, 180, 20);
        btnDisplay = Library.LocateAJButton(this, this, layout, "Display", 560, 420, 100, 20);
        btnDisplayPre = Library.LocateAJButton(this, this, layout, "Display", 10, 550, 80, 20);
        btnSavePre = Library.LocateAJButton(this, this, layout, "Save", 90, 550, 80, 20);
        btnDisplayIn = Library.LocateAJButton(this, this, layout, "Display", 240, 550, 80, 20);
        btnSaveIn = Library.LocateAJButton(this, this, layout, "Save", 320, 550, 80, 20);
        btnDisplayPost = Library.LocateAJButton(this, this, layout, "Display", 480, 550, 80, 20);
        btnSavePost = Library.LocateAJButton(this, this, layout, "Save", 560, 550, 80, 20);
    }
    
    private void displayTextFields(SpringLayout layout)
    {
        txtTopic = Library.LocateAJTextField(this, layout, 20, 440, 60);
        txtQuestionNum = Library.LocateAJTextField(this, layout, 2, 400, 100);
        txtCorrectAns = Library.LocateAJTextField(this, layout, 4, 420, 280);       
    }
    
    private void displayTextAreas(SpringLayout layout)
    {
        txaQuestion = Library.LocateAJTextArea(this, layout, 441, 80, 3, 20);
        txaA = Library.LocateAJTextArea(this, layout, 441, 130, 2, 20);
        txaB = Library.LocateAJTextArea(this, layout, 441, 165, 2, 20);
        txaC = Library.LocateAJTextArea(this, layout, 441, 200, 2, 20);
        txaD = Library.LocateAJTextArea(this, layout, 441, 235, 2, 20);
        txaLinkedList =Library.LocateAJTextArea(this, layout, 10, 340, 4, 59);
        txaBinaryTree =Library.LocateAJTextArea(this, layout, 10, 450, 4, 59);
    }
    
    private void displayTable(SpringLayout layout)
    {      
        table = Library.LocateAJTable(this, this, layout, wordModel, 10, 80, 370, 160);     
        table.getColumnModel().getColumn(0).setMaxWidth(30);       
        table.getColumnModel().getColumn(1).setMaxWidth(100);
       
    }
    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Action and Mouse Listener">
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnQuestionNum)
        {
            bubbleSort(dataValues);
            wordModel.fireTableDataChanged();
        }
        
        if (e.getSource() == btnTopic )
        {
            insertionSort(dataValues);
            wordModel.fireTableDataChanged();
        }
        
        if (e.getSource() == btnQuestion)
        {
            selectionSort(dataValues);
            wordModel.fireTableDataChanged();
        }
        
        if (e.getSource() == btnExit) 
        {
           // Tell students server closed
            server.qSend("quit");
            
            try
            {
                server.close();
            } 
            catch (IOException ex)
            {
                System.out.println("Server close error" + ex);
            }                
            
            System.exit(0);            
        }
        
        if (e.getSource() == btnSend) 
        {
            try 
            {
                addBST();
                sendQ();
            }
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage() + " Select a question first.");
            }            
        }
        
        if (e.getSource() == btnDisplayPre)
        {
            try 
            {
                String temp = "Pre-Order: " + binaryTree.preOrderTraverse(binaryTree.root);
                temp = temp.substring(0, temp.lastIndexOf(","));
                txaBinaryTree.setText(temp);
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "No Binary Tree");
            }
        }
        
        if (e.getSource() == btnDisplayIn)
        {
            try 
            {
                String temp = "In-Order: " + binaryTree.inOrderTraverse(binaryTree.root);
                temp = temp.substring(0, temp.lastIndexOf(","));
                txaBinaryTree.setText(temp);
            } 
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, "No Binary Tree");
            }
        }
        
        if (e.getSource() == btnDisplayPost)
        {
            try 
            {
                String temp = "Post-Order: " + binaryTree.postOrderTraverse(binaryTree.root);
                temp = temp.substring(0, temp.lastIndexOf(","));
                txaBinaryTree.setText(temp);
            }
            catch (Exception ex) 
            {               
                JOptionPane.showMessageDialog(this, "No Binary Tree");
            }
        }
        
        if (e.getSource()== btnSavePre)
        {
            String h = binaryTree.preOrderHash(binaryTree.root);
            h = h.substring(0, h.lastIndexOf("_"));
            saveHash(h, "preOrder.txt");
        }
        
        if (e.getSource() == btnSaveIn)
        {
            String h = binaryTree.inOrderHash(binaryTree.root);
            h = h.substring(0, h.lastIndexOf("_"));
            saveHash(h, "inOrder.txt");
        }
        
        if (e.getSource() == btnSavePost)
        {
            String h = binaryTree.postOrderHash(binaryTree.root);
            h = h.substring(0, h.lastIndexOf("_"));
            saveHash(h, "postOrder.txt");
        }
        
        if (e.getSource() == btnDisplay)
        {
//            try 
//            {
//                // Dependency injection
//                DisplayBT displayBT = new DisplayBT();
//                displayBT.run(binaryTree);
//            }
//            catch (Exception ex)
//            {
//                JOptionPane.showMessageDialog(this, "No Binary Tree");
//            }            
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    { 
        rowSelected = table.getSelectedRow();
        displaySingleQ();
    }
    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    
    // Set the textfield with relevant values
    private void displaySingleQ()
    {         
        txtQuestionNum.setText((String)dataValues.get(rowSelected)[0]);
        txtTopic.setText((String)dataValues.get(rowSelected)[1]);
        txaQuestion.setText((String)dataValues.get(rowSelected)[2]);
        txaA.setText((String)dataValues.get(rowSelected)[3]);
        txaB.setText((String)dataValues.get(rowSelected)[4]);
        txaC.setText((String)dataValues.get(rowSelected)[5]);
        txaD.setText((String)dataValues.get(rowSelected)[6]);
        txtCorrectAns.setText((String)dataValues.get(rowSelected)[7]);
    }
    
    public void printDLL(String s)
    {    
        txaLinkedList.setText(s);
    }
    
    private void addBST()
    {
        int key = Integer.valueOf((String)dataValues.get(rowSelected)[0]);
        String topic = (String)dataValues.get(rowSelected)[1];
        binaryTree.add(key, topic);          
    }
    
    // This method is to send the selectec question to students.
    // Using the qSend method in  Mainserver.java
    // Then this qSend wil call the method send in MainServerTread
    // This is the Dependency Injection
    // Also the third party library is used in this method
    private void sendQ()
    {
//        String question = "";    
//        
//        for (int i = 0; i < dataValues.get(rowSelected).length; i++) 
//        {
//            question += (String)dataValues.get(rowSelected)[i] + "_";
//        }
//        
//        for (Object get : dataValues.get(rowSelected)) 
//        {
//            question += (String) get + "_";
//        }
        
        String question = dataValues.get(rowSelected)+ "_";
        
        server.qSend(question);           
    }
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="File Management">
    
    // This method is for reading the questiongs from the QuizQuestions.txt file    
    private void readQuestions(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            // Get the column names
            String temp1[] = br.readLine().split("_");
            for (int i = 0; i < columnNumbers; i++)
            {
                columnNames[i] = temp1[i + 1];
            }
           
            String line;
            while ((line= br.readLine())!= null)
            {
                String temp2[] = line.split("_");
                // Get data values without the first column
                String temp3[] = new String[temp2.length - 1];
                for (int i = 0; i < temp3.length; i++) 
                {
                    temp3[i] = temp2[i + 1];
                }
                dataValues.add(temp3);
            }
            br.close();
        } 
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // this method is to save the binary tree result with hashing
    private void saveHash(String h, String fileName)
    {
        try
        {
            BufferedWriter outFile = new BufferedWriter(new FileWriter(fileName));
            outFile.write(h);
            outFile.close();
            JOptionPane.showMessageDialog(this, fileName + " saved");
        } 
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Error: " + e);
        }
    }
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Sort Algorithm">
    
    // Sort algorithm for button Qn#
    private void bubbleSort(ArrayList<Object[]> arr) 
    {        
        for(int j=0; j<arr.size(); j++) 
        {  
            for(int i=j+1; i<arr.size(); i++)
            {  
                if((arr.get(i)[0]).toString().compareToIgnoreCase(arr.get(j)[0].toString())>0)
                {  
                   Object[] words = arr.get(j); 
                   arr.set(j, arr.get(i));
                   arr.set(i, words);
                }  
            }   
        }  
    }
    // Sort algorithm for button Topic
    private void insertionSort(ArrayList<Object[]> arr) 
    {  
        int i;
        int j;
        Object[] words;
        for(j=1; j<arr.size(); j++) 
        {    
            words = arr.get(j);
            for(i=j-1; (i>=0) && ((arr.get(i)[1]).toString().compareToIgnoreCase(words[1].toString())<0); i--)
            {  
                arr.set(i+1, arr.get(i));
            }  
            arr.set(i+1, words);
        }   
    }
    // Sort algorithm for button Questions
    private void selectionSort(ArrayList<Object[]> arr)
    {
        for (int i = 0; i < arr.size() - 1; i++)
        {
            int first = i;
            for (int j = i + 1; j < arr.size(); j++)
            {
                if ((arr.get(j)[2]).toString().compareToIgnoreCase(arr.get(first)[2].toString())<0)
                    first = j;
            }
            Object[] words = arr.get(first);
            arr.set(first, arr.get(i));
            arr.set(i, words);
        }
    }
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Inner Class">
     
    class MyModel extends AbstractTableModel
    {
        ArrayList<Object[]> al;

        // the headers
        String[] header;

        // constructor 
        MyModel(ArrayList<Object[]> obj, String[] header)
        {
            // save the header
            this.header = header;
            // and the data
            al = obj;
        }

        // method that needs to be overload. The row count is the size of the ArrayList

        public int getRowCount()
        {
            return al.size();
        }

        // method that needs to be overload. The column count is the size of our header
        public int getColumnCount()
        {
            return header.length;
        }

        // method that needs to be overload. The object is in the arrayList at rowIndex
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return al.get(rowIndex)[columnIndex];
        }

        // a method to return the column name 
        public String getColumnName(int index)
        {
            return header[index];
        }

        // a method to add a new line to the table
        void add(String word1, String word2)
        {
            // make it an array[2] as this is the way it is stored in the ArrayList
            // (not best design but we want simplicity)
            String[] str = new String[2];
            str[0] = word1;
            str[1] = word2;
            al.add(str);
            // inform the GUI that I have change
            fireTableDataChanged();
        }
    }
    
//</editor-fold>            
}
