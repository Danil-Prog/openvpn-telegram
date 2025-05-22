package org.openvpn.telegram.telnet.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openvpn.telegram.dto.ClientDto;
import org.openvpn.telegram.telnet.events.StatusCommandEvent;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);

        Pattern pattern = Pattern.compile("^([^,]+),([\\d.]+):(\\d+),(\\d+),(\\d+),([\\d\\-]+ [\\d:]+)$");


        rawData.forEach(line -> {
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                System.out.println("Name: " + matcher.group(1));
                System.out.println("IP: " + matcher.group(2));
                System.out.println("Port: " + matcher.group(3));
                System.out.println("Value1: " + matcher.group(4));
                System.out.println("Value2: " + matcher.group(5));
                System.out.println("DateTime: " + matcher.group(6));
            }
        });

        return new ArrayList<>();
    }

}


//    status
//    OpenVPN CLIENT LIST
//    Updated,2025-05-22 10:41:09
//    Common Name,Real Address,Bytes Received,Bytes Sent,Connected Since
//    diana-g,88.201.206.52:4674,130398,191625,2025-05-22 10:40:37
//    gavr-dg,92.125.152.58:56838,4289,8244,2025-05-22 10:41:04
//    ROUTING TABLE
//    Virtual Address,Common Name,Real Address,Last Ref
//    10.8.0.4,diana-g,88.201.206.52:4674,2025-05-22 10:41:07
//    10.8.0.3,gavr-dg,92.125.152.58:56838,2025-05-22 10:41:08
//    GLOBAL STATS
//    Max bcast/mcast queue length,1
//    END

