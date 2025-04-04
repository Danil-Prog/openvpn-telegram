package org.openvpn.telegram.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("telegram.bot")
public class TelegramBotProperties {

    private String token;

    private Long chat;

    public String getToken() {
        return token;
    }

    public Long getChat() {
        return chat;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setChat(Long chat) {
        this.chat = chat;
    }
}
