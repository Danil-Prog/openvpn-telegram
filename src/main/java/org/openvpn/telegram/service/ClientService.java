package org.openvpn.telegram.service;

import java.util.Optional;
import org.openvpn.telegram.entity.Client;
import org.openvpn.telegram.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<Client> getClientByUsername(String username) {
        return this.clientRepository.findByUsername(username);
    }

    public void createOrUpdateClient(Client client) {
        this.clientRepository.save(client);
    }
}
