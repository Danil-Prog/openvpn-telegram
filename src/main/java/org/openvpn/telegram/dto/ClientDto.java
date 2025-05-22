package org.openvpn.telegram.dto;

import java.util.Date;

public record ClientDto(
        String commonName,
        String realAddress,
        Long bytesReceived,
        Long bytesSent,
        Date connectedSince
) {
}
