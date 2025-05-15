package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.service.MonitoringService;
import org.openvpn.telegram.service.NotificationService;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientConnectListener implements ITelnetEventListener<ClientConnectedEvent> {

    private final NotificationService notificationService;
    private final MonitoringService monitoringService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ClientConnectListener(
            NotificationService notificationService,
            MonitoringService monitoringService
    ) {
        this.notificationService = notificationService;
        this.monitoringService = monitoringService;
    }

    @Override
    public Class<ClientConnectedEvent> getSupportedEventType() {
        return ClientConnectedEvent.class;
    }

    @Override
    public void onEvent(ClientConnectedEvent event) {
        logger.info("Client connected: username[{}], ip[{}]", event.username(), event.ip());
        notificationService.clientConnectionNotification(event);
        monitoringService.clientConnected(event);
    }
}
