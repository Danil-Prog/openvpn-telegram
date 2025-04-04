package org.openvpn.telegram.notifier.listener;

import java.util.Arrays;

public enum TypeListener {
    START("/start"),
    USERS("/users"),
    ;

    private final String messageType;

    TypeListener(String s) {
        this.messageType = s;
    }

    public TypeListener getListenerByMessageType(String messageType) {
        return Arrays.stream(values()).filter(it -> it.messageType.equals(messageType)).findFirst().orElse(null);
    }

}
