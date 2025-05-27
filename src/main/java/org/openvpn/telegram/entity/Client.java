package org.openvpn.telegram.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.openvpn.telegram.constants.TableNames;

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
    private List<Session> sessions = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTraffic() {
        return traffic;
    }

    public void setTraffic(Long traffic) {
        this.traffic = traffic;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }
}
