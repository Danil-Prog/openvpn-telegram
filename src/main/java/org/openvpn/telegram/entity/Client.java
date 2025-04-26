package org.openvpn.telegram.entity;

import jakarta.persistence.*;
import org.openvpn.telegram.constants.TableNames;

@Entity
@Table(name = TableNames.CLIENT)
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
