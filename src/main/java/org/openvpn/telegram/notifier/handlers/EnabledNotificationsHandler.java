package org.openvpn.telegram.notifier.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.service.NotificationSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EnabledNotificationsHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final NotificationSettingsService notificationSettingsService;

    private final TypeListener typeListener = TypeListener.ENABLED_NOTIFICATION;

    private final Logger logger = LoggerFactory.getLogger(EnabledNotificationsHandler.class);

    public EnabledNotificationsHandler(
            TelegramBot bot,
            TelegramBotProperties properties,
            NotificationSettingsService notificationSettingsService
    ) {
        this.bot = bot;
        this.properties = properties;
        this.notificationSettingsService = notificationSettingsService;
    }

    @Override
    public TypeListener getTypeListener() {
        return this.typeListener;
    }

    @Override
    public void handle(Update update) {
        Long adminChatId = properties.getChat();

        notificationSettingsService.updateNotificationState(true);
        logger.info("Telegram bot notifications enabled");

        String message = "Telegram bot notifications enabled";

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }
}
