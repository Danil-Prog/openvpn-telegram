package org.openvpn.telegram.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class TelegramBotDefault {

    private final TelegramBot telegramBot;
    private final String adminChatId;

    public TelegramBotDefault(String token, String chatId) {
        this.telegramBot = new TelegramBot(token);
        this.adminChatId = chatId;
        this.init();
    }

    /**
     * Run initialization logic telegram bot
     */
    private void init() {
        System.out.println("Initializing Telegram Bot...");

        telegramBot.setUpdatesListener(updates -> {
                    for (Update update : updates) {
                        String message = update.message().text();

                        if (message.startsWith("/bot")) {
                            SendMessage sendMessage = new SendMessage(adminChatId, "Hi, running monitoring OpenVPN server...");
                            telegramBot.execute(sendMessage);
                        }
                    }
                    return UpdatesListener.CONFIRMED_UPDATES_ALL;
                }
        );
    }
}
