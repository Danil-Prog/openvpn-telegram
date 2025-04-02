package org.openvpn.telegram.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.apache.commons.net.telnet.TelnetClient;
import org.openvpn.telegram.configuration.properties.TelegramBotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenVpnTelegramConfiguration {

    private final TelegramBotProperties properties;

    @Autowired
    public OpenVpnTelegramConfiguration(TelegramBotProperties properties) {
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
