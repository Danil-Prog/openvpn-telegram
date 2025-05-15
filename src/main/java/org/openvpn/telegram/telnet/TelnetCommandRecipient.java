package org.openvpn.telegram.telnet;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.openvpn.telegram.telnet.parser.TelnetMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Receives and processes telnet messages, generates events
 */
@Component
public class TelnetCommandRecipient {

    private final ICommandSender commandSender;
    private final TelnetEventManager eventManager;
    private final ITelnetClient telnetClient;
    private final List<TelnetMessageParser<?>> telnetMessageParsers;
    private final List<String> buffer = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(TelnetCommandRecipient.class);

    @Autowired
    public TelnetCommandRecipient(
            @Qualifier("telnetClientDefault") ITelnetClient telnetClient,
            @Qualifier("telnetCommandSender") ICommandSender commandSender,
            TelnetEventManager eventManager,
            List<TelnetMessageParser<?>> telnetMessageParsers
    ) {
        this.telnetClient = telnetClient;
        this.telnetMessageParsers = telnetMessageParsers;
        this.commandSender = commandSender;
        this.eventManager = eventManager;

        new DefaultTelnetTerminalConfiguration().configure();
    }

    @PostConstruct
    public void init() {
        new Thread(this::handle).start();
    }

    private void handle() {
        logger.info("Telnet message processing started");

        while (telnetClient.isConnected()) {
            try {
                this.process();
            } catch (IOException | InterruptedException e) {
                logger.error("Error on reading telnet messages, error: {}", e.getMessage());
            }
        }
    }

    private void process() throws IOException, InterruptedException {
        BufferedReader reader = telnetClient.getStreamReader();
        buffer.clear();

        Instant start = Instant.now();
        Duration timeout = Duration.ofSeconds(1);

        while (Duration.between(start, Instant.now()).compareTo(timeout) < 0) {
            if (reader.ready()) {
                String line = reader.readLine();
                if (line != null) {
                    buffer.add(line);
                }
            } else {
                // Expected to save CPU time
                Thread.sleep(50);
            }
        }

        if (!buffer.isEmpty()) {
            for (TelnetMessageParser<?> parser : telnetMessageParsers) {
                TelnetEvent event = parser.parse(buffer);

                if (event != null) {
                    eventManager.publish(event);
                    logger.info("Event with type {} generated", event.getClass().getSimpleName());
                }
            }

            buffer.clear();
        }
    }

    /**
     * Default preset terminal settings
     */
    private final class DefaultTelnetTerminalConfiguration {

        /**
         * Enable real-time output of log messages
         */
        private static final String LOG_ON = "log on";

        private void configure() {
            if (telnetClient.isConnected()) {
                logger.info("Configuring Telnet terminal...");
                commandSender.send(LOG_ON);
            }
        }
    }
}
