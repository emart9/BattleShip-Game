import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class userInteraction
{
    private MyJButton[][] playerGrid;
    private MyJButton[][] opponentGrid;
    private JButton rotateButton;
    private Ship[] ships;
    private Ship shipClicked;

    // user interaction class constructor
    public userInteraction(MyJButton[][] player, MyJButton[][] opponent, Ship[] allShips, JButton rotate)
    {
        playerGrid = player;
        opponentGrid = opponent;
        rotateButton = rotate;
        ships = allShips;
        shipClicked = null;
        configureShipsActionListeners();
        configureRotateActionListener();
        configurePlayerGridActionListeners();
    }

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
                                if (((c + shipImage.length)-1) < 10)
                                {
                                    for (int i = 0; i < shipImage.length; ++i)
                                    {
                                        playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
                                        c++;
                                    }
                                    shipClicked.setVisible(false);  // cannot put that ship on the board anymore
                                }
                                // otherwise if it isn't, send a message that it is out of bounds
                                else
                                {
                                    JOptionPane.showMessageDialog(null,
                                            "Cannot place ship out of bounds.",
                                            "Out of bounds!", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            // otherwise if its in the vertical state
                            else
                            {
                                System.out.println("VERTICAL: Gonna put this image from Row: " + r + " to " + ((r + shipImage.length)-1) + " in Column: " + r);

                                // if the ship is within bounds place it.
                                if (((r + shipImage.length)-1) < 10)
                                {
                                    for (int i = 0; i < shipImage.length; ++i)
                                    {
                                        playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
                                        r++;
                                    }
                                    shipClicked.setVisible(false);  // cannot put that ship on the board anymore
                                }
                                // otherwise if it isn't, send a message that it is out of bounds
                                else
                                {
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

    private void configureRotateActionListener()
    {
        this.rotateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (shipClicked != null)
                {
                    shipClicked.rotate();
                }
                else
                {
                    System.out.println("Clicked the rotate button but didn't even press a ship!");
                }
            }
        });
    }


}

