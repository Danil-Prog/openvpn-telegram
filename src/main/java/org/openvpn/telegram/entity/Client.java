package org.openvpn.telegram.entity;

import jakarta.persistence.*;
import org.openvpn.telegram.constants.TableNames;

import java.util.List;

@Entity
@Table(name = TableNames.CLIENTS)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "traffic")
    private Long traffic;

    @Column(name = "is_enabled")
    private Boolean enabled;

    @Column(name = "last_ip_login")
    private String ip;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = TableNames.CLIENTS_SESSIONS,
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id")
    )
    private List<Session> sessions;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
