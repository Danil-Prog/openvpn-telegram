package org.openvpn.telegram.notifier.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.handlers.IMessageHandler;
import org.openvpn.telegram.notifier.handlers.TypeListener;
import org.openvpn.telegram.telnet.ICommandSender;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class UsersMessageHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;

    private final TypeListener typeListener = TypeListener.USERS;

    private final Set<String> users = new HashSet<>();

    private final Logger logger = LoggerFactory.getLogger(UsersMessageHandler.class);

    public UsersMessageHandler(TelegramBot bot, TelegramBotProperties properties, ICommandSender commandSender) {
        this.properties = properties;
        this.bot = bot;
    }

    @Override
    public TypeListener getTypeListener() {
        return this.typeListener;
    }

    @Override
    public void handle(Update update) {
        Long adminChatId = properties.getChat();

        if (users.isEmpty()) {
            SendMessage sendMessage = new SendMessage(adminChatId, "No users found");
            bot.execute(sendMessage);
            return;
        }
        String usersFormatted = users.stream().map(it -> it + "\n").collect(Collectors.joining());

        String message = "📋 All connections to VPN server:\n" + usersFormatted;

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }

    public void userConnected(ClientConnectedEvent event) {
        Long adminChatId = properties.getChat();

        if (users.contains(event.username())) {
            return;
        }
        users.add(event.username());

        String timeConnectFormat = dateTimeFormatter(event.timeConnected());

        String message = String.format("""
                🌐 VPN user connected
                IP: %s
                Username: %s
                Connected date: %s
                
                Revoke user: /revoke %s""", event.ip(), event.username(), timeConnectFormat, event.username());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("revoke").callbackData("/revoke " + event.username())
        );

        SendMessage sendMessage = new SendMessage(adminChatId, message).replyMarkup(inlineKeyboardMarkup);
        bot.execute(sendMessage);
    }

    public void userDisconnected(ClientDisconnectedEvent event) {
        Long adminChatId = properties.getChat();

        if (!users.contains(event.username())) {
            return;
        }

        users.remove(event.username());

        String timeDisconnectedFormat = dateTimeFormatter(event.timeDisconnected());

        String message = String.format("""
                ⚡ VPN user disconnected
                IP: %s
                Username: %s
                Disconnected date: %s""", event.ip(), event.username(), timeDisconnectedFormat);

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }

    private String dateTimeFormatter(Instant time) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(time);
    }
}
