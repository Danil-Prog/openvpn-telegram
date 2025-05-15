package org.openvpn.telegram.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.handlers.IMessageHandler;
import org.openvpn.telegram.notifier.handlers.TypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotManager {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final List<IMessageHandler> messageHandlers;

    private final String ACCESS_DENIED_MESSAGE = "❗ Access denied. You are not the administrator of this bot.";

    private final Logger logger = LoggerFactory.getLogger(TelegramBotManager.class);

    @Autowired
    public TelegramBotManager(
            TelegramBot bot,
            TelegramBotProperties properties,
            List<IMessageHandler> messageHandlers
    ) {
        this.messageHandlers = messageHandlers;
        this.properties = properties;
        this.bot = bot;
    }

    /**
     * Run initialization logic telegram bot
     */
    @PostConstruct
    public void init() {
        new Thread(this::telegramBotStart).start();
    }

    private void telegramBotStart() {
        logger.info("Initializing Telegram Bot...");
        bot.setUpdatesListener(this::handle);
    }

    private int handle(List<Update> updates) {
        Long administrator = properties.getChat();

        updates.forEach(update -> {
            Message message = update.message();
            Long from = message.chat().id();

            // Ignoring non-administrator messages
            if (!from.equals(administrator)) {
                bot.execute(new SendMessage(administrator, ACCESS_DENIED_MESSAGE));
                return;
            }

            switch (message.text()) {
                case "/start":
                    getListenerByMessageType(TypeListener.START).handle(update);
                    break;
                case "/users":
                    getListenerByMessageType(TypeListener.USERS).handle(update);
                    break;
                case "/enabled_notifications":
                    getListenerByMessageType(TypeListener.ENABLED_NOTIFICATION).handle(update);
                    break;
                case "/disabled_notifications":
                    getListenerByMessageType(TypeListener.DISABLED_NOTIFICATION).handle(update);
                    break;
            }

        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private IMessageHandler getListenerByMessageType(TypeListener typeListener) {
        return messageHandlers.stream()
                .filter(listener -> listener.getTypeListener() == typeListener)
                .findFirst()
                .orElse(null);
    }
}
