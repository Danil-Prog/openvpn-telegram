package org.openvpn.telegram.entity;

public record CommandLineArguments(String ip, int port, String botToken, String chatId) {
}
