package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.notifier.service.UserEventService;
import org.openvpn.telegram.service.NotificationSettingsService;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientConnectListener implements ITelnetEventListener<ClientConnectedEvent> {

    private final UserEventService userEventService;
    private final NotificationSettingsService notificationSettingsService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ClientConnectListener(
            UserEventService userEventService,
            NotificationSettingsService notificationSettingsService
    ) {
        this.userEventService = userEventService;
        this.notificationSettingsService = notificationSettingsService;
    }

    @Override
    public Class<ClientConnectedEvent> getSupportedEventType() {
        return ClientConnectedEvent.class;
    }

    @Override
    public void onEvent(ClientConnectedEvent event) {
        logger.info("Client connected: username[{}], ip[{}]", event.username(), event.ip());

        if (notificationSettingsService.isNotificationSettingsEnabled()) {
            userEventService.userConnected(event);
        }
    }

}
