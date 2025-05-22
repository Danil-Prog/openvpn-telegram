package org.openvpn.telegram.telnet.parser;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openvpn.telegram.dto.ClientDto;
import org.openvpn.telegram.telnet.events.StatusCommandEvent;
import org.springframework.stereotype.Component;

@Component
public class StatusCommandParser implements TelnetMessageParser<StatusCommandEvent> {

    private static final String START = "OpenVPN CLIENT LIST";
    private static final String END = "ROUTING TABLE";

    @Override
    public StatusCommandEvent parse(List<String> lines) {
        List<String> rawData = new ArrayList<>();

        boolean startRead = false;

        for (String line : lines) {
            if (line.startsWith(START)) {
                startRead = true;
            }

            if (line.startsWith(END)) {
                startRead = false;
            }

            if (startRead) {
                rawData.add(line);
            }
        }

        List<ClientDto> clients = this.getClientConnectionsFromLines(rawData);

        return new StatusCommandEvent(clients);
    }

    private List<ClientDto> getClientConnectionsFromLines(List<String> rawData) {
        List<ClientDto> clients = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Pattern pattern = Pattern.compile("^([^,]+),([\\d.]+):(\\d+),(\\d+),(\\d+),([\\d\\-]+ [\\d:]+)$");

        rawData.forEach(line -> {
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                String commonName = matcher.group(1);
                String realAddress = matcher.group(2);
                String port = matcher.group(3);
                Long bytesReceived = Long.parseLong(matcher.group(4));
                Long bytesSent = Long.parseLong(matcher.group(5));
                String connectedSince = matcher.group(6);

                //  LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

                clients.add(new ClientDto(commonName, realAddress, bytesReceived, bytesSent, null));
            }
        });

        return clients;
    }

}
