package org.openvpn.telegram.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram.bot")
public class TelegramBotProperties {

    private String token;

    private String chat;

    public String getToken() {
        return token;
    }

    public String getChat() {
        return chat;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
