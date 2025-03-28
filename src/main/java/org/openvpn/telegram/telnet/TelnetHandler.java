package org.openvpn.telegram.telnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public synchronized void handle() {
        while (telnetClient.isConnected()) {

        }
    }

    public TelnetEventManager getEventManager() {
        return eventManager;
    }
}
