package org.openvpn.telegram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.openvpn.telegram.constants.TableNames;

@Entity
@Table(name = TableNames.STATISTICS)
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Client client;

    @Column(name = "total_bytes_received")
    private Long totalBytesReceived;

    @Column(name = "total_bytes_sent")
    private Long totalBytesSent;

    @Column(name = "total_connection_time")
    private Long totalConnectionTime;
}
