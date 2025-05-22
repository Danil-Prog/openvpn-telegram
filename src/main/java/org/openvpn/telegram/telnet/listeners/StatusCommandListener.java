package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.telnet.events.StatusCommandEvent;

public class StatusCommandListener implements ITelnetEventListener<StatusCommandEvent> {

    @Override
    public Class<StatusCommandEvent> getSupportedEventType() {
        return StatusCommandEvent.class;
    }

    @Override
    public void onEvent(StatusCommandEvent event) {

    }
}
