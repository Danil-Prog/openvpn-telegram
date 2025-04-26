package org.openvpn.telegram.notifier.handlers;

public enum TypeListener {
    START("/start"),
    USERS("/users"),
    ENABLED_NOTIFICATION("/enabled_notifications"),
    DISABLED_NOTIFICATION("/disabled_notifications"),
    ;

    TypeListener(String command) {
    }

}
