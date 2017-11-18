import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Server extends JFrame implements ActionListener{

    // GUI items
    JButton ssButton;
    JLabel machineInfo;
    JLabel portInfo;
    private boolean running;
    private MyJButton[][] playerGrid;
    private MyJButton[][] opponentGrid;
    private int hits;
    private int misses;


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

        hits = 0;
        misses = 0;
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

        setSize( 300, 100 );
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

    // player grid images
    final private String[] allHorizontalImages = {"batt201.gif", "batt202.gif", "batt203.gif"};
    final private String[] allVerticalImages = {"batt204.gif", "batt205.gif", "batt206.gif"};

    // opponent grid images
    final private String[] hitImages = {"batt102.gif", "batt103.gif"};  // first one is a miss, second one is a hit
    private int buttonRow = -1;
    private int buttonColumn = -1;



    public CommunicationThread (Socket clientSoc, Server ec3, MyJButton[][] player, MyJButton[][] opponent)
    {
        clientSocket = clientSoc;
        gui = ec3;

        playerGrid = player;
        opponentGrid = opponent;
        start();
    }


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

    private void doResultHitOnOpponent(String hit)
    {
        int hitOrMiss = Character.getNumericValue(hit.charAt(0));
        opponentGrid[buttonRow][buttonColumn].setIcon(new ImageIcon(setImage(hitImages[hitOrMiss])));

        if (hitOrMiss == 1)
            gui.incrimentHits();
        else
            gui.incrimentMisses();

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
            String wasHit, inputLine, hitAnswer;

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

                            String messageBackToClient = "" + r + "" + c + "";
                            out.println(messageBackToClient);
                            buttonRow = r;
                            buttonColumn = c;
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
                // parse the input line
                // char firstInt = inputLine.charAt(0);
                // char secondInt = inputLine.charAt(1);

                int row = Character.getNumericValue(inputLine.charAt(0));
                int column = Character.getNumericValue(inputLine.charAt(1));

                // *****************************************************************************
                // **********************PROCESS WHETHER OR NOT IT WAS HIT**********************
                // *****************************************************************************
                // inputLine is from the client
                // wasHit = "WAS Not Hit!";
                wasHit = wasHit(row, column);
                System.out.println("Server> Please Enter If Hit: " + wasHit);
                out.println(wasHit);   //send If hit to client


                // *****************************************************************************
                // ********************HERE THE SERVER NEEDS TO CHOOSE A BUTTON*****************
                // *****************************************************************************

                // System.out.print("SERVER_PRINT: Server> Please Enter Location: ");
                // info2 = userEntry.nextLine(); // INFO2 is the button to send back

                // *****************************************************************************
                // **************************HERE SEND IT BACK TO THE CLIENT********************
                // *****************************************************************************
                // out.println(info2);
                hitAnswer = in.readLine(); // getting the answer from the client
                System.out.println("Client> " + hitAnswer);
                doResultHitOnOpponent(hitAnswer);

                if(gui.getHits() >= 17){
                    JOptionPane.showMessageDialog(null,
                            "You won",
                            "Winner", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

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
        }
    }
}