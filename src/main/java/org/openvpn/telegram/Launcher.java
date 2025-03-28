package org.openvpn.telegram;

import org.openvpn.telegram.entity.ConnectionParams;
import org.openvpn.telegram.telnet.ITelnetClient;
import org.openvpn.telegram.telnet.TelnetClientDefault;
import org.openvpn.telegram.utils.ArgumentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        logger.info("Starting OpenVPN monitoring...");

        final ConnectionParams connectionParams = ArgumentUtils.parseConnectionParams(args);
        ITelnetClient telnetClient = new TelnetClientDefault(connectionParams);
    }
}
