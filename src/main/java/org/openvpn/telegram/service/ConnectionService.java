package org.openvpn.telegram.service;

import java.util.ArrayList;
import java.util.List;
import org.openvpn.telegram.entity.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

    /**
     * All clients connections is now
     */
    private final List<Connection> connections;

    private final Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    public ConnectionService() {
        this.connections = new ArrayList<>();
    }

    public List<Connection> getConnections() {
        return this.connections;
    }

    public boolean connectionsIsEmpty() {
        return this.connections.isEmpty();
    }

    public synchronized void addConnection(Connection connection) {
        var isExist = getConnectionByUsername(connection.getUsername());
        if (isExist != null) {
            logger.warn("Connection with username {} already exists.", connection.getUsername());
        } else {
            connections.add(connection);
        }
    }

    public synchronized void deleteConnectionByUsername(String username) {
        var connection = getConnectionByUsername(username);
        if (connection != null) {
            logger.info("Remove connection from all connections.");
            connections.remove(connection);
        } else {
            logger.warn("Connection not found for username: {}.", username);
        }
    }

    public synchronized Connection getConnectionByUsername(String username) {
        return connections.stream()
                .filter(connection -> connection.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}
