package org.openvpn.telegram.telnet;

import org.apache.commons.net.telnet.TelnetClient;
import org.openvpn.telegram.entity.Connection;
import org.openvpn.telegram.entity.ConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketException;

public class TelnetClientDefault implements ITelnetClient {

    private final ConnectionParams connectionParams;

    private static final Logger logger = LoggerFactory.getLogger(TelnetClientDefault.class);

    private org.apache.commons.net.telnet.TelnetClient telnetClient;

    public TelnetClientDefault(ConnectionParams connectionParams) {
        this.connectionParams = connectionParams;
    }

    @Override
    public void connect(ConnectionParams connectionParams) throws Exception {
        if (telnetClient == null) {
            telnetClient = new TelnetClient();
        }

        telnetClientConfiguration();

        telnetClient.connect(connectionParams.ip(), connectionParams.port());
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

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Boolean isConnected() {
        return telnetClient != null && telnetClient.isConnected() && telnetClient.isAvailable();
    }

    private void telnetClientConfiguration() throws SocketException {
        telnetClient.setKeepAlive(true);
        telnetClient.setConnectTimeout(10000);
    }
}
