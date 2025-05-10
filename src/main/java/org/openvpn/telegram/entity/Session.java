package org.openvpn.telegram.entity;

import jakarta.persistence.*;
import org.openvpn.telegram.constants.TableNames;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = TableNames.SESSIONS)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bytes_received")
    private Long bytesReceived;

    @Column(name = "bytes_sent")
    private Long bytesSent;

    @Column(name = "time_connected")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeConnected;

    @Column(name = "time_disconnected")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeDisconnected;

    @Column(name = "duration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionDuration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(Long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public Long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(Long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public Date getTimeConnected() {
        return timeConnected;
    }

    public void setTimeConnected(Date timeConnected) {
        this.timeConnected = timeConnected;
    }

    public Date getTimeDisconnected() {
        return timeDisconnected;
    }

    public void setTimeDisconnected(Date timeDisconnected) {
        this.timeDisconnected = timeDisconnected;
    }

    public Date getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(Date sessionDuration) {
        this.sessionDuration = sessionDuration;
    }
}
