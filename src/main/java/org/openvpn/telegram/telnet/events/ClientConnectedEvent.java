package org.openvpn.telegram.telnet.events;

import java.time.Instant;

public record ClientConnectedEvent(
        String username,
        String ip,
        Instant timeConnected
) implements TelnetEvent {
}
