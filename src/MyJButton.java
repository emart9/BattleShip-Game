/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * MyJButton class - Class that contains the games Button
 * 					 object that holds all button values along
 * 					 with the position that the button currently
 * 					 holds on the game board.
 */

import javax.swing.*;

public class MyJButton extends JButton {
    private int row;        // row coordinate
    private int col;        // col coordinate
    private int value;      // int value

    public MyJButton(String text, int row, int col, int value){
        super(text);
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow(){
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public int getValue(){
        return this.value;
    }
}