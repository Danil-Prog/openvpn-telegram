package org.openvpn.telegram.telnet.scheduler;

import org.openvpn.telegram.service.ConnectionService;
import org.openvpn.telegram.telnet.TelnetCommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TelnetStatusJob {

    private Boolean firstLaunch = true;

    private final TelnetCommandSender telnetCommandSender;
    private final ConnectionService connectionService;

    private final Logger logger = LoggerFactory.getLogger(TelnetStatusJob.class);

    private static final String TELNET_COMMAND = "status";

    @Autowired
    public TelnetStatusJob(
            TelnetCommandSender telnetCommandSender,
            ConnectionService connectionService
    ) {
        this.telnetCommandSender = telnetCommandSender;
        this.connectionService = connectionService;
    }

    @Scheduled(fixedDelayString = "PT05S")
    public void execute() {
        logger.debug("Executing TelnetStatusJob");

        // We request the status only if there are currently connected clients
        if (!connectionService.connectionsIsEmpty() || firstLaunch) {
            telnetCommandSender.send(TELNET_COMMAND);
        }

        firstLaunch = false;

        logger.debug("TelnetStatusJob executed");
    }
}
