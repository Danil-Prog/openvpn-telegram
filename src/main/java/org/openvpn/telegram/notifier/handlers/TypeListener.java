package org.openvpn.telegram.notifier.handlers;

public enum TypeListener {
    START("/start"),
    USERS("/users"),
    ;

    TypeListener(String command) {
    }

}
