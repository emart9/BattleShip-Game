import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum ShipType
{
    AircraftCarrier(5, "Aircraft Carrier"),
    Battleship(4, "Battleship"),
    Destroyer(3, "Destroyer"),
    Submarine(3, "Submarine"),
    PatrolBoat(2, "Patrol Boat");

    private int size;
    private String name;

    private ShipType(int s, String n)
    {
        size = s;
        name = n;
    }

    public int getSize()
    {
        return size;
    }

    public String getName()
    {
        return name;
    }
}
