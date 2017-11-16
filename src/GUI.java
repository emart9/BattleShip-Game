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

public class GUI extends JFrame implements ActionListener {

    private MyJButton shipPositions[][] = new MyJButton[10][10];  //grid where user ships go on
    private MyJButton fired[][] = new MyJButton[10][10];   //grid that shows where user fires
    private Ship ships[] = new Ship[5];   //buttons array that holds ships
    private JPanel seaPanel = new JPanel();    //holds sea buttons grid
    private JPanel firedPanel = new JPanel();  //holds fired buttons grid
    private JPanel letterPanel = new JPanel(); //holds letter labels
    private JPanel numberPanel = new JPanel(); //holds number labels
    private JPanel shipPanel = new JPanel();   //holds ship buttons
    private JPanel gamePanel = new JPanel();   //panel that holds both grid panels and letter label panel
    JMenuBar bar = new JMenuBar();
    private final String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};  //labels array

    public GUI(){
        super("Battleship");

        JFrame container = new JFrame();
        container.setResizable(false);
        container.setSize(350, 700);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        container.setLocation(dim.width/2-container.getSize().width/2, dim.height/2-container.getSize().height/2);

        setUpGrid();  //sets up grid

        container.add(numberPanel, BorderLayout.WEST);
        container.add(gamePanel,BorderLayout.CENTER);
        container.add(shipPanel, BorderLayout.EAST);
        container.add(new JLabel("Not Connected", SwingConstants.CENTER), BorderLayout.SOUTH);
        container.getContentPane().setBackground(Color.RED);  //TODO changes color based on connection red for not connected

        makeMenu();
        container.setJMenuBar(bar);  //Adds the menu bar to the window
        container.setVisible(true);
        container.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent event) {
        MyJButton click = (MyJButton) event.getSource();

        //Window displayed when digit can't be place at this position
        JOptionPane.showMessageDialog(this,
                "Row: " + click.getRow() + "\nCol: " + click.getCol() +
                "\nValue: " + click.getValue(),
                "Button Values", JOptionPane.PLAIN_MESSAGE);
    }

    //Creates menu bar and attach it to GUI window
    //and adds all buttons like exit
    private void makeMenu() {
        //set up File menu and its menu items
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        //set up Stats submenu item under File
        JMenuItem statsItem = new JMenuItem("Stats");
        statsItem.setMnemonic('s');
        fileMenu.add(statsItem);
        statsItem.addActionListener(e ->
                JOptionPane.showMessageDialog(GUI.this,
                        "Stats\n", "Player Stats",
                        JOptionPane.PLAIN_MESSAGE) // end anonymous inner class
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
        JMenu helpMenu = new JMenu("Help");
        fileMenu.setMnemonic('H');

        //set up How to play submenu item under Help
        JMenuItem connectionItem = new JMenuItem("How to Connect");
        connectionItem.setMnemonic('c');
        helpMenu.add(connectionItem);
        // anonymous inner class
        connectionItem.addActionListener(
                event -> JOptionPane.showMessageDialog( GUI.this,
                        "STEPS ON HOW TO CONNECT\n",
                        "How to Connect",JOptionPane.PLAIN_MESSAGE )  // end anonymous inner class
        ); // end call to addActionListener

        //set up How to play submenu item under Help
        JMenuItem helpItem = new JMenuItem("How to Play");
        helpItem.setMnemonic('p');
        helpMenu.add(helpItem);
        // anonymous inner class
        helpItem.addActionListener(
                event -> JOptionPane.showMessageDialog( GUI.this,
                        "Begin the game by placing ships onto the bottom grid. Once all ships have been\n" +
                                 "placed by both players, the game begins. Players guess the locations of opposing\n" +
                                 "players ships by  clicking the top grid to fire onto enemy ships. Players will\n" +
                                 "alternate taking turns firing onto enemy ships. Once all opposing players ships\n" +
                                 "are sunk you are the winner.\n",
                        "How to Play",JOptionPane.PLAIN_MESSAGE )  // end anonymous inner class
        ); // end call to addActionListener

        // Adds all buttons bar
        bar.add(fileMenu);
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

                fired[row][col].addActionListener(this);
                firedPanel.add(fired[row][col]);

                shipPositions[row][col].addActionListener(this);
                seaPanel.add(shipPositions[row][col]);
            }
        }

        int shipIndex = 0;
        for (ShipType S : ShipType.values()) {
            ships[shipIndex] = new Ship(S);
            ships[shipIndex].setPreferredSize(new Dimension(30, 20));
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

        //sets layout
        shipPanel.setLayout(new GridLayout(5, 1));
        numberPanel.setLayout(new GridLayout(21, 1, 0, 10));
        letterPanel.setLayout(new GridLayout(1, 10, 20, 0));
        seaPanel.setLayout(new GridLayout(10, 10));
        firedPanel.setLayout(new GridLayout(10, 10));

        gamePanel.add(firedPanel);
        gamePanel.add(letterPanel);
        gamePanel.add(seaPanel);
    }
}