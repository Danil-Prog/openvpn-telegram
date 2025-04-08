package org.openvpn.telegram.telnet.events;

import java.time.Instant;

public record ClientDisconnectedEvent(
        String username,
        Instant timeDisconnected
) implements TelnetEvent {
}
