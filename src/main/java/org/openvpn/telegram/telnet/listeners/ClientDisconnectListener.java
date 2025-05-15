package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.service.MonitoringService;
import org.openvpn.telegram.service.NotificationService;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientDisconnectListener implements ITelnetEventListener<ClientDisconnectedEvent> {

    private final NotificationService notificationService;
    private final MonitoringService monitoringService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ClientDisconnectListener(
            NotificationService notificationService,
            MonitoringService monitoringService
    ) {
        this.notificationService = notificationService;
        this.monitoringService = monitoringService;
    }

    @Override
    public Class<ClientDisconnectedEvent> getSupportedEventType() {
        return ClientDisconnectedEvent.class;
    }

    @Override
    public void onEvent(ClientDisconnectedEvent event) {
        logger.info("Client disconnected: username[{}], ip[{}]", event.username(), event.ip());
        notificationService.clientDisconnectionNotification(event);
        monitoringService.clientDisconnected(event);
    }
}
