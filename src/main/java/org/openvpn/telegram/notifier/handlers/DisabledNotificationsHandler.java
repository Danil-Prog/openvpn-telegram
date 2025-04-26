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
public final class DisabledNotificationsHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final NotificationSettingsService notificationSettingsService;

    private final TypeListener typeListener = TypeListener.DISABLED_NOTIFICATION;

    private final Logger logger = LoggerFactory.getLogger(DisabledNotificationsHandler.class);

    public DisabledNotificationsHandler(
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

        notificationSettingsService.updateNotificationState(false);
        logger.info("Telegram bot notifications disabled");

        String message = "Telegram bot notifications disabled";

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }
}
