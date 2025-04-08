package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.springframework.stereotype.Component;

@Component
public class ClientConnectListener implements ITelnetEventListener<ClientConnectedEvent> {

    @Override
    public Class<ClientConnectedEvent> getSupportedEventType() {
        return ClientConnectedEvent.class;
    }

    @Override
    public void onEvent(ClientConnectedEvent event) {
        System.out.println("Client connected: " + event.username());
    }

}
