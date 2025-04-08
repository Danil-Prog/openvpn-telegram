package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ConnectedClientParser implements TelnetMessageParser<ClientConnectedEvent> {

    private final String START_WITH_USERNAME = ">CLIENT:ENV,common_name=";
    private final String START_WITH_IP = ">CLIENT:ENV,untrusted_ip=";

    @Override
    public ClientConnectedEvent parse(List<String> lines) {
        return lines.stream()
                .filter(line -> line.contains(START_WITH_USERNAME))
                .map(line -> {
                    String username = line.replace(START_WITH_USERNAME, "");
                    return new ClientConnectedEvent(username, Instant.now());
                })
                .findFirst()
                .orElse(null);
    }

}
