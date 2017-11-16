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

                            /*
                            try { //adds image to grid buttons
                            Image img1 = ImageIO.read(getClass().getResource("batt100.gif"));
                            fired[row][col].setIcon(new ImageIcon(img1));
                            fired[row][col].setHorizontalTextPosition(SwingConstants.CENTER);
                            shipPositions[row][col].setIcon(new ImageIcon(img1));
                            shipPositions[row][col].setHorizontalTextPosition(SwingConstants.CENTER);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                             */


                            // get image and if its vertical or horizontal
                            Image[] shipImage = shipClicked.getShipImage();
                            int r = B.getRow() - 1;
                            int c = B.getCol() - 1;

                            // TODO: out of bounds check
                            if (shipClicked.isHorizontal())
                            {
                                for (int i = 0; i < shipImage.length; ++i)
                                {
                                    playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
                                    c++;
                                }
                                // System.out.println("HORIZONTAL: Gonna put this image from Column: " + c + " to " + (c + shipImage.length) + " in Row: " + r);
                            }
                            // TODO: out of bounds check
                            else
                            {
                                for (int i = 0; i < shipImage.length; ++i)
                                {
                                    playerGrid[r][c].setIcon(new ImageIcon(shipImage[i]));
                                    r++;
                                }
                                // System.out.println("VERTICAL: Gonna put this image from Row: " + r + " to " + (r + shipImage.length) + " in Column: " + r);
                            }
                        }
                        /*
                         // TODO DELETE THIS LATER
                        if (shipClicked != null)
                        {
                            System.out.println(shipClicked.getShipName() + " was clicked before this button!");
                            System.out.println("Row: " + B.getRow() + " | Column: " + B.getCol());
                        }
                        else
                        {
                            System.out.println("No button was clicked before this button");
                            System.out.println("Row: " + B.getRow() + " | Column: " + B.getCol());
                        }
                        */


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

