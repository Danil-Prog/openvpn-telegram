package org.openvpn.telegram.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.listener.IMessageHandler;
import org.openvpn.telegram.notifier.listener.TypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotDefault {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final List<IMessageHandler> messageHandlers;

    private final String ACCESS_DENIED_MESSAGE = "❗ Access denied. You are not the administrator of this bot.";

    private final Logger logger = LoggerFactory.getLogger(TelegramBotDefault.class);

    @Autowired
    public TelegramBotDefault(TelegramBot bot,
                              TelegramBotProperties properties,
                              List<IMessageHandler> messageHandlers) {
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
        Long administrator = properties.getChat();

        bot.setUpdatesListener(updates -> {

                    for (Update update : updates) {
                        Message message = update.message();
                        Long from = message.chat().id();

                        // Ignoring non-administrator messages
                        if (!from.equals(administrator)) {
                            bot.execute(new SendMessage(administrator, ACCESS_DENIED_MESSAGE));
                            continue;
                        }

                        switch (message.text()) {
                            case "/start":
                                getListenerByMessageType(TypeListener.START).handle(update);
                                break;
                            case "/users":
                                getListenerByMessageType(TypeListener.USERS).handle(update);
                                break;
                        }

                    }

                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }
        );
    }

    private IMessageHandler getListenerByMessageType(TypeListener typeListener) {
        return messageHandlers.stream().filter(listener -> listener.getTypeListener() == typeListener).findFirst().orElse(null);
    }
}
