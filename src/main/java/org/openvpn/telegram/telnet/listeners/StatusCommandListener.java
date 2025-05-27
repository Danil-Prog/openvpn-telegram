package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.service.MonitoringService;
import org.openvpn.telegram.telnet.events.StatusCommandEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusCommandListener implements ITelnetEventListener<StatusCommandEvent> {

    private final MonitoringService monitoringService;

    @Autowired
    public StatusCommandListener(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Override
    public Class<StatusCommandEvent> getSupportedEventType() {
        return StatusCommandEvent.class;
    }

    @Override
    public void onEvent(StatusCommandEvent event) {
        monitoringService.updateClientConnections(event);
    }
}
