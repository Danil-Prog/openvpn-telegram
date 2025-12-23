package org.openvpn.telegram.service;

import org.openvpn.telegram.entity.NotificationSettings;
import org.openvpn.telegram.repository.NotificationSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationSettingsService {


    /**
     * Local cache
     */
    private Boolean isNotificationEnabled;

    /**
     * Notification settings exist in a single instance
     */
    private final Long DEFAULT_NOTIFICATION_SETTINGS_PRESET = 1L;

    private final NotificationSettingsRepository notificationSettingsRepository;

    private final Logger logger = LoggerFactory.getLogger(NotificationSettingsService.class);

    @Autowired
    public NotificationSettingsService(NotificationSettingsRepository notificationSettingsRepository) {
        this.notificationSettingsRepository = notificationSettingsRepository;
    }

    /**
     * Disabled/Enabled TG notification
     */
    public void updateNotificationState(boolean state) {
        if (state == isNotificationEnabled) {
            logger.info("Notification settings already enabled");
            return;
        }

        NotificationSettings settings = notificationSettingsRepository
                .findById(DEFAULT_NOTIFICATION_SETTINGS_PRESET)
                .get();

        settings.setEnabledNotification(true);

        notificationSettingsRepository.save(settings);
        isNotificationEnabled = state;
    }

    public boolean isNotificationSettingsEnabled() {
        if (isNotificationEnabled != null) {
            return isNotificationEnabled;
        }

        NotificationSettings settings = notificationSettingsRepository
                .findById(DEFAULT_NOTIFICATION_SETTINGS_PRESET).get();

        return settings.getEnabled();
    }

}
