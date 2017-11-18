import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class Ship extends JButton
{
    private int size;
    private String name;
    private Image[] horizontalShip;
    private Image[] verticalShip;
    private boolean horizontal;
    final private String[] allHorizontalImages = {"batt1.gif", "batt2.gif", "batt3.gif", "batt4.gif", "batt5.gif"};
    final private String[] allVerticalImages = {"batt6.gif", "batt7.gif", "batt8.gif", "batt9.gif", "batt10.gif"};

    public Ship(ShipType type)
    {
        super(type.getName());
        name = type.getName();
        size = type.getSize();
        horizontal = true;

        horizontalShip = new Image[size];
        verticalShip = new Image[size];

        for (int i = 0; i < (size - 1); ++i)
        {
            horizontalShip[i] = setImage(allHorizontalImages[i]);
        }
        // set the last image for the horizontal ship to be the front
        horizontalShip[size - 1] = setImage(allHorizontalImages[4]);

        for (int i = 0; i < (size - 1); ++i)
        {
            verticalShip[i] = setImage(allVerticalImages[i]);
        }
        // set the last image for the horizontal ship to be the front
        verticalShip[size - 1] = setImage(allVerticalImages[4]);

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

    public int getShipSize()
    {
        return size;
    }

    public void rotate()
    {
        if (horizontal)
            horizontal = false;
        else
            horizontal = true;
    }

    public boolean isHorizontal()
    {
        return horizontal;
    }

    public Image[] getShipImage()
    {
        if (horizontal)
            return horizontalShip;
        else
            return verticalShip;
    }
}
