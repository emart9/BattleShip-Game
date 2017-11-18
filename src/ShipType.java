/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * ShipType enum class - a custom data type.
 * It is used for when you want to create a ship object, all you need to do
 * is construct it with whatever ship type you want
 * example Ship aircraftCarrier = new Ship(AircraftCarrier);
 */


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public enum ShipType
{
    // name of the type, it includes size, and the name (string)
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
