package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ConnectedClientParser implements TelnetMessageParser<ClientConnectedEvent> {

    private static final String START_USERNAME_PATTERN = ">CLIENT:ENV,common_name=";
    private static final String START_IP_PATTERN = ">CLIENT:ENV,untrusted_ip=";
    private static final String START_IV_PLAT = ">CLIENT:ENV,IV_PLAT=";
    private static final String START_TIME_ASCII = ">CLIENT:ENV,time_ascii=";

    @Override
    public ClientConnectedEvent parse(List<String> lines) {
        String username = null;
        String ip = null;
        String platform = null;
        Instant time = null;

        for (String line : lines) {
            if (line.startsWith(START_USERNAME_PATTERN)) {
                username = line.substring(START_USERNAME_PATTERN.length());
                System.out.println(line);
            }

            if (line.startsWith(START_IP_PATTERN)) {
                ip = line.substring(START_IP_PATTERN.length());
                System.out.println(line);

            }

            if (line.startsWith(START_IV_PLAT)) {
                platform = line.substring(START_IV_PLAT.length());
                System.out.println(line);
            }

            if (line.startsWith(START_TIME_ASCII)) {
                String date = line.substring(START_TIME_ASCII.length());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

                time = localDateTime.toInstant(ZoneOffset.UTC);
            }
        }

        if (username != null || ip != null) {
            return new ClientConnectedEvent(username, ip, platform, time);
        }

        return null;
    }
}
