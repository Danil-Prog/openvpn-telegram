package org.openvpn.telegram.service;

import org.openvpn.telegram.entity.NotificationSettings;
import org.openvpn.telegram.repository.NotificationSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class NotificationSettingsService {

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
     * Disabled TG notification
     */
    public void updateNotificationState(boolean enabled) {
        notificationSettingsRepository
                .findById(DEFAULT_NOTIFICATION_SETTINGS_PRESET)
                .ifPresent(settings -> {
                    settings.setEnabled(enabled);
                    logger.info("Default setting notification state changed to [{}]", enabled);

                    notificationSettingsRepository.save(settings);
                });
    }

    public boolean isNotificationSettingsEnabled() {
        NotificationSettings settings = notificationSettingsRepository
                .findById(DEFAULT_NOTIFICATION_SETTINGS_PRESET)
                .orElseThrow(() -> new NoSuchElementException("Notification settings not found"));

        return settings.getEnabled();
    }

}
