package org.openvpn.telegram.utils;

import org.openvpn.telegram.entity.ConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgumentUtils {

    private static final Logger logger = LoggerFactory.getLogger(ArgumentUtils.class);

    public static ConnectionParams parseConnectionParams(String[] args) {

        String ip = null;
        int port = 0;

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--ip") || args[i].equals("-i")) {
                    ip = args[i + 1];
                }

                if (args[i].equals("--port") || args[i].equals("-p")) {
                    port = Integer.parseInt(args[i + 1]);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            logger.error("Error parsing connection parameters: {}", e.getMessage());
            throw e;
        }

        validateArgs(ip, port);

        return new ConnectionParams(ip, port);
    }

    private static void validateArgs(String ip, int port) {
        if (ip == null || ip.isEmpty()) {
            throw new IllegalArgumentException("IP address cannot be empty");
        }

        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 0 and 65535");
        }
    }
}
