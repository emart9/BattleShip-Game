/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * GUI class - Handles all GUI  related parts of the program. Such
 *             examples include the frame, side buttons and all their functionality,
 *             menu and all the submenu....along with all it functionality.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import javax.imageio.ImageIO;
import java.io.*;

public class GUI extends JFrame {

    private MyJButton shipPositions[][] = new MyJButton[10][10];  //grid where user ships go on
    private MyJButton fired[][] = new MyJButton[10][10];   //grid that shows where user fires
    private JButton rotateButton;
    private Ship ships[] = new Ship[5];   //buttons array that holds ships
    private JPanel seaPanel = new JPanel();    //holds sea buttons grid
    private JPanel firedPanel = new JPanel();  //holds fired buttons grid
    private JPanel letterPanel = new JPanel(); //holds letter labels
    private JPanel numberPanel = new JPanel(); //holds number labels
    private JPanel shipPanel = new JPanel();   //holds ship buttons
    private JPanel gamePanel = new JPanel();   //panel that holds both grid panels and letter label panel
    private JLabel statusLabel = new JLabel("Not connected",JLabel.CENTER);
    private userInteraction userInteraction;
    private Server server;
    private Client client;
    private boolean running = false;   //if the server is running turns to true
    private boolean connected = false;  //if the client is running turns to true

    private JMenuBar bar = new JMenuBar();
    private final String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};  //labels array

    public GUI(){
        super("Battleship");

        JFrame container = new JFrame();   //Container to holds all panels
        container.setResizable(false);
        container.setSize(450, 700);
        //Places window at center of users screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        container.setLocation(dim.width/2-container.getSize().width/2, dim.height/2-container.getSize().height/2);

        setUpGrid();  //sets up grid

        //adds panels to container
        container.add(numberPanel, BorderLayout.WEST);
        container.add(gamePanel,BorderLayout.CENTER);
        container.add(shipPanel, BorderLayout.EAST);
        container.add(statusLabel, BorderLayout.SOUTH);
        container.getContentPane().setBackground(Color.RED);

        makeMenu(container);   //builds menu
        container.setJMenuBar(bar);  //Adds the menu bar to the window
        container.setVisible(true);
        container.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        userInteraction = new userInteraction(shipPositions, fired, ships, rotateButton, client);
    }


    //Creates menu bar and attach it to GUI window
    //and adds all buttons like exit
    private void makeMenu(JFrame container) {
        //set up File menu and its menu items
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        //set up Stats submenu item under File
        JMenuItem statsItem = new JMenuItem("Stats");
        statsItem.setMnemonic('s');
        fileMenu.add(statsItem);
        statsItem.addActionListener(
                new ActionListener(){  // anonymous inner class
                    // terminate application when user clicks exitItem
                    public void actionPerformed(ActionEvent event){
                        if(server != null) {
                            JOptionPane.showMessageDialog(GUI.this,
                                    "Number of hits: " + server.getHits() +
                                    "\nNumber of misses: " + server.getMisses(), "Player Stats",
                                    JOptionPane.PLAIN_MESSAGE); // end anonymous inner class
                        }
                        else if(client != null){
                            JOptionPane.showMessageDialog(GUI.this,
                                    "Number of hits: " + client.getHits() +
                                            "\nNumber of misses: " + client.getMisses(), "Player Stats",
                                    JOptionPane.PLAIN_MESSAGE); // end anonymous inner class
                        }
                        else{
                            JOptionPane.showMessageDialog(GUI.this,
                                    "No Connection", "Player Stats",
                                    JOptionPane.PLAIN_MESSAGE); // end anonymous inner class
                        }
                    }
                }  // end anonymous inner class
        ); // end call to addActionListener

        //set up About submenu item under File
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('A');
        fileMenu.add(aboutItem);
        fileMenu.addSeparator();
        // display message dialog when user selects About...
        aboutItem.addActionListener(
                event -> JOptionPane.showMessageDialog( GUI.this,
                        "Authors:\n   Edgar Martinez-Ayala -> emart9\n" +
                                "   Alex Guler -> aguler3\n",
                        "About",JOptionPane.PLAIN_MESSAGE )  // end anonymous inner class
        ); // end call to addActionListener

        //set up Exit submenu item under File
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('x');
        fileMenu.add(exitItem);
        // terminate application when user clicks exitItem
        exitItem.addActionListener(
                event -> System.exit(0)  // end anonymous inner class
        ); // end call to addActionListener

        //set up Help menu and its menu items
        JMenu connectionMenu = new JMenu("Connect");
        fileMenu.setMnemonic('C');

        //set up server submenu item under connection
        JCheckBoxMenuItem serverItem = new JCheckBoxMenuItem("Server");
        serverItem.setMnemonic('s');
        connectionMenu.add(serverItem);
        // anonymous inner class
        serverItem.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent event) {
                        if(running == false) {
                            running = true;
                            server = new Server(shipPositions, fired);
                            container.getContentPane().setBackground(Color.GREEN);  //sets color to green
                            statusLabel.setText("Server Connected");
                        }
                    }
                }
        );

        //set up client submenu item under connection
        JCheckBoxMenuItem clientItem = new JCheckBoxMenuItem("Client");
        clientItem.setMnemonic('c');
        connectionMenu.add(clientItem);
        // anonymous inner class
        clientItem.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent event) {
                        if(connected == false) {
                            connected = true;
                            client = new Client(shipPositions, fired);
                            userInteraction.setClient(client);
                            container.getContentPane().setBackground(Color.GREEN);  //sets color to green
                            statusLabel.setText("Client Connected");
                        }
                    }
                }
        );

        //set up Help menu and its menu items
        JMenu helpMenu = new JMenu("Help");
        fileMenu.setMnemonic('H');

        //set up How to play submenu item under Help
        JMenuItem connectionItem = new JMenuItem("How to Connect");
        connectionItem.setMnemonic('c');
        helpMenu.add(connectionItem);
        // anonymous inner class
        connectionItem.addActionListener(
                event -> JOptionPane.showMessageDialog( GUI.this,
                        "If Server:\n" +
                                " On menu bar click connection and then click server in\n" +
                                " submenu. Afterwords click start listening and give the address and\n" +
                                " port number to the client player.\n" +
                                "If Client:\n" +
                                " On the menu bar click connection and then click client\n" +
                                " in the submenu. Then enter the address and port number given\n" +
                                " to you by the player that's the server and click connect\n" +
                                " to server button",
                        "How to Connect",JOptionPane.PLAIN_MESSAGE )  // end anonymous inner class
        ); // end call to addActionListener

        //set up How to play submenu item under Help
        JMenuItem helpItem = new JMenuItem("How to Play");
        helpItem.setMnemonic('p');
        helpMenu.add(helpItem);
        // anonymous inner class
        helpItem.addActionListener(
                event -> JOptionPane.showMessageDialog( GUI.this,
                        "Begin the game connectin and then by placing ships onto the bottom grid. Once\n" +
                                "all ships have been placed by both players, the game begins. Players guess the\n" +
                                "locations of opposing players ships by  clicking the top grid to fire onto\n" +
                                "enemy ships. Players will alternate taking turns firing onto enemy ships. Once\n" +
                                "all opposing players ships are sunk you are the winner.\n",
                        "How to Play",JOptionPane.PLAIN_MESSAGE )  // end anonymous inner class
        ); // end call to addActionListener

        // Adds all buttons bar
        bar.add(fileMenu);
        bar.add(connectionMenu);
        bar.add(helpMenu);
    }


    //Sets up the array of buttons that make
    //up the battleship grid
    private void setUpGrid() {
        //sets up buttons to initial state of fired buttons
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                //makes buttons and sets size
                fired[row][col] = new MyJButton("a", row + 1, col + 1, 0);
                fired[row][col].setPreferredSize(new Dimension(30, 30));
                shipPositions[row][col] = new MyJButton("b", row + 1, col + 1, 0);
                shipPositions[row][col].setPreferredSize(new Dimension(30, 30));


                try { //adds image to grid buttons
                    Image img1 = ImageIO.read(getClass().getResource("batt100.gif"));
                    fired[row][col].setIcon(new ImageIcon(img1));
                    fired[row][col].setHorizontalTextPosition(SwingConstants.CENTER);
                    shipPositions[row][col].setIcon(new ImageIcon(img1));
                    shipPositions[row][col].setHorizontalTextPosition(SwingConstants.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                firedPanel.add(fired[row][col]);

                seaPanel.add(shipPositions[row][col]);
            }
        }

        int shipIndex = 0;
        for (ShipType S : ShipType.values()) {
            ships[shipIndex] = new Ship(S);
            ships[shipIndex].setSize(new Dimension(50, 10));
            shipPanel.add(ships[shipIndex]);
            shipIndex++;
        }

        //adds labels onto panel
        for(int i = 1; i < 22; i++){
            if(i <  11){
                numberPanel.add(new JLabel(Integer.toString(i), SwingConstants.CENTER));
                letterPanel.add(new JLabel(letters[i-1], SwingConstants.CENTER));  //adds letter labels onto panel
            }
            else if(i == 11){
                numberPanel.add(new JLabel(""));
            }
            else{
                numberPanel.add(new JLabel(Integer.toString(i - 11), SwingConstants.CENTER));
            }
        }

        rotateButton = new JButton("Horizontal");  //makes a new button to handel ship orientation
        shipPanel.add(rotateButton);

        //sets layout
        shipPanel.setLayout(new GridLayout(6, 1));
        numberPanel.setLayout(new GridLayout(21, 1, 0, 10));
        letterPanel.setLayout(new GridLayout(1, 10, 20, 0));
        seaPanel.setLayout(new GridLayout(10, 10));
        firedPanel.setLayout(new GridLayout(10, 10));

        gamePanel.add(firedPanel);
        gamePanel.add(letterPanel);
        gamePanel.add(seaPanel);
    }
}



