package org.openvpn.telegram.notifier.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.openvpn.telegram.telnet.events.ClientDisconnectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserEventService {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;

    private final Set<String> users = new HashSet<>();

    @Autowired
    public UserEventService(
            TelegramBot bot,
            TelegramBotProperties properties
    ) {
        this.bot = bot;
        this.properties = properties;
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
                        Platform: %s
                        Connected date: %s
                        
                        Revoke user: /revoke %s""",
                event.ip(),
                event.username(),
                event.platform(),
                timeConnectFormat,
                event.username()
        );

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

    public synchronized Set<String> getUsers() {
        return users;
    }

    private String dateTimeFormatter(Instant time) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(time);
    }
}
