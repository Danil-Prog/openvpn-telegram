package org.openvpn.telegram.telnet;

import org.apache.commons.net.telnet.TelnetClient;
import org.openvpn.telegram.entity.Connection;
import org.openvpn.telegram.entity.ConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

public class TelnetClientDefault implements ITelnetClient {

    private final Long RECONNECT_TIME = 10000L;

    private static final Logger logger = LoggerFactory.getLogger(TelnetClientDefault.class);

    private final ConnectionParams connectionParams;

    private final TelnetClient telnetClient;

    public TelnetClientDefault(ConnectionParams connectionParams) {
        this.connectionParams = connectionParams;
        telnetClient = new TelnetClient();
    }

    @Override
    public void connect() {
        try {
//            configure();
            telnetClient.connect(connectionParams.ip(), connectionParams.port());
        } catch (IOException e) {
            logger.error("Failed to connect to telnet client, reason: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        logger.info("Successful connected to {}:{}", connectionParams.ip(), connectionParams.port());
    }

    @Override
    public void disconnect() {
        logger.info("Trying disconnecting telnet server..");
        try {
            telnetClient.disconnect();
        } catch (IOException e) {
            logger.error("Ooops! An error occurred while disconnecting telnet server", e);
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream() {
        return telnetClient.getInputStream();
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Boolean isConnected() {
        boolean connected = telnetClient.isAvailable();

        if (connected) {
            return true;
        } else {
            int attempts = 0;

            while (attempts < 3) {
                try {

                    // Attempt to reinstall connect to server
                    reconnect();

                    // Check if connect restored
                    if (telnetClient.isConnected()) {
                        logger.info("Successful reconnected to {}:{}", connectionParams.ip(), connectionParams.port());
                        return true;
                    }

                    Thread.sleep(RECONNECT_TIME);

                } catch (IOException e) {
                    logger.error("Attempt reconnected to telnet server num {} failed, reason: {}", attempts, e.getMessage());
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while waiting for telnet server to reconnect", e);
                    throw new RuntimeException(e);
                }

                attempts++;
            }
        }

        return false;
    }

    private void configure() throws SocketException {
        telnetClient.setKeepAlive(true);
        telnetClient.setConnectTimeout(10000);
    }

    private void reconnect() throws IOException {
        this.telnetClient.connect(connectionParams.ip(), connectionParams.port());
    }
}
