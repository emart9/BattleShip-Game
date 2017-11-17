/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * Class:   CS 342
 * Program #4: Battleships Game
 * Description: Build and plays a Battleship game
 */

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class main {
//    private static ServerSocket serverSocket;
//    private static final int PORT = 1234;
//    private static int i;
//
//    private static InetAddress host;
//
    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
//        i = scan.nextInt();
//
//        if (i == 1) {
//
//            String machineAddress = null;
//            try
//            {
//                InetAddress addr = InetAddress.getLocalHost();
//                machineAddress = addr.getHostAddress();
//            }
//            catch (UnknownHostException e)
//            {
//                machineAddress = "127.0.0.1";
//            }
//
//            //code to test server
//            System.out.println("Opening port...\n");
//            System.out.println(machineAddress);
//
//            try {
//                serverSocket = new ServerSocket(PORT);
//            } catch (IOException ioex) {
//                System.out.println("Unable to attach to port!");
//                System.exit(1);
//            }
//            handleClient();
//        } else {
//            try {
//                host = InetAddress.getLocalHost();
//            } catch (UnknownHostException uhEx) {
//                System.out.println("Host ID not found!");
//                System.exit(1);
//            }
//            accessServer();
//        }
//
//
        GUI gui = new GUI();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
//
//    private static void handleClient()
//    {
//        Socket link = null; //Step 2
//        try {
//            link = serverSocket.accept(); //Step 2
//            //Step 3
//            Scanner input = new Scanner(link.getInputStream());
//            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
//            int firstInt = input.nextInt();
//            int secondInt = input.nextInt();
//            int answer, firstInt2, secondInt2;
//
//            while (firstInt != -1 || secondInt != -1)
//            {
//                answer = firstInt + secondInt;
//
//                output.println(answer); //Server returns the sum here 4  //TODO Write method to check if hit or mis
//                Scanner userEntry = new Scanner(System.in);
//                System.out.print("Please input the first number: ");
//                firstInt2 = userEntry.nextInt();
//                System.out.print("Please input the second number: ");
//                secondInt2 = userEntry.nextInt();
//                output.println(firstInt2);
//                output.println(secondInt2);
//
//                firstInt = input.nextInt();
//                secondInt = input.nextInt();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                System.out.println("Closing connection...");
//                link.close();
//            }
//            catch (IOException ie)
//            {
//                System.out.println("Unable to close connection");
//                System.exit(1);
//            }
//        }
//    }
//
//    private static void accessServer() {
//        Socket link = null;    //Step 1
//        Scanner userEntry10 = new Scanner(System.in);
//        System.out.print("Please input the address: ");
//        String host2 = userEntry10.nextLine();
//        try {
//            link = new Socket(host2, PORT); //Step 1
//            //Step 2
//            Scanner input = new Scanner(link.getInputStream());
//            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
//
//            //Set up stream for keyboard entry
//            Scanner userEntry = new Scanner(System.in);
//
//            int firstInt, secondInt, answer, firstInt2, secondInt2;
//            do {
//                System.out.print("Please input the first number: ");
//                firstInt = userEntry.nextInt();
//                System.out.print("Please input the second number: ");
//                secondInt = userEntry.nextInt();
//
//                //send the numbers
//                output.println(firstInt);
//                output.println(secondInt);
//                answer = input.nextInt(); //getting the answer from the server
//                System.out.println("\nSERVER> " + answer);
//                firstInt2 = input.nextInt();
//                secondInt2 = input.nextInt();
//                System.out.println("\nSERVER> " + answer + " " + firstInt2 + " " + secondInt2);
//            } while (firstInt != 0 || secondInt != 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (NoSuchElementException ne){   //This exception may be raised when the server closes connection
//            System.out.println("Connection closed");
//        }
//        finally {
//            try {
//                System.out.println("\n* Closing connection… *");
//                link.close(); //Step 4.
//            } catch (IOException ioEx) {
//                System.out.println("Unable to disconnect!");
//                System.exit(1);
//            }
//        }
//    }
//}
//
//
//
//
