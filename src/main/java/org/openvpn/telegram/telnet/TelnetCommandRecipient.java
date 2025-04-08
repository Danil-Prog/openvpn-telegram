package org.openvpn.telegram.telnet;

import jakarta.annotation.PostConstruct;
import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.openvpn.telegram.telnet.parser.TelnetMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives and processes telnet messages, generates events
 */
@Component
public class TelnetCommandRecipient {

    private final TelnetEventManager eventManager;
    private final ITelnetClient telnetClient;
    private final List<TelnetMessageParser<TelnetEvent>> telnetMessageParsers;

    private static final Logger logger = LoggerFactory.getLogger(TelnetCommandRecipient.class);

    @Autowired
    public TelnetCommandRecipient(
            @Qualifier("telnetClientDefault") ITelnetClient telnetClient,
            TelnetEventManager eventManager,
            List<TelnetMessageParser<TelnetEvent>> telnetMessageParsers) {
        this.telnetMessageParsers = telnetMessageParsers;
        this.eventManager = eventManager;
        this.telnetClient = telnetClient;
    }

    @PostConstruct
    public void init() {
        new Thread(this::listenToTelnet).start();
    }

    public void listenToTelnet() {
        logger.info("Telnet connection established.");

        while (telnetClient.isConnected()) {
            try {

                BufferedReader reader = telnetClient.getStreamReader();
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Received: " + line);
                    lines.add(line);
                }

                telnetMessageParsers.forEach(parser -> {
                            TelnetEvent generatedEvent = parser.parse(lines);

                            if (generatedEvent != null) {
                                eventManager.fire(generatedEvent);
                            }
                        }
                );

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public TelnetEventManager getEventManager() {
        return eventManager;
    }
}
