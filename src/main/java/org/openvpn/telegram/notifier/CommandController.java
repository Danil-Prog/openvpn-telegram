package org.openvpn.telegram.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.openvpn.telegram.config.properties.TelegramBotProperties;
import org.openvpn.telegram.notifier.handlers.IMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandController {

    private final TelegramBot bot;
    private final TelegramBotProperties properties;
    private final List<IMessageHandler> messageHandlers;

    private final String ACCESS_DENIED_MESSAGE = "❗ Access denied. You are not the administrator of this bot.";
    private final String COMMAND_NOT_FOUND = "Command not found. Please check command and try again.";

    private final Logger logger = LoggerFactory.getLogger(CommandController.class);

    @Autowired
    public CommandController(
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
        Thread.startVirtualThread(this::telegramBotStart);
    }

    private void telegramBotStart() {
        logger.info("Initializing Telegram Bot...");
        bot.setUpdatesListener(this::handle);
        setCommands();
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

            var command = message.text();

            try {
                getListenerByCommand(command).handle(update);
            } catch (Exception e) {
                logger.error("Error while handling update", e);
                bot.execute(new SendMessage(administrator, COMMAND_NOT_FOUND));
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void setCommands() {
        BotCommand[] commands = new BotCommand[]{
                new BotCommand("/users", "Пользователи Online"),
                new BotCommand("/all_commands", "Помощь"),
        };

        BaseResponse response = bot.execute(new SetMyCommands(commands));

        if (response.isOk()) {
            logger.info("Successfully set commands");
        } else {
            logger.warn("Failed to set commands: {}", response.description());
        }
    }

    private IMessageHandler getListenerByCommand(String command) {
        return messageHandlers.stream()
                .filter(listener -> listener.getTypeListener().getCommand().equals(command))
                .findFirst()
                .orElse(null);
    }
}
