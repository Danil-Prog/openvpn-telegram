package org.openvpn.telegram.notifier.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.openvpn.telegram.telnet.ICommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UsersMessageHandler implements IMessageHandler {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final ICommandSender commandSender;
    private final TypeListener typeListener = TypeListener.USERS;

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

        commandSender.send("pid\r\n");

        SendMessage sendMessage = new SendMessage(adminChatId, "Current users: gavr-dg");
        bot.execute(sendMessage);
    }
}
