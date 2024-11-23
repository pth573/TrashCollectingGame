package com.example.gametrashcollecting.server.utils;

import com.example.gametrashcollecting.model.Client;
import java.util.HashMap;
import java.util.Map;

public class ClientManager {
    private static ClientManager instance;
    private Map<String, Client> clientMap;

    private ClientManager() {
        clientMap = new HashMap<>();
    }

    // Singleton method to get the instance
    public static synchronized ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    public Map<String, Client> getClientMap() {
        return clientMap;
    }

    public void addClient(String username, Client client) {
        clientMap.put(username, client);
    }

    public Client getClient(String username) {
        return clientMap.get(username);
    }

    public void removeClient(String username) {
        clientMap.remove(username);
    }
}
