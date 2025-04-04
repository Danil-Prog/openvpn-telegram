package org.openvpn.telegram.notifier.listener;

import com.pengrad.telegrambot.model.Update;

public interface IMessageHandler {

    TypeListener getTypeListener();

    void handle(Update update);
}
