package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.notifier.service.UserEventService;
import org.openvpn.telegram.service.NotificationSettingsService;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientDisconnectListener implements ITelnetEventListener<ClientDisconnectedEvent> {

    private final UserEventService userEventService;
    private final NotificationSettingsService notificationSettingsService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ClientDisconnectListener(
            UserEventService userEventService,
            NotificationSettingsService notificationSettingsService
    ) {
        this.userEventService = userEventService;
        this.notificationSettingsService = notificationSettingsService;
    }

    @Override
    public Class<ClientDisconnectedEvent> getSupportedEventType() {
        return ClientDisconnectedEvent.class;
    }

    @Override
    public void onEvent(ClientDisconnectedEvent event) {
        logger.info("Client disconnected: username[{}], ip[{}]", event.username(), event.ip());

        if (notificationSettingsService.isNotificationSettingsEnabled()) {
            userEventService.userDisconnected(event);
        }
    }

}
