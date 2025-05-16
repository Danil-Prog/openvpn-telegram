package org.openvpn.telegram.telnet;

import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.commons.net.telnet.TelnetClient;
import org.openvpn.telegram.config.properties.TelnetConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelnetClientDefault implements ITelnetClient {

    private final TelnetClient telnetClient;
    private final TelnetConnectionProperties telnetConnectionProperties;

    private static final Long RECONNECT_TIME = 10000L;
    private static final Logger logger = LoggerFactory.getLogger(TelnetClientDefault.class);

    @Autowired
    public TelnetClientDefault(
            TelnetConnectionProperties telnetConnectionProperties,
            TelnetClient telnetClient
    ) {
        this.telnetConnectionProperties = telnetConnectionProperties;
        this.telnetClient = telnetClient;
        connect();
    }

    @Override
    public void connect() {
        telnetClient.setConnectTimeout(3000);
        this.reconnect();

        logger.info(
                "Successful connected to {}:{}",
                telnetConnectionProperties.getHost(),
                telnetConnectionProperties.getPort()
        );
    }

    @PreDestroy
    @Override
    public void disconnect() {
        logger.info("Disconnect from telnet server..");
        try {
            telnetClient.disconnect();
        } catch (IOException e) {
            logger.error("Ooops! An error occurred while disconnecting telnet server", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public BufferedReader getStreamReader() {
        return new BufferedReader(new InputStreamReader(telnetClient.getInputStream()));
    }

    @Override
    public OutputStream getStreamWriter() {
        return telnetClient.getOutputStream();
    }

    @Override
    public Boolean isConnected() {
        boolean connected = telnetClient.isConnected();

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

                    logger.info("Attempt #{} failed.. retry", attempts);
                    Thread.sleep(RECONNECT_TIME);

                } catch (InterruptedException e) {
                    logger.error("Interrupted while waiting for telnet server to reconnect", e);
                    throw new RuntimeException(e);
                }

                attempts++;
            }

            System.exit(1);

            throw new RuntimeException("Failed connect to telnet server");
        }
    }

    private void reconnect() {
        try {
            this.telnetClient.connect(telnetConnectionProperties.getHost(), telnetConnectionProperties.getPort());
        } catch (IOException e) {
            logger.error("Failed connect to telnet server, reason: {}", e.getMessage());
        }
    }
}
