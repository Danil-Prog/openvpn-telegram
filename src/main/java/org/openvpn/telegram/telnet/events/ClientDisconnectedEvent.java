package org.openvpn.telegram.telnet.events;

import java.time.Instant;

public record ClientDisconnectedEvent(
        String username,
        String ip,
        Instant timeDisconnected
) implements TelnetEvent {
}
