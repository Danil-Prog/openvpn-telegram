package org.openvpn.telegram.telnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Responsible for the status of the connection to the telnet server,
 * event notifications for subscribers
 */
public class TelnetHandler {

    private final TelnetEventManager eventManager;
    private final ITelnetClient telnetClient;

    private static final Logger logger = LoggerFactory.getLogger(TelnetHandler.class);

    public TelnetHandler(TelnetEventManager eventManager, ITelnetClient telnetClient) {
        this.eventManager = eventManager;
        this.telnetClient = telnetClient;
    }

    public void handle() {
        logger.info("Telnet connection established.");

        while (telnetClient.isConnected()) {
            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.getInputStream()));

                while (reader.ready()) {
                    String line = reader.readLine();

                    if (line.contains("common_name")) {

                    }

//                    reader.lines().filter(line -> line.contains("common_name") || line.contains("untrusted_ip")).forEach(System.out::println);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public TelnetEventManager getEventManager() {
        return eventManager;
    }
}

// common_name
// untrusted_ip