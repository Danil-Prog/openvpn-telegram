package org.openvpn.telegram.notifier.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.telnet.ICommandSender;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsersMessageHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final ICommandSender commandSender;
    private final TypeListener typeListener = TypeListener.USERS;

    private final Set<String> users = new HashSet<>();

    private final Logger logger = LoggerFactory.getLogger(UsersMessageHandler.class);

    public UsersMessageHandler(TelegramBot bot, TelegramBotProperties properties, ICommandSender commandSender) {
        this.commandSender = commandSender;
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

    public void onEvent(ClientConnectedEvent event) {
        Long adminChatId = properties.getChat();
        users.add(event.username());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        String timeConnectFormat = formatter.format(event.timeConnected());

        String message = String.format("""
                🌐 VPN user connected
                IP: %s
                Username: %s
                Connected time: %s""", event.username(), event.ip(), timeConnectFormat);

        SendMessage sendMessage = new SendMessage(adminChatId, message);
        bot.execute(sendMessage);
    }
}
