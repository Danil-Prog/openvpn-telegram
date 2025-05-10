package org.openvpn.telegram.constants;

public final class TableNames {

    private static final String DB_PREFIX = "openvpn_telegram_";

    public static final String CLIENTS = DB_PREFIX + "clients";
    public static final String SESSIONS = DB_PREFIX + "sessions";
    public static final String CLIENTS_SESSIONS = DB_PREFIX + "clients_sessions";
    public static final String NOTIFICATION_SETTINGS = DB_PREFIX + "notification_settings";
}
