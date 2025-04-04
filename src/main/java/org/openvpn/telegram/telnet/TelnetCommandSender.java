package org.openvpn.telegram.telnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
public class TelnetCommandSender implements ICommandSender {

    private final ITelnetClient telnetClient;

    private static final Logger logger = LoggerFactory.getLogger(TelnetCommandSender.class);

    @Autowired
    public TelnetCommandSender(@Qualifier("telnetClientDefault") ITelnetClient telnetClient) {
        this.telnetClient = telnetClient;
    }

    @Override
    public void send(String command) {
        try {
            OutputStream writer = telnetClient.getStreamWriter();
            writer.write(command.getBytes());
            writer.flush();
        } catch (Exception e) {
            logger.error("Error sending command, error: {}", e.getMessage());
        }
    }

}
