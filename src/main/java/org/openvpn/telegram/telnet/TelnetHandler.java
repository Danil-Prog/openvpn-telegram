package org.openvpn.telegram.telnet;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Responsible for the status of the connection to the telnet server,
 * event notifications for subscribers
 */
@Component
public class TelnetHandler {

    private final TelnetEventManager eventManager;
    private final ITelnetClient telnetClient;

    private static final Logger logger = LoggerFactory.getLogger(TelnetHandler.class);

    @Autowired
    public TelnetHandler(@Qualifier("telnetClientDefault") ITelnetClient telnetClient) {
        this.eventManager = new TelnetEventManager();
        this.telnetClient = telnetClient;
    }

    @PostConstruct
    public void init() {
        logger.info("Telnet connection established.");

        while (telnetClient.isConnected()) {
            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.getInputStream()));

                while (reader.ready()) {
                    String line = reader.readLine();

                    if (line.contains("common_name")) {
                        System.out.print("Connected client: " + line.replace(">CLIENT:ENV,common_name=", ""));
                    }

                    if (line.contains("untrusted_ip")) {
                        System.out.println(". Untrusted IP: " + line.replace(">CLIENT:ENV,untrusted_ip=", ""));
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