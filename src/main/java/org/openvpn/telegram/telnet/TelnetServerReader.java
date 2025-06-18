package org.openvpn.telegram.telnet;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Receives and processes telnet messages, generates events
 */
@Component
public class TelnetServerReader {

    private final ICommandSender commandSender;
    private final ITelnetClient telnetClient;
    private final List<String> buffer = new ArrayList<>();

    private final UnprocessCommandReceiver unprocessCommandReceiver;

    private static final Logger logger = LoggerFactory.getLogger(TelnetServerReader.class);

    @Autowired
    public TelnetServerReader(
            @Qualifier("telnetClientDefault") ITelnetClient telnetClient,
            @Qualifier("telnetCommandSender") ICommandSender commandSender,
            UnprocessCommandReceiver unprocessCommandReceiver
    ) {
        this.telnetClient = telnetClient;
        this.commandSender = commandSender;
        this.unprocessCommandReceiver = unprocessCommandReceiver;

        new DefaultTelnetTerminalConfiguration().configure();
    }

    @PostConstruct
    public void init() {
        Thread.startVirtualThread(this::handle);
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

        processBufferAndClear();
    }

    private void processBufferAndClear() {
        Thread.startVirtualThread(() -> {
            if (!buffer.isEmpty()) {
                unprocessCommandReceiver.receive(buffer);
                unprocessCommandReceiver.process();

                buffer.clear();
            }
        });
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
