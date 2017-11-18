///**
// * @authors Edgar Martinez-Ayala and Alex Guler
// * Server class - Handles all server related issues with the class
// *
// *
// */
//
//import java.net.*;
//import java.io.*;
//import java.util.*;
//
//public class Server{
//    // Network Items
//    ServerSocket serverSocket;
//    private int PORT;
//    private String machineAddress;
//    private boolean running = false;
//
//    // set up Connection
//    public Server() {
//        try {
//            InetAddress addr = InetAddress.getLocalHost();
//            machineAddress = addr.getHostAddress();
//        } catch (UnknownHostException e) {
//            machineAddress = "127.0.0.1";
//        }
//
//        //code to test server
//        System.out.println("Opening port...\n");
//        System.out.println(machineAddress);
//
//        try {
//            serverSocket = new ServerSocket(0);
//            PORT = serverSocket.getLocalPort();
//
//        } catch (IOException ioex) {
//            System.out.println("Unable to attach to port!");
//            System.exit(1);
//        }
//        running = true;
//
//    } // end CountDown constructor
//
//    public int getPORT(){
//        return this.PORT;
//    }
//    public String getAddress(){
//        return this.machineAddress;
//    }
//
//    public void handleClient()
//    {
//        Socket link = null; //Step 1
//        try {
//            link = serverSocket.accept(); //Step 2
//            //Step 3
//            Scanner input = new Scanner(link.getInputStream());
//            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
//            int firstInt = input.nextInt();
//            int secondInt = input.nextInt();
//            int answer, firstInt2, secondInt2;
//
//            while (firstInt != -1 || secondInt != -1)
//            {
//                answer = firstInt + secondInt;
//
//                output.println(answer); //Server returns the sum here 4
//                Scanner userEntry = new Scanner(System.in);
//                System.out.print("Please input the first number: ");
//                firstInt2 = userEntry.nextInt();
//                System.out.print("Please input the second number: ");
//                secondInt2 = userEntry.nextInt();
//                output.println(firstInt2);
//                output.println(secondInt2);
//
//                firstInt = input.nextInt();
//                secondInt = input.nextInt();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                System.out.println("Closing connection...");
//                link.close();
//            }
//            catch (IOException ie)
//            {
//                System.out.println("Unable to close connection");
//                System.exit(1);
//            }
//        }
//    }
//} // end class EchoServer3
//
//

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.swing.*;

public class Server extends JFrame implements ActionListener{

    // GUI items
    JButton ssButton;
    JLabel machineInfo;
    JLabel portInfo;
    private boolean running;
    private MyJButton[][] playerGrid;
    private MyJButton[][] opponentGrid;


    // Network Items
    boolean serverContinue;
    ServerSocket serverSocket;

    // set up GUI
    public Server(MyJButton[][] player, MyJButton[][] opponent)
    {
        super( "Echo Server" );

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout( new FlowLayout() );

        playerGrid = player;
        opponentGrid = opponent;

        // create buttons
        running = false;
        ssButton = new JButton( "Start Listening" );
        ssButton.addActionListener( this );
        container.add( ssButton );

        String machineAddress = null;
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            machineAddress = addr.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            machineAddress = "127.0.0.1";
        }
        machineInfo = new JLabel (machineAddress);
        container.add( machineInfo );
        portInfo = new JLabel (" Not Listening ");
        container.add( portInfo );

        setSize( 500, 250 );
        setVisible( true );

    } // end CountDown constructor

    // handle button event
    public void actionPerformed( ActionEvent event )
    {
        if (running == false)
        {
            new ConnectionThread (this, this.playerGrid, this.opponentGrid);
        }
        else
        {
            serverContinue = false;
            ssButton.setText ("Start Listening");
            portInfo.setText (" Not Listening ");
        }
    }


} // end class EchoServer3


class ConnectionThread extends Thread
{
    Server gui;
    private MyJButton[][] playerGrid;
    private MyJButton[][] opponentGrid;


    public ConnectionThread (Server es3, MyJButton[][] player, MyJButton[][] opponent)
    {
        gui = es3;
        playerGrid = player;
        opponentGrid = opponent;
        start();
    }

    public void run()
    {
        gui.serverContinue = true;

        try
        {
            gui.serverSocket = new ServerSocket(0);
            gui.portInfo.setText("Listening on Port: " + gui.serverSocket.getLocalPort());
            System.out.println ("Connection Socket Created");
            try {
                while (gui.serverContinue)
                {
                    System.out.println ("Waiting for Connection");
                    gui.ssButton.setText("Stop Listening");
                    new CommunicationThread (gui.serverSocket.accept(), gui, playerGrid, opponentGrid);
                }
            }
            catch (IOException e)
            {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        }
        finally
        {
            try {
                gui.serverSocket.close();
            }
            catch (IOException e)
            {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }
}


class CommunicationThread extends Thread
{
    //private boolean serverContinue = true;
    private Socket clientSocket;
    private Server gui;
    private MyJButton[][] playerGrid;
    private MyJButton[][] opponentGrid;


    public CommunicationThread (Socket clientSoc, Server ec3, MyJButton[][] player, MyJButton[][] opponent)
    {
        clientSocket = clientSoc;
        gui = ec3;
        playerGrid = player;
        opponentGrid = opponent;
        start();
    }

    public void run()
    {
        System.out.println ("New Communication Thread Started");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader( clientSocket.getInputStream()));

            //Set up stream for keyboard entry

            String info, info2, inputLine, hitAnswer;

            for(int row = 0; row < 10; ++row)
            {
                for(int column = 0; column < 10; ++column)
                {
                    opponentGrid[row][column].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            MyJButton B = (MyJButton) e.getSource();
                            int r = B.getRow() - 1;
                            int c = B.getCol() - 1;

                            String messageBackToClient = "Row: " + r + " and Column: " + c + " was clicked From Server!\n";

                            out.println(messageBackToClient);

                        }
                    });
                }
            }

            while ((inputLine = in.readLine()) != null)
            {
                // *****************************************************************************
                // **************************RECEIVING FROM CLIENT******************************
                // *****************************************************************************
                System.out.println ("SERVER_PRINT: Client> " + inputLine);
                // System.out.print("Server> Please Enter If Hit: ");
                // info = userEntry.nextLine();

                // *****************************************************************************
                // **********************PROCESS WHETHER OR NOT IT WAS HIT**********************
                // *****************************************************************************
                // inputLine is from the client
                info = "WAS HIT!";
                out.println(info);   //send If hit to client



                // *****************************************************************************
                // ********************HERE THE SERVER NEEDS TO CHOOSE A BUTTON*****************
                // *****************************************************************************

                // System.out.print("SERVER_PRINT: Server> Please Enter Location: ");
                // info2 = userEntry.nextLine(); // INFO2 is the button to send back

                // *****************************************************************************
                // **************************HERE SEND IT BACK TO THE CLIENT********************
                // *****************************************************************************
                // out.println(info2);
                hitAnswer = in.readLine(); //getting the answer from the server
                System.out.println("SERVER_PRINT: Client> " + hitAnswer);

                if (inputLine.equals("Bye."))
                    break;

                if (inputLine.equals("End Server."))
                    gui.serverContinue = false;
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            System.err.println("Problem with Communication Server");
            //System.exit(1);
        }
    }
}
