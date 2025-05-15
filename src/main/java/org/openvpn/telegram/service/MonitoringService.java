package org.openvpn.telegram.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.openvpn.telegram.entity.Session;
import org.openvpn.telegram.repository.ClientRepository;
import org.openvpn.telegram.repository.SessionRepository;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

    private final ClientRepository clientRepository;
    private final SessionRepository sessionRepository;

    private final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    /**
     * All clients connections is now
     */
    private static final List<Connection> connections = new ArrayList<>();

    @Autowired
    public MonitoringService(
            ClientRepository clientRepository,
            SessionRepository sessionRepository
    ) {
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
    }

    public synchronized void clientConnected(ClientConnectedEvent event) {
        boolean clientConnectedExist = findConnectionByUsername(event.username()) == null;

        if (clientConnectedExist) {
            logger.info("Client connection already exist: username[{}], ip[{}]", event.username(), event.ip());
            return;
        }

        Connection connection = new Connection(event.username(), event.ip(), event.timeConnected());
        connections.add(connection);
    }

    public synchronized void clientDisconnected(ClientDisconnectedEvent event) {
        Connection connection = findConnectionByUsername(event.username());

        if (connection == null) {
            logger.info("Client connection not found: username[{}], ip[{}]", event.username(), event.ip());
            return;
        }

        // If connection closed - created and saved session
        this.createClientSession(connection);
    }

    private void createClientSession(Connection connection) {
        Session session = new Session();
        session.setTimeConnected(connection.getConnectedAt());
    }

    private Connection findConnectionByUsername(String username) {
        return connections.stream()
                .filter(connection -> connection.getUsername().equals(username))
                .findFirst().orElse(null);
    }

    private static class Connection {

        private final String username;
        private final String ip;
        private final Instant connectedAt;

        private Connection(String username, String ip, Instant connectedAt) {
            this.username = username;
            this.ip = ip;
            this.connectedAt = connectedAt;
        }

        public String getUsername() {
            return username;
        }

        public String getIp() {
            return ip;
        }

        public Instant getConnectedAt() {
            return connectedAt;
        }
    }
}
