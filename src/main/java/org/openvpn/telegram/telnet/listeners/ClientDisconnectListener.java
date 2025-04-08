package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.springframework.stereotype.Component;

@Component
public class ClientDisconnectListener implements ITelnetEventListener<ClientDisconnectedEvent> {

    @Override
    public Class<ClientDisconnectedEvent> getSupportedEventType() {
        return ClientDisconnectedEvent.class;
    }

    @Override
    public void onEvent(ClientDisconnectedEvent event) {
        System.out.println("Client disconnected: " + event.username());
    }

}
