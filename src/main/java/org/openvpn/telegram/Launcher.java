package org.openvpn.telegram;

import org.openvpn.telegram.entity.CommandLineArguments;
import org.openvpn.telegram.entity.ConnectionParams;
import org.openvpn.telegram.telnet.ITelnetClient;
import org.openvpn.telegram.telnet.TelnetClientDefault;
import org.openvpn.telegram.telnet.TelnetHandler;
import org.openvpn.telegram.tg.TelegramBotDefault;
import org.openvpn.telegram.utils.ArgumentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        logger.info("Starting OpenVPN monitoring...");

        final CommandLineArguments commandLineArguments = ArgumentUtils.parseConnectionParams(args);

        var connectionParams = new ConnectionParams(commandLineArguments.ip(), commandLineArguments.port());

        ITelnetClient telnetClient = new TelnetClientDefault(connectionParams);
        telnetClient.connect();

        // Allocate threads for the telnet connection and separately for the bot
        Thread telnetThread = new Thread(() -> new TelnetHandler(telnetClient).handle());
        Thread botThread = new Thread(() -> new TelegramBotDefault(commandLineArguments.botToken(), commandLineArguments.chatId()));

        telnetThread.start();
        botThread.start();
    }
}
