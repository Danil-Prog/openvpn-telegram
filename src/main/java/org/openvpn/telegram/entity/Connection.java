package org.openvpn.telegram.entity;

import java.time.Instant;

public class Connection {

    private final String username;
    private final String ip;
    private final Instant connectedAt;
    private final Instant disconnectedAt;
    private Long totalBytesReceived;
    private Long totalBytesSent;

    public Connection(
            String username,
            String ip,
            Instant connectedAt,
            Instant disconnectedAt,
            Long totalBytesReceived,
            Long totalBytesSent
    ) {
        this.username = username;
        this.ip = ip;
        this.connectedAt = connectedAt;
        this.disconnectedAt = disconnectedAt;
        this.totalBytesReceived = totalBytesReceived;
        this.totalBytesSent = totalBytesSent;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public Instant getConnectedAt() {
        return connectedAt;
    }

    public Instant getDisconnectedAt() {
        return disconnectedAt;
    }

    public Long getTotalBytesReceived() {
        return totalBytesReceived;
    }

    public void setTotalBytesReceived(Long totalBytesReceived) {
        this.totalBytesReceived = totalBytesReceived;
    }

    public Long getTotalBytesSent() {
        return totalBytesSent;
    }

    public void setTotalBytesSent(Long totalBytesSent) {
        this.totalBytesSent = totalBytesSent;
    }
}
