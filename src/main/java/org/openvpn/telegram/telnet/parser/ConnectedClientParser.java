package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ConnectedClientParser implements TelnetMessageParser<ClientConnectedEvent> {

    private static final String START_WITH_USERNAME = ">CLIENT:ENV,common_name=";
    private static final String START_WITH_IP = ">CLIENT:ENV,untrusted_ip=";

    @Override
    public ClientConnectedEvent parse(List<String> lines) {
        String username = null;
        String ip = null;

        for (String line : lines) {
            if (line.startsWith(START_WITH_USERNAME)) {
                username = line.substring(START_WITH_USERNAME.length());
            }

            if (line.startsWith(START_WITH_IP)) {
                ip = line.substring(START_WITH_IP.length());
            }
        }

        if (username != null || ip != null) {
            return new ClientConnectedEvent(username, ip, Instant.now());
        }

        return null;
    }
}
