/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * Client class - Handles all server related issues with the class
 *
 *
 */

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Client extends JFrame implements ActionListener {
    // GUI items
    JButton sendButton;
    JButton connectButton;
    JTextField machineInfo;
    JTextField portInfo;

    // Network Items
    boolean connected = false;
    Socket echoSocket;
    PrintWriter out;
    BufferedReader in;
    private int value;


    // set up GUI
    public Client() {
        super( "Echo Client" );
        value = 0;

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout (new BorderLayout ());

        // set up the North panel
        JPanel upperPanel = new JPanel ();
        upperPanel.setLayout (new GridLayout (3,2));
        container.add (upperPanel, BorderLayout.NORTH);

        upperPanel.add ( new JLabel ("Server Address: ", JLabel.LEFT) );
        machineInfo = new JTextField ("127.0.0.1");
        upperPanel.add( machineInfo );

        upperPanel.add ( new JLabel ("Server Port: ", JLabel.LEFT) );
        portInfo = new JTextField ("");
        upperPanel.add( portInfo );

        connectButton = new JButton( "Connect to Server" );
        connectButton.addActionListener( this );
        upperPanel.add( connectButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize( 300, 150 );
        setVisible( true );

    } // end CountDown constructor

    // handle button event
    public void actionPerformed( ActionEvent event ){
        if ( connected )
        {
            doSendMessage();
        }
        else if (event.getSource() == connectButton)
        {
            doManageConnection();
            doSendMessage();
            connected = true;
        }
    }

    public void doSendMessage()
    {
        try
        {
            Scanner input = new Scanner(echoSocket.getInputStream());
            PrintWriter output = new PrintWriter(echoSocket.getOutputStream(), true);

            //Set up stream for keyboard entry
            Scanner userEntry = new Scanner(System.in);

            int firstInt, secondInt, answer, firstInt2, secondInt2;
            do {
                System.out.print("Please input the first number: ");
                firstInt = userEntry.nextInt();
                System.out.print("Please input the second number: ");
                secondInt = userEntry.nextInt();

                //send the numbers
                output.println(firstInt);
                output.println(secondInt);
                answer = input.nextInt(); //getting the answer from the server
                System.out.println("\nSERVER> " + answer);
                firstInt2 = input.nextInt();
                secondInt2 = input.nextInt();
                System.out.println("\nSERVER> " + firstInt2 + " " + secondInt2);
            } while (firstInt != 0 || secondInt != 0);
        }
        catch (IOException e)
        {
            System.out.println("Error in processing message ");
        }
    }

    public void doManageConnection()
    {
        if (connected == false)
        {
            String machineName = null;
            int portNum = -1;
            try {
                machineName = machineInfo.getText();
                portNum = Integer.parseInt(portInfo.getText());
                echoSocket = new Socket(machineName, portNum );
                connected = true;
            } catch (NumberFormatException e) {
                System.out.println( "Server Port must be an integer\n");
            } catch (UnknownHostException e) {
                System.out.println("Don't know about host: " + machineName);
            } catch (IOException e) {
                System.out.println("Couldn't get I/O for "
                        + "the connection to: " + machineName);
            }
        }
        else
        {
            try
            {
                out.close();
                in.close();
                echoSocket.close();
                sendButton.setEnabled(false);
                connected = false;
                connectButton.setText("Connect to Server");
            }
            catch (IOException e)
            {
                System.out.println("Error in closing down Socket ");
            }
        }
    }

} // end class client