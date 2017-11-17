/**
 * @authors Edgar Martinez-Ayala and Alex Guler
 * Server class - Handles all server related issues with the class
 *
 *
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
    // Network Items
    ServerSocket serverSocket;
    private int PORT;
    private String machineAddress;
    private boolean running = false;

    // set up Connection
    public Server() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            machineAddress = addr.getHostAddress();
        } catch (UnknownHostException e) {
            machineAddress = "127.0.0.1";
        }

        //code to test server
        System.out.println("Opening port...\n");
        System.out.println(machineAddress);

        try {
            serverSocket = new ServerSocket(0);
            PORT = serverSocket.getLocalPort();

        } catch (IOException ioex) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
        running = true;

    } // end CountDown constructor

    public int getPORT(){
        return this.PORT;
    }
    public String getAddress(){
        return this.machineAddress;
    }

    public void handleClient()
    {
        Socket link = null; //Step 1
        try {
            link = serverSocket.accept(); //Step 2
            //Step 3
            Scanner input = new Scanner(link.getInputStream());
            PrintWriter output = new PrintWriter(link.getOutputStream(), true);
            int firstInt = input.nextInt();
            int secondInt = input.nextInt();
            int answer, firstInt2, secondInt2;

            while (firstInt != -1 || secondInt != -1)
            {
                answer = firstInt + secondInt;

                output.println(answer); //Server returns the sum here 4
                Scanner userEntry = new Scanner(System.in);
                System.out.print("Please input the first number: ");
                firstInt2 = userEntry.nextInt();
                System.out.print("Please input the second number: ");
                secondInt2 = userEntry.nextInt();
                output.println(firstInt2);
                output.println(secondInt2);

                firstInt = input.nextInt();
                secondInt = input.nextInt();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                System.out.println("Closing connection...");
                link.close();
            }
            catch (IOException ie)
            {
                System.out.println("Unable to close connection");
                System.exit(1);
            }
        }
    }
} // end class EchoServer3


