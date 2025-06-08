package org.openvpn.telegram.repository;

import org.openvpn.telegram.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT client FROM Client AS client WHERE client.username=':username'")
    Optional<Client> findByUsername(String username);
}
