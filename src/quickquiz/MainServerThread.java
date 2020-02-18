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
public class MainServerThread extends Thread
{
    private MainServer server = null;
    private Socket socket = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private boolean running = true;

    public MainServerThread(MainServer _server, Socket _socket)
    {
        server = _server;
        socket = _socket;
        start();

    }

    public void run()
    {
        while (running)
        {
            try
            {
                streamIn = new DataInputStream(socket.getInputStream());
                String answer = streamIn.readUTF();
                server.answer(answer);
                if (answer.equals("quit")) 
                {
                    close();
                }
            }
            catch (IOException ex)
            {
                System.out.println("Data read in error: " + ex);
            }    
        }        
    }

    public void send(String question)
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            streamOut.writeUTF(question);
            streamOut.flush();
        }
        catch (IOException ex)
        {
            System.out.println("Error sending: " + ex);
        }
    }

    public void close() throws IOException 
    {
        running = false;
                
        if (socket != null)
        {
            socket.close();
        }      
        if (streamIn != null)
        {
            streamIn.close();
        }
        if (streamOut != null)
        {
            streamOut.close();
        }
        
        System.out.println("Student quit: " + socket);
    }



}
