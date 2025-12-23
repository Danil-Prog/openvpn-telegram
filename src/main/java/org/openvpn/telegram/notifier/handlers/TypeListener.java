package org.openvpn.telegram.notifier.handlers;

public enum TypeListener {
    START("/start"),
    USERS("/users"),
    ENABLE_NOTIFICATION("/enable_notification"),
    DISABLE_NOTIFICATION("/disable_notification"),
    ALL_COMMANDS("/all_commands"),
    ;

    private final String command;

    TypeListener(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
