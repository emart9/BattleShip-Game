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
import javax.imageio.ImageIO;
import javax.swing.*;

public class Client extends JFrame implements ActionListener
{
    // GUI items
    JButton connectButton;
    JTextField machineInfo;
    JTextField portInfo;
    // Network Items
    boolean connected;
    Socket echoSocket;
    PrintWriter out;
    BufferedReader in;

    private MyJButton[][] playerGrid;
    private MyJButton[][] opponentGrid;
    private int hits;
    private int misses;


    // set up GUI
    public Client(MyJButton[][] player, MyJButton[][] opponent)
    {
        super( "Client" );

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout (new BorderLayout ());
        playerGrid = player;
        opponentGrid = opponent;
        hits = 0;
        misses = 0;

        // set up the North panel
        JPanel upperPanel = new JPanel ();
        upperPanel.setLayout (new GridLayout (3,2));
        container.add (upperPanel, BorderLayout.NORTH);

        // create buttons
        connected = false;

        upperPanel.add ( new JLabel ("Server Address: ", JLabel.LEFT) );
        machineInfo = new JTextField ("127.0.0.1");
        upperPanel.add( machineInfo );

        upperPanel.add ( new JLabel ("Server Port: ", JLabel.LEFT) );
        portInfo = new JTextField ("");
        upperPanel.add( portInfo );

        connectButton = new JButton( "Connect to Server" );
        connectButton.addActionListener( this );
        upperPanel.add( connectButton);

        setSize( 300, 125 );
        setVisible( true );

    } // end CountDown constructor


    public void incrimentHits() {
        this.hits++;
    }

    public int getHits() {
        return hits;
    }

    public void incrimentMisses(){
        this.misses++;
    }

    public int getMisses(){
        return misses;
    }

    // handle button event
    public void actionPerformed( ActionEvent event )
    {
        if (event.getSource() == connectButton) {
            doManageConnection();
        }
    }

    // player grid images
    final private String[] allHorizontalImages = {"batt201.gif", "batt202.gif", "batt203.gif"};
    final private String[] allVerticalImages = {"batt204.gif", "batt205.gif", "batt206.gif"};

    // opponent grid images
    final private String[] hitImages = {"batt102.gif", "batt103.gif"};  // first one is a miss, second one is a hit
    private int buttonRow = -1;
    private int buttonColumn = -1;

    private Image setImage(String file)
    {
        Image i = null;
        try
        {
            i = ImageIO.read(getClass().getResource(file));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return i;
    }

    private void doResultHitOnOpponent(String hit)
    {
        int hitOrMiss = Character.getNumericValue(hit.charAt(0));
        opponentGrid[buttonRow][buttonColumn].setIcon(new ImageIcon(setImage(hitImages[hitOrMiss])));

        if (hitOrMiss == 1)
            this.incrimentHits();
        else
            this.incrimentMisses();
    }

    private String wasHit(int row, int column)
    {
        String wasHitString;

        if(playerGrid[row][column].getValue() == 1) // it is occupied so they got a hit!
        {
            // playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
            playerGrid[row][column].setIcon(new ImageIcon(setImage(allHorizontalImages[1])));
            playerGrid[row][column].setValue(-1);
            wasHitString = "" + 1 + "";
        }
        else
        {
            wasHitString = "" + 0 + "";
        }

        return wasHitString;
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

            buttonRow = Character.getNumericValue(theMessage.charAt(0));
            buttonColumn = Character.getNumericValue(theMessage.charAt(1));

            //Send coordinates of ship hit
            System.out.println("Client> Click Position: " + theMessage);
            output.println(theMessage);   //send the numbers
            hitAnswer = input.nextLine(); //getting the answer from the server
            System.out.println("SERVER> " + hitAnswer);

            doResultHitOnOpponent(hitAnswer);

            if(getHits() >= 17){
                JOptionPane.showMessageDialog(null,
                        "You won",
                        "Winner", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

            //Send if hit or not
            pAnswer = input.nextLine(); //getting the answer from the server

            int r = Character.getNumericValue(pAnswer.charAt(0));
            int c = Character.getNumericValue(pAnswer.charAt(1));

            System.out.println ("Server> " + pAnswer);
            info = wasHit(r, c);
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