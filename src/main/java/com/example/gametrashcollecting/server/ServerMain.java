package com.example.gametrashcollecting.server;
import com.example.gametrashcollecting.server.handlers.ClientHandlers;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
public class ServerMain {
    private static final int PORT = 8080;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
                ClientHandlers clientHandlers = new ClientHandlers(clientSocket);
                Thread clientThread = new Thread(clientHandlers);
                clientThread.start();
        }
    }
}
