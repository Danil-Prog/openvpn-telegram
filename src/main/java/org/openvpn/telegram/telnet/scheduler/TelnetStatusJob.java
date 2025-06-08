package org.openvpn.telegram.telnet.scheduler;

import org.openvpn.telegram.service.MonitoringService;
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
    private final MonitoringService monitoringService;

    private final Logger logger = LoggerFactory.getLogger(TelnetStatusJob.class);

    private static final String TELNET_COMMAND = "status";

    @Autowired
    public TelnetStatusJob(
            TelnetCommandSender telnetCommandSender,
            MonitoringService monitoringService
    ) {
        this.telnetCommandSender = telnetCommandSender;
        this.monitoringService = monitoringService;
    }

    @Scheduled(fixedDelayString = "PT05S")
    public void execute() {
        logger.debug("Executing TelnetStatusJob");

        // We request the status only if there are currently connected clients
        if (monitoringService.connectionsIsNotEmpty() || firstLaunch) {
            telnetCommandSender.send(TELNET_COMMAND);
        }

        firstLaunch = false;

        logger.debug("TelnetStatusJob executed");
    }
}
