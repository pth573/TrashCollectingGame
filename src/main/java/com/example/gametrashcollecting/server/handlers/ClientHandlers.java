package com.example.gametrashcollecting.server.handlers;

import com.example.gametrashcollecting.model.Client;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.statusReponse.ResponseStatus;
import com.example.gametrashcollecting.server.utils.ClientManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandlers implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
//    private List<Client> clientList = new ArrayList<Client>();
//    private Map<String, Client> clientMap = new HashMap<String, Client>();
//
    public ClientHandlers(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.ois = new ObjectInputStream(clientSocket.getInputStream());
        this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        while (clientSocket.isConnected()) {
            try
            {
                Client client = new Client(oos, ois);
//                clientList.add(client);
                Object obj = ois.readObject();
                Router router = new Router();
                if(obj instanceof Request){
                    Request requestClient = (Request) obj;
//                    System.out.println(requestClient.getStatus().toString());
//                    System.out.println("Request received from client" + requestClient.getStatus().toString());

                    ResponseStatus responseStatus = router.handle(requestClient, client, ClientManager.getInstance().getClientMap());
                    if(responseStatus != null){
//                        System.out.println("Response: " + responseStatus);
                        oos.writeObject(responseStatus);
                        oos.flush();
//                        System.out.println("Response send to client after handle");
                    }
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
