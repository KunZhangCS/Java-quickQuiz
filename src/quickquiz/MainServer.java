/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickquiz;

import java.io.*;
import java.net.*;

/**
 *
 * @author student
 */
public class MainServer extends Thread
{
    private QuickQuizMain qqMain = null;
    private ServerSocket server = null;
    private MainServerThread students[] = new MainServerThread[50];
    private int studentCount = 0;
    
    // Counter for incorrect answers
    private int counter = 0;
    
    // Initialize the Doubly-Linked List
    private DLL dll = new DLL();
        
    
    public MainServer(QuickQuizMain _qqMain, int port)
    {
        qqMain = _qqMain;
        
        try
        {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } 
        catch (IOException ex)
        {
            System.out.println("Can not bind to port " + port + ": " + ex.getMessage());
        }
        
    }
    
    public void run()
    {
        while (true)
        {
            System.out.println("server");
            try
            {      
                System.out.println("Waiting for a student ...");
                addThread(server.accept());
            } 
            catch (IOException ex)
            {
                System.out.println("Server accept error: " + ex);
            }
        } 
    }
    
    private void addThread(Socket socket)
    {
        if (studentCount < students.length)
        {
            System.out.println("Student accepted: " + socket);
            students[studentCount] = new MainServerThread(this, socket);
            studentCount++;
        }
        else
        {
            System.out.println("Student refused: maximum " + students.length + " reached.");
        }  
    }
    
    public void qSend(String question)
    {
        for(int i = 0; i < studentCount; i++)
        {
            students[i].send(question);            
        }
        counter = 0;
    }
    
    public void answer(String a)
    {
        String[] temp = a.split("_");
        
        String question = temp[0];
        String check = temp[1];
        
        if (check.equals("-"))
        {
            counter++;
            dll.prepend(question, Integer.toString(counter));
            
            if (counter > 1)
            {
                dll.remove(question, Integer.toString(counter - 1));
            }
            
            qqMain.printDLL(dll.printList());
        }
    }
    
    public void close() throws IOException
    {
        for(int i = 0; i < studentCount; i++)
        {
            students[i].close();            
        }
    }
}
