package org.openvpn.telegram.notifier.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.config.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.handlers.IMessageHandler;
import org.openvpn.telegram.notifier.handlers.TypeListener;
import org.springframework.stereotype.Component;

@Component
public class AllCommandsHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;

    private final TypeListener typeListener = TypeListener.ALL_COMMANDS;

    public AllCommandsHandler(TelegramBot bot, TelegramBotProperties properties) {
        this.bot = bot;
        this.properties = properties;
    }

    @Override
    public TypeListener getTypeListener() {
        return this.typeListener;
    }

    @Override
    public void handle(Update update) {
        Long adminChatId = properties.getChat();
        String message = """
                Available commands:
                
                /users - get all users online
                /enable_notification - notify about new connections
                /disable_notification - disable notifications about new connections
                """;

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }
}
