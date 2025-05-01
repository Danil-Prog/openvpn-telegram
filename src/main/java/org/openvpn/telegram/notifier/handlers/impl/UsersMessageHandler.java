package org.openvpn.telegram.notifier.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.handlers.IMessageHandler;
import org.openvpn.telegram.notifier.handlers.TypeListener;
import org.openvpn.telegram.notifier.service.UserEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class UsersMessageHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final UserEventService userEventService;

    private final TypeListener typeListener = TypeListener.USERS;

    private final Logger logger = LoggerFactory.getLogger(UsersMessageHandler.class);

    public UsersMessageHandler(
            TelegramBot bot,
            TelegramBotProperties properties,
            UserEventService userEventService
    ) {
        this.properties = properties;
        this.bot = bot;
        this.userEventService = userEventService;
    }

    @Override
    public TypeListener getTypeListener() {
        return this.typeListener;
    }

    @Override
    public void handle(Update update) {
        Set<String> users = userEventService.getUsers();

        Long adminChatId = properties.getChat();

        if (users.isEmpty()) {
            SendMessage sendMessage = new SendMessage(adminChatId, "📋 No users found");
            bot.execute(sendMessage);
            return;
        }
        String usersFormatted = users.stream().map(it -> " * " + it + "\n").collect(Collectors.joining());

        String message = "📋 All connections to VPN server:\n" + usersFormatted;

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }
}
