package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DisconnectedClientParser implements TelnetMessageParser<ClientDisconnectedEvent> {

    private final String END_WITH_DISCONNECTED = "received, client-instance exiting";

    @Override
    public ClientDisconnectedEvent parse(List<String> lines) {
        String username = null;
        String ip = null;

        for (String line : lines) {
            if (!line.contains(END_WITH_DISCONNECTED)) {
                continue;
            }

            Pattern pattern = Pattern.compile(">LOG:\\d+,,([^/]+)/([\\d.]+):\\d+");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                username = matcher.group(1);
                ip = matcher.group(2);
            }
        }

        if (username != null && ip != null) {
            return new ClientDisconnectedEvent(username, ip, Instant.now());
        }

        return null;
    }

}
