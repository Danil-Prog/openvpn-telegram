package org.openvpn.telegram.service;

import org.openvpn.telegram.notifier.service.ClientEventService;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ClientEventService clientEventService;
    private final NotificationSettingsService notificationSettingsService;

    @Autowired
    public NotificationService(
            ClientEventService clientEventService,
            NotificationSettingsService notificationSettingsService
    ) {
        this.clientEventService = clientEventService;
        this.notificationSettingsService = notificationSettingsService;
    }

    public void clientConnectionNotification(ClientConnectedEvent event) {
        if (notificationSettingsService.isNotificationSettingsEnabled()) {
            clientEventService.clientConnected(event);
        }
    }

    public void clientDisconnectionNotification(ClientDisconnectedEvent event) {
        if (notificationSettingsService.isNotificationSettingsEnabled()) {
            clientEventService.clientDisconnected(event);
        }
    }
}
