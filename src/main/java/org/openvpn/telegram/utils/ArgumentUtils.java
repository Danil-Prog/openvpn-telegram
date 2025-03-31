package org.openvpn.telegram.utils;

import org.openvpn.telegram.entity.CommandLineArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentUtils {

    private static final Logger logger = LoggerFactory.getLogger(ArgumentUtils.class);

    public static CommandLineArguments parseConnectionParams(String[] args) {

        String ip = null;
        int port = 0;

        String botToken = null;
        String chatId = null;

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--ip") || args[i].equals("-i")) {
                    ip = args[i + 1];
                }

                if (args[i].equals("--port") || args[i].equals("-p")) {
                    port = Integer.parseInt(args[i + 1]);
                }

                if (args[i].equals("--bot-token")) {
                    botToken = args[i + 1];
                }

                if (args[i].equals("--chatId")) {
                    chatId = args[i + 1];
                }

            }
        } catch (IndexOutOfBoundsException e) {
            logger.error("Error parsing connection parameters: {}", e.getMessage());
            throw e;
        }

        validateArgs(ip, port, botToken, chatId);

        return new CommandLineArguments(ip, port, botToken, chatId);
    }

    private static void validateArgs(String ip, int port, String botToken, String chatId) {
        if (ip == null || ip.isEmpty()) {
            throw new IllegalArgumentException("IP address cannot be empty");
        }

        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 0 and 65535");
        }

        if (botToken == null || botToken.isEmpty()) {
            throw new IllegalArgumentException("Bot token cannot be empty");
        }

        if (chatId == null || chatId.isEmpty()) {
            throw new IllegalArgumentException("Chat ID cannot be empty");
        }
    }
}
