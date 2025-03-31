package org.openvpn.telegram.entity;

public record Client(String username, int port, String ip) {
}
