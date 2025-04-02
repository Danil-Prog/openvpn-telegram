package org.openvpn.telegram.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotDefault {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotDefault.class);

    @Autowired
    public TelegramBotDefault(TelegramBot bot, TelegramBotProperties properties) {
        this.bot = bot;
        this.properties = properties;
    }

    /**
     * Run initialization logic telegram bot
     */
    @PostConstruct
    private void init() {
        logger.info("Initializing Telegram Bot...");
        String adminChatId = properties.getChat();

        bot.setUpdatesListener(updates -> {
                    for (Update update : updates) {
                        String message = update.message().text();

                        if (message.startsWith("/bot")) {
                            SendMessage sendMessage = new SendMessage(adminChatId, "Hi, running monitoring OpenVPN server...");
                            bot.execute(sendMessage);
                        }
                    }
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }
        );
    }
}
