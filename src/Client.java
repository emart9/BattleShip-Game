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

public class Client extends JFrame implements ActionListener {
    // GUI items
    JButton sendButton;
    JButton connectButton;
    JTextField machineInfo;
    JTextField portInfo;
    JTextField message;
    JTextArea history;

    // Network Items
    boolean connected;
    Socket echoSocket;
    PrintWriter out;
    BufferedReader in;

    // set up GUI
    public Client() {
        super( "Echo Client" );

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

        history = new JTextArea ( 10, 40 );
        history.setEditable(false);
        container.add( new JScrollPane(history) ,  BorderLayout.CENTER);

        setSize( 500, 250 );
        setVisible( true );

    } // end CountDown constructor

    // handle button event
    public void actionPerformed( ActionEvent event ) {
        if ( connected &&
                (event.getSource() == sendButton ||
                        event.getSource() == message ) ) {
            doSendMessage();
        }
        else if (event.getSource() == connectButton) {
            doManageConnection();
        }
    }

    public void doSendMessage() {
        try {
            out.println(message.getText());
            history.insert ("From Client: " + in.readLine() + "\n" , 0);
        }
        catch (IOException e) {
            history.insert ("Error in processing message ", 0);
        }
    }

    public void doManageConnection() {
        if (connected == false) {
            String machineName = null;
            int portNum;//= -1;
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
                history.insert ( "Server Port must be an integer\n", 0);
            } catch (UnknownHostException e) {
                history.insert("Don't know about host: " + machineName , 0);
            } catch (IOException e) {
                history.insert ("Couldn't get I/O for "
                        + "the connection to: " + machineName , 0);
            }

        }
        else {
            try {
                out.close();
                in.close();
                echoSocket.close();
                sendButton.setEnabled(false);
                connected = false;
                connectButton.setText("Connect to Server");
            }
            catch (IOException e) {
                history.insert ("Error in closing down Socket ", 0);
            }
        }
    }
} // end class EchoServer3