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
public class StudentThread extends Thread
{
    private Socket socket = null;
    private QuickQuizStudent quickQuizStudent = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private boolean running = true;
    
    public StudentThread(QuickQuizStudent _quickQuizStudent, Socket _socket)
    {
        socket = _socket;
        quickQuizStudent = _quickQuizStudent;
        start();
    }
    
    public void run()
    {
        while (running)
        {
            try 
            {
                streamIn = new DataInputStream(socket.getInputStream());
                String q = streamIn.readUTF();
                quickQuizStudent.displayQ(q);
            } 
            catch (IOException ex)
            {
                System.out.println("Data read in error: " + ex.getMessage());;
            }
        }
        System.out.println("StudentThread stopped");
    }
    
    public void send(String answer)
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            streamOut.writeUTF(answer);
            streamOut.flush();
            System.out.println("Answer Sent");
        }
        catch (IOException ex)
        {
            System.out.println("Error sending: " + ex);
        }
    }
    
    public void close() throws IOException
    {
        running = false;
            
        if (streamIn != null)
        {
            streamIn.close();
        }
        if (streamOut != null)
        {
            streamOut.close();
        }
    }
}
