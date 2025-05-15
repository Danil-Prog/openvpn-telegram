package org.openvpn.telegram.service;

import java.util.Optional;
import org.openvpn.telegram.entity.Client;
import org.openvpn.telegram.repository.ClientRepository;
import org.openvpn.telegram.telnet.events.ClientConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitoringService {

    private final ClientRepository clientRepository;

    private final Logger logger = LoggerFactory.getLogger(MonitoringService.class);

    @Autowired
    public MonitoringService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void updateUserInformation(ClientConnectedEvent event) {
        Optional<Client> optionalClient = clientRepository.findByUsername(event.username());

        optionalClient.ifPresent(client -> {
            logger.info("Updating user information for username[{}]", event.username());

        });

    }
}
