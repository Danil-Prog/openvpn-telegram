package org.openvpn.telegram.config;

import com.pengrad.telegrambot.TelegramBot;
import org.apache.commons.net.telnet.TelnetClient;
import org.openvpn.telegram.config.properties.TelegramBotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenVpnTelegramConfig {

    private final TelegramBotProperties properties;

    @Autowired
    public OpenVpnTelegramConfig(TelegramBotProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(properties.getToken());
    }

    @Bean
    public TelnetClient telnetClient() {
        return new TelnetClient();
    }
}
