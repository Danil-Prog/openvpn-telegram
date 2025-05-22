package org.openvpn.telegram.telnet.events;

import java.util.List;
import org.openvpn.telegram.dto.ClientDto;

public record StatusCommandEvent(List<ClientDto> clientConnections) implements TelnetEvent {
}
