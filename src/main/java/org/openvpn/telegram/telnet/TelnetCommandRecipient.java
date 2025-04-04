package org.openvpn.telegram.telnet;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Receives and processes telnet messages, generates events
 */
@Component
public class TelnetCommandRecipient {

    private final TelnetEventManager eventManager;
    private final ITelnetClient telnetClient;

    private static final Logger logger = LoggerFactory.getLogger(TelnetCommandRecipient.class);

    @Autowired
    public TelnetCommandRecipient(@Qualifier("telnetClientDefault") ITelnetClient telnetClient) {
        this.eventManager = new TelnetEventManager();
        this.telnetClient = telnetClient;
    }

    @PostConstruct
    public void init() {
        logger.info("Telnet connection established.");

        while (telnetClient.isConnected()) {
            try {

                BufferedReader reader = telnetClient.getStreamReader();

                while (reader.ready()) {
                    String line = reader.readLine();
                    System.out.println(line);
                    if (line.contains("common_name")) {
                        System.out.print("Connected client: " + line.replace(">CLIENT:ENV,common_name=", ""));
                    }

                    if (line.contains("untrusted_ip")) {
                        System.out.println(". Untrusted IP: " + line.replace(">CLIENT:ENV,untrusted_ip=", ""));
                    }

                    if (line.contains("SUCCESS: pid=")) {
                        System.out.println(". PID= " + line.replace("SUCCESS: pid=", ""));
                    }

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
