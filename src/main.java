/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * Class:   CS 342
 * Program #4: Battleships Game
 * Description: Build and plays a Battleship game
 */

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        //code to test server
//        Server application = new Server();
//        application.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
//        Client application2 = new Client();
//        application2.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        GUI gui = new GUI();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

