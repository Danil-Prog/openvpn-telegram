package org.openvpn.telegram.entity;

import jakarta.persistence.*;
import java.util.Date;
import org.openvpn.telegram.constants.TableNames;

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
    private Long sessionDurationSeconds;

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

    public Long getSessionDurationSeconds() {
        return sessionDurationSeconds;
    }

    public void setSessionDurationSeconds(Long sessionDurationSeconds) {
        this.sessionDurationSeconds = sessionDurationSeconds;
    }
}
