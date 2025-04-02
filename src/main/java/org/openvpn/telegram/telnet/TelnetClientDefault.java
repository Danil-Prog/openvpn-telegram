package org.openvpn.telegram.telnet;

import org.apache.commons.net.telnet.TelnetClient;
import org.openvpn.telegram.configuration.properties.TelnetConnectionProperties;
import org.openvpn.telegram.entity.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

@Component
public class TelnetClientDefault implements ITelnetClient {

    private final Long RECONNECT_TIME = 10000L;

    private static final Logger logger = LoggerFactory.getLogger(TelnetClientDefault.class);

    private final TelnetConnectionProperties telnetConnectionProperties;

    private final TelnetClient telnetClient;

    @Autowired
    public TelnetClientDefault(TelnetConnectionProperties telnetConnectionProperties, TelnetClient telnetClient) {
        this.telnetConnectionProperties = telnetConnectionProperties;
        this.telnetClient = telnetClient;
    }

    @Override
    public void connect() {
        try {
//            configure();
            telnetClient.connect(telnetConnectionProperties.getHost(), telnetConnectionProperties.getPort());
        } catch (IOException e) {
            logger.error("Failed to connect to telnet client, reason: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        logger.info("Successful connected to {}:{}", telnetConnectionProperties.getHost(), telnetConnectionProperties.getPort());
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
                        logger.info("Successful reconnected to {}:{}", telnetConnectionProperties.getHost(), telnetConnectionProperties.getPort());
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
        this.telnetClient.connect(telnetConnectionProperties.getHost(), telnetConnectionProperties.getPort());
    }
}
