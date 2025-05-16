package org.openvpn.telegram.notifier.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.config.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.handlers.IMessageHandler;
import org.openvpn.telegram.notifier.handlers.TypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StartMessageHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final TypeListener typeListener = TypeListener.START;

    private final Logger logger = LoggerFactory.getLogger(StartMessageHandler.class);

    public StartMessageHandler(TelegramBot bot, TelegramBotProperties properties) {
        this.properties = properties;
        this.bot = bot;
    }

    @Override
    public TypeListener getTypeListener() {
        return typeListener;
    }

    @Override
    public void handle(Update update) {
        Long adminChatId = properties.getChat();

        SendMessage sendMessage = new SendMessage(adminChatId, "Hi, running monitoring OpenVPN server...");
        bot.execute(sendMessage);
    }
}
