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
        System.out.println("Server Side ");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader( clientSocket.getInputStream()));

            //Set up stream for keyboard entry
            String info, inputLine, hitAnswer;

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

                            String messageBackToClient = r + ", " + c;

                            out.println(messageBackToClient);
                            System.out.println("Server> Click Position: " + messageBackToClient);

                        }
                    });
                }
            }

            while ((inputLine = in.readLine()) != null)
            {
                // *****************************************************************************
                // **************************RECEIVING FROM CLIENT******************************
                // *****************************************************************************
                System.out.println ("Client> " + inputLine);

                // *****************************************************************************
                // **********************PROCESS WHETHER OR NOT IT WAS HIT**********************
                // *****************************************************************************
                // inputLine is from the client
                info = "WAS Not Hit!";
                System.out.println("Server> Please Enter If Hit: " + info);
                out.println(info);   //send If hit to client



                // *****************************************************************************
                // ********************HERE THE SERVER NEEDS TO CHOOSE A BUTTON*****************
                // *****************************************************************************

                // User clicks buttons to do this operation

                // *****************************************************************************
                // **************************HERE SEND IT BACK TO THE CLIENT********************
                // *****************************************************************************
                // out.println(info2);
                hitAnswer = in.readLine(); //getting the answer from the server
                System.out.println("Client> " + hitAnswer);


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