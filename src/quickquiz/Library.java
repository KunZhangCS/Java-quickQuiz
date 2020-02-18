package quickquiz;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.awt.*;
import javafx.scene.layout.Border;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Kun
 */

public class Library
{
    public static JLabel LocateAJLabel(JFrame myJFrame, SpringLayout myJLabelLayout, String JLabelCaption, int x, int y)
    {	
        JLabel myJLabel = new JLabel(JLabelCaption);	
        myJFrame.add(myJLabel); 
	myJLabel.setForeground(new Color(0, 204, 0));
        myJLabelLayout.putConstraint(SpringLayout.WEST, myJLabel, x, SpringLayout.WEST, myJFrame);
        myJLabelLayout.putConstraint(SpringLayout.NORTH, myJLabel, y, SpringLayout.NORTH, myJFrame);
	
        return myJLabel;
    }
    
    public static JTextField LocateAJTextField(JFrame myJFrame, SpringLayout myJTextFieldLayout, int width, int x, int y)
    {
        JTextField myJTextField = new JTextField(width);
        myJFrame.add(myJTextField);  
        myJTextField.setEditable(false);
        myJTextFieldLayout.putConstraint(SpringLayout.WEST, myJTextField, x, SpringLayout.WEST, myJFrame);
        myJTextFieldLayout.putConstraint(SpringLayout.NORTH, myJTextField, y, SpringLayout.NORTH, myJFrame);
        return myJTextField;
    }

    public static JButton LocateAJButton(JFrame myJFrame, ActionListener myActnLstnr, SpringLayout myJButtonLayout, String  JButtonCaption, int x, int y, int w, int h)
    {    
        JButton myJButton = new JButton(JButtonCaption);
        myJFrame.add(myJButton);
        myJButton.addActionListener(myActnLstnr);
        myJButtonLayout.putConstraint(SpringLayout.WEST, myJButton, x, SpringLayout.WEST, myJFrame);
        myJButtonLayout.putConstraint(SpringLayout.NORTH, myJButton, y, SpringLayout.NORTH, myJFrame);
        myJButton.setPreferredSize(new Dimension(w,h));
        return myJButton;
    }

    public static JTextArea LocateAJTextArea(JFrame myJFrame, SpringLayout myLayout, int x, int y, int r, int c)
    {    
        JTextArea myJTextArea = new JTextArea(r,c);
        myJFrame.add(myJTextArea);
        myJTextArea.setEditable(false);
        myJTextArea.setLineWrap(true);
        myJTextArea.setBorder(BorderFactory.createLineBorder(Color.yellow));
        myLayout.putConstraint(SpringLayout.WEST, myJTextArea, x, SpringLayout.WEST, myJFrame);
        myLayout.putConstraint(SpringLayout.NORTH, myJTextArea, y, SpringLayout.NORTH, myJFrame);
        return myJTextArea;
    }
    
    public static JTable LocateAJTable(JFrame myJFrame, MouseListener myMouseLstnr, SpringLayout myLayout, AbstractTableModel myModel, int x, int y, int w, int h)
    {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        myJFrame.add(topPanel);
        
        JTable myJTable = new JTable(myModel);
        myJTable.setRowSelectionAllowed(true);
        myJTable.setColumnSelectionAllowed(true);
        myJTable.setSelectionBackground(Color.green);
        myJFrame.add(myJTable);        
        myJTable.addMouseListener(myMouseLstnr);
              
        JScrollPane scrollPane = new JScrollPane(myJTable);
                
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(w,h));
        myLayout.putConstraint(SpringLayout.WEST, topPanel, x, SpringLayout.WEST, myJFrame);
        myLayout.putConstraint(SpringLayout.NORTH, topPanel, y, SpringLayout.NORTH, myJFrame);
        return myJTable;
    }
}
