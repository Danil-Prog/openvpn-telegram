package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ConnectedClientParser implements TelnetMessageParser<ClientConnectedEvent> {

    private static final String START_USERNAME_PATTERN = ">CLIENT:ENV,common_name=";
    private static final String START_IP_PATTERN = ">CLIENT:ENV,untrusted_ip=";

    @Override
    public ClientConnectedEvent parse(List<String> lines) {
        String username = null;
        String ip = null;

        for (String line : lines) {
            if (line.startsWith(START_USERNAME_PATTERN)) {
                username = line.substring(START_USERNAME_PATTERN.length());
            }

            if (line.startsWith(START_IP_PATTERN)) {
                ip = line.substring(START_IP_PATTERN.length());
            }
        }

        if (username != null || ip != null) {
            return new ClientConnectedEvent(username, ip, Instant.now());
        }

        return null;
    }
}
