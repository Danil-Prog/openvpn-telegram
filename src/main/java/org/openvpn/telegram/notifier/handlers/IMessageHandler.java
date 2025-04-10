package org.openvpn.telegram.notifier.handlers;

import com.pengrad.telegrambot.model.Update;

public interface IMessageHandler {

    TypeListener getTypeListener();

    void handle(Update update);
}
