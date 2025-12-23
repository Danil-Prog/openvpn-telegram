package org.openvpn.telegram.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.openvpn.telegram.dto.ClientDto;
import org.openvpn.telegram.entity.Client;
import org.openvpn.telegram.entity.Connection;
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
    private final ConnectionService connectionService;

    private final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    @Autowired
    public MonitoringService(ClientService clientService, ConnectionService connectionService) {
        this.clientService = clientService;
        this.connectionService = connectionService;
    }

    public synchronized void addClientConnection(ClientConnectedEvent event) {
        boolean clientConnectedExist = connectionService.getConnectionByUsername(event.username()) != null;

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

        connectionService.addConnection(connection);
    }

    public synchronized void updateClientConnections(StatusCommandEvent event) {
        connectionService.getConnections().forEach(connection -> {

            // Update connection state, if client contain with list connections
            List<String> allClientUsernames = event.clientConnections().stream().map(ClientDto::commonName).toList();

            if (allClientUsernames.contains(connection.getUsername())) {
                connection.setTotalBytesReceived(connection.getTotalBytesReceived() + 1L);
                connection.setTotalBytesSent(connection.getTotalBytesSent() + 1L);
            } else {
                this.closeClientSessionByUsername(connection.getUsername());
            }
        });
    }

    public synchronized void clientDisconnected(ClientDisconnectedEvent event) throws IllegalArgumentException {
        Connection connection = connectionService.getConnectionByUsername(event.username());

        if (connection == null) {
            logger.info("Client connection not found: username[{}], ip[{}]", event.username(), event.ip());
            return;
        }

        // If connection closed - created and saved session
        this.createClientSession(connection);
    }

    private void createClientSession(Connection connection) throws IllegalArgumentException {
        Session session = new Session();

        Date disconnectedAt = Date.from(Instant.now());
        Date connectedAt = Date.from(connection.getConnectedAt());
        Long sessionDurationSeconds = disconnectedAt.getTime() - connectedAt.getTime();

        session.setSessionDurationSeconds(sessionDurationSeconds);

        session.setTimeDisconnected(disconnectedAt);
        session.setTimeConnected(connectedAt);
        session.setBytesReceived(1L);
        session.setBytesSent(1L);

        Client client = clientService.getClientByUsername(connection.getUsername()).orElse(null);

        if (client == null) {
            logger.info("Client did not exist, adding account with name[{}]", connection.getUsername());

            client = new Client();
            client.setUsername(connection.getUsername());
            client.setEnabled(true);
            client.setTraffic(connection.getTotalBytesReceived());
        }

        client.addSession(session);
        clientService.createOrUpdateClient(client);
    }

    private void closeClientSessionByUsername(String username) {
        Connection connection = connectionService.getConnectionByUsername(username);

        if (connection == null) {
            logger.info("Client connection not found: username[{}]", username);
            return;
        }

        Session session = new Session();
        session.setTimeConnected(Date.from(connection.getConnectedAt()));
        session.setTimeDisconnected(Date.from(Instant.now()));

        Optional<Client> clientOptional = clientService.getClientByUsername(username);

        Client client;

        if (clientOptional.isPresent()) {
            client = clientOptional.get();
            client.setTraffic(client.getTraffic() + connection.getTotalBytesReceived() + connection.getTotalBytesSent());

            client.getSessions().add(session);
        } else {
            client = new Client();
            client.setEnabled(true);
            client.setUsername(username);
            client.setIp(connection.getIp());

            client.addSession(session);
        }

        clientService.createOrUpdateClient(client);

        logger.info("Update client session: username[{}], ip[{}]", username, connection.getIp());
    }
}
