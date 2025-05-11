package org.openvpn.telegram.telnet.events;

import java.time.Instant;

public record ClientConnectedEvent(
        String username,
        String ip,
        String platform,
        Instant timeConnected
) implements TelnetEvent {
}
