package org.openvpn.telegram.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.openvpn.telegram.dto.ClientDto;
import org.openvpn.telegram.entity.Client;
import org.openvpn.telegram.entity.Session;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.openvpn.telegram.telnet.events.StatusCommandEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

    private final ClientService clientService;

    private final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    /**
     * All clients connections is now
     */
    private static final List<Connection> connections = new ArrayList<>();

    @Autowired
    public MonitoringService(ClientService clientService) {
        this.clientService = clientService;
    }

    public synchronized void addClientConnection(ClientConnectedEvent event) {
        boolean clientConnectedExist = findConnectionByUsername(event.username()) == null;

        if (!clientConnectedExist) {
            logger.info("Client connection already exist: username[{}], ip[{}]", event.username(), event.ip());
            return;
        }

        Connection connection = new Connection(
                event.username(),
                event.ip(),
                event.timeConnected(),
                null,
                0L,
                0L
        );

        connections.add(connection);
    }

    public synchronized void updateClientConnections(StatusCommandEvent event) {
        connections.forEach(connection -> {

            // Update connection state, if client contain with list connections
            List<String> allClientUsernames = event.clientConnections().stream().map(ClientDto::commonName).toList();

            if (allClientUsernames.contains(connection.username)) {
                connection.bytesReceived += connection.bytesReceived + 1L;
                connection.bytesSent += connection.bytesSent + 1L;
            } else {
                this.closeClientSessionByUsername(connection.username);
            }
        });
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

    public boolean connectionsExist() {
        return !connections.isEmpty();
    }

    private void createClientSession(Connection connection) {
        Session session = new Session();

        Date disconnectedAt = Date.from(Instant.now());
        Date connectedAt = Date.from(connection.connectedAt);
        Long sessionDurationSeconds = disconnectedAt.getTime() - connectedAt.getTime();

        session.setSessionDurationSeconds(sessionDurationSeconds);

        session.setTimeDisconnected(disconnectedAt);
        session.setTimeConnected(connectedAt);
        session.setBytesReceived(1L);
        session.setBytesSent(1L);
    }

    private void closeClientSessionByUsername(String username) {
        Connection connection = findConnectionByUsername(username);

        if (connection == null) {
            logger.info("Client connection not found: username[{}]", username);
            return;
        }

        Session session = new Session();
        session.setTimeConnected(Date.from(connection.connectedAt));
        session.setTimeDisconnected(Date.from(connection.disconnectedAt));

        Optional<Client> clientOptional = clientService.getClientByUsername(username);
        Client client;

        if (clientOptional.isPresent()) {
            client = clientOptional.get();
            client.setTraffic(client.getTraffic() + connection.bytesReceived + connection.bytesSent);

            client.getSessions().add(session);
        } else {
            client = new Client();
            client.setEnabled(true);
            client.setUsername(username);
            client.setIp(connection.ip);

            client.addSession(session);
        }

        clientService.createOrUpdateClient(client);

        logger.info("Update client session: username[{}], ip[{}]", username, connection.ip);
    }

    private Connection findConnectionByUsername(String username) {
        return connections.stream()
                .filter(connection -> connection.username.equals(username))
                .findFirst().orElse(null);
    }

    private static class Connection {

        private final String username;
        private final String ip;
        private final Instant connectedAt;
        private final Instant disconnectedAt;
        private Long bytesReceived;
        private Long bytesSent;

        private Connection(
                String username,
                String ip,
                Instant connectedAt,
                Instant disconnectedAt,
                Long bytesReceived,
                Long bytesSent
        ) {
            this.username = username;
            this.ip = ip;
            this.connectedAt = connectedAt;
            this.disconnectedAt = disconnectedAt;
            this.bytesReceived = bytesReceived;
            this.bytesSent = bytesSent;
        }
    }
}
