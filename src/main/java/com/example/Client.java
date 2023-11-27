package com.example;






import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);

            final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Receive and print the welcome message from the server
            System.out.println(in.readLine());

            // Get user's name
            System.out.print("Enter your name: ");
            String clientName = consoleInput.readLine();
            out.println(clientName);
            // Start a thread to handle incoming messages
            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            System.out.println(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveThread.start();

            // Read user input and send messages to the server
            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                out.println(userInput);
            }

            // Close resources
            receiveThread.interrupt(); // Stop receiving thread
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
