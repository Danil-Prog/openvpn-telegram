package org.openvpn.telegram;

import org.openvpn.telegram.entity.CommandLineArguments;
import org.openvpn.telegram.entity.ConnectionParams;
import org.openvpn.telegram.telnet.ITelnetClient;
import org.openvpn.telegram.telnet.TelnetClientDefault;
import org.openvpn.telegram.telnet.TelnetEventManager;
import org.openvpn.telegram.telnet.TelnetHandler;
import org.openvpn.telegram.utils.ArgumentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        logger.info("Starting OpenVPN monitoring...");

        final CommandLineArguments commandLineArguments = ArgumentUtils.parseConnectionParams(args);

        TelnetEventManager eventManager = new TelnetEventManager(new HashMap<>());

        var connectionParams = new ConnectionParams(commandLineArguments.ip(), commandLineArguments.port());

        ITelnetClient telnetClient = new TelnetClientDefault(connectionParams);
        telnetClient.connect();

        TelnetHandler telnetHandler = new TelnetHandler(eventManager, telnetClient);
        telnetHandler.handle();
    }
}
