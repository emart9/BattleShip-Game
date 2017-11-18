/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * Client class - Handles all server related issues with the class
 *
 *
 */


import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener
{
    // GUI items
    JButton connectButton;
    JTextField machineInfo;
    JTextField portInfo;
    JTextField message;
    JButton sendButton;

    // Network Items
    boolean connected;
    Socket echoSocket;
    PrintWriter out;
    BufferedReader in;

    // set up GUI
    public Client()
    {
        super( "Client" );

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout (new BorderLayout ());

        // set up the North panel
        JPanel upperPanel = new JPanel ();
        upperPanel.setLayout (new GridLayout (4,2));
        container.add (upperPanel, BorderLayout.NORTH);

        // create buttons
        connected = false;

        upperPanel.add ( new JLabel ("Message: ", JLabel.RIGHT) );
        message = new JTextField ("");
        message.addActionListener( this );
        upperPanel.add( message );

        sendButton = new JButton( "Send Message" );
        sendButton.addActionListener( this );
        sendButton.setEnabled (false);
        upperPanel.add( sendButton );

        connectButton = new JButton( "Connect to Server" );
        connectButton.addActionListener( this );
        upperPanel.add( connectButton );

        upperPanel.add ( new JLabel ("Server Address: ", JLabel.RIGHT) );
        machineInfo = new JTextField ("127.0.0.1");
        upperPanel.add( machineInfo );

        upperPanel.add ( new JLabel ("Server Port: ", JLabel.RIGHT) );
        portInfo = new JTextField ("");
        upperPanel.add( portInfo );

        setSize( 500, 250 );
        setVisible( true );

    } // end CountDown constructor

    // handle button event
    public void actionPerformed( ActionEvent event )
    {
        if ( connected &&
                (event.getSource() == sendButton ||
                        event.getSource() == message ) )
        {
            // doSendMessage();
        }
        else if (event.getSource() == connectButton) {
            doManageConnection();
        }
    }

    public void doSendMessage(String theMessage)
    {
        try
        {
            System.out.println("Client Side ");
            Scanner input = new Scanner(echoSocket.getInputStream());
            PrintWriter output = new PrintWriter(echoSocket.getOutputStream(), true);

            //Set up stream for keyboard entry
            String info, hitAnswer, pAnswer;

            //Send coordinates of ship hit
            System.out.println("Client> Click Position: " + theMessage);
            output.println(theMessage);   //send the numbers
            hitAnswer = input.nextLine(); //getting the answer from the server
            System.out.println("SERVER> " + hitAnswer);

            //Send if hit or not
            pAnswer = input.nextLine(); //getting the answer from the server
            System.out.println ("Server> " + pAnswer);
            info = "THOSE COORDINATES WERE HIT!";
            System.out.println(" Client> Please Enter If Hit: " + info);
            output.println(info); //send the numbers

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
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(
                        echoSocket.getInputStream()));
                sendButton.setEnabled(true);
                connected = true;
                connectButton.setText("Disconnect from Server");
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
} // end class Client