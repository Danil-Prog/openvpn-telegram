package org.openvpn.telegram.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.openvpn.telegram.constants.TableNames;

@Entity
@Table(name = TableNames.NOTIFICATION_SETTINGS)
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notifications_enabled")
    private Boolean enabled;

    @OneToMany
    @JoinColumn(name = "settings_id")
    private List<Client> disabledClients = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Client> getDisabledClients() {
        return disabledClients;
    }

    public void addDisabledClient(Client client) {
        this.disabledClients.add(client);
    }
}
