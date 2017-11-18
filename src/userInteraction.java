/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * userInteraction class - Handles most of the action listeners that the user can do.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class userInteraction
{
    private MyJButton[][] playerGrid;   // player's grid
    private MyJButton[][] opponentGrid; // opponent's grid
    private JButton rotateButton;       // the button for rotation
    private Ship[] ships;               // all the possible ships in an array
    private Ship shipClicked;           // if the user hits a ship that they want to place it goes into this
    private Client client;              // the client pointer

    // user interaction class constructor
    public userInteraction(MyJButton[][] player, MyJButton[][] opponent, Ship[] allShips, JButton rotate, Client c)
    {
        playerGrid = player;
        opponentGrid = opponent;
        rotateButton = rotate;
        ships = allShips;
        shipClicked = null;
        client = c;

        // this action listeners waits for the user to click one of the ships on the side
        // and it sets that ship to the shipClicked so that we know what ship they want to put on the grid
        configureShipsActionListeners();

        // this action listener if for when the user clicks rotate if they want to rotate a ship
        // it also displays an error message if they click it without clicking another ship
        configureRotateActionListener();

        // this action listener places whatever ship was clicked on the grid
        // it only place a ship if a ship was clicked
        configurePlayerGridActionListeners();

        // this action listener is for the client sides opponent grid
        // all it does is send the coordinates of where the user wants to fire
        configureOpponentGridActionListeners();


    }

    // just sets the client pointer to the client inside of userInteraction class
    public void setClient(Client c)
    {
        client = c;
    }


    // this action listener is for the client sides opponent grid
    // all it does is send the coordinates of where the user wants to fire
    private void configureOpponentGridActionListeners()
    {
        for (int row = 0; row < 10; ++row)
        {
            for (int col = 0; col < 10; ++col)
            {
                opponentGrid[row][col].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        // get the button that was clicked
                        MyJButton B = (MyJButton) e.getSource();
                        int r = B.getRow() - 1;
                        int c = B.getCol() - 1;

                        // if we are inside of client
                        if (client != null)
                        {
                            String S = "" + r + "" + c + "";
                            client.doSendMessage(S);
                        }
                    }
                });
            }
        }
    }

    // checks to see if that row and column is occupied. "used in configurePlayerGridActionListeners()"
    private boolean _isOccupied(int row, int column)
    {
        int r = row;
        int c = column;
        // see if its empty for a horizontal ship
        if (shipClicked.isHorizontal()) {
            for(int i = 0; i < shipClicked.getShipSize(); ++i) {
                if (playerGrid[r][c].getValue() != 0) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot place a ship on top of another!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                c++;
            }
        }
        // see if its empty for a vertical ship
        else {
            for(int i = 0; i < shipClicked.getShipSize(); ++i) {
                if (playerGrid[r][c].getValue() != 0) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot place a ship on top of another!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                r++;
            }
        }
        return false;
    }

    // this action listener places whatever ship was clicked on the grid
    // it only place a ship if a ship was clicked
    private void configurePlayerGridActionListeners()
    {
        for (int row = 0; row < 10; ++row)
        {
            for(int col = 0; col < 10; ++col)
            {
                playerGrid[row][col].addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        MyJButton B = (MyJButton) e.getSource();

                        if (shipClicked != null)
                        {
                            // get image and if its vertical or horizontal
                            Image[] shipImage = shipClicked.getShipImage();
                            int r = B.getRow() - 1;
                            int c = B.getCol() - 1;

                            // if the ship that was clicked is in the horizontal state
                            if (shipClicked.isHorizontal())
                            {
                                // if the ship is within bounds place it.
                                if (((c + shipImage.length)-1) < 10 && !_isOccupied(r, c))
                                {
                                    for (int i = 0; i < shipImage.length; ++i)
                                    {
                                        playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
                                        playerGrid[r][c].setValue(1); // occupied
                                        c++;
                                    }
                                    // rotateButton.setText("Horizontal");
                                    shipClicked.setVisible(false);  // cannot put that ship on the board anymore
                                }
                                // otherwise if it isn't, send a message that it is out of bounds
                                else if(((c + shipImage.length)-1) > 10)
                                {
                                    JOptionPane.showMessageDialog(null,
                                            "Cannot place ship out of bounds.",
                                            "Out of bounds!", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            // otherwise if its in the vertical state
                            else
                            {
                                // if the ship is within bounds place it.
                                if (((r + shipImage.length)-1) < 10 && !_isOccupied(r, c))
                                {
                                    for (int i = 0; i < shipImage.length; ++i)
                                    {
                                        playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
                                        playerGrid[r][c].setValue(1); // occupied
                                        r++;
                                    }
                                    rotateButton.setText("Horizontal");
                                    shipClicked.setVisible(false);  // cannot put that ship on the board anymore
                                }
                                // otherwise if it isn't, send a message that it is out of bounds
                                else if(((r + shipImage.length)-1) > 10)
                                {
                                    rotateButton.setText("Horizontal");
                                    JOptionPane.showMessageDialog(null,
                                            "Cannot place ship out of bounds.",
                                            "Out of bounds!", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        shipClicked = null;
                    }
                });
            }
        }
    }

    // this action listeners waits for the user to click one of the ships on the side
    // and it sets that ship to the shipClicked so that we know what ship they want to put on the grid
    private void configureShipsActionListeners()
    {
        for(int i = 0; i < ships.length; ++i)
        {
            ships[i].addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    shipClicked = (Ship) e.getSource();
                }
            });
        }
    }


    // this action listener if for when the user clicks rotate if they want to rotate a ship
    // it also displays an error message if they click it without clicking another ship
    private void configureRotateActionListener()
    {
        this.rotateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // if a ship was clicked
                if (shipClicked != null)
                {
                    if(rotateButton.getText().equals("Horizontal")){
                        rotateButton.setText("Vertical");
                    }
                    shipClicked.rotate();
                }
                // if a ship was not clicked display an error message
                else
                {
                    JOptionPane.showMessageDialog(null,
                            "Press Ship before rotate button!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}