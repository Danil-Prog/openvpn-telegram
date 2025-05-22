package org.openvpn.telegram.telnet.scheduler.conditional;

import org.openvpn.telegram.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

//@Component
//public class TelnetStatusJobConditional implements Condition {
//
//    private final MonitoringService monitoringService;
//
//    @Autowired
//    public TelnetStatusJobConditional(MonitoringService monitoringService) {
//        this.monitoringService = monitoringService;
//    }
//
//    /**
//     * We request the status only if there are currently connected clients
//     */
//    @Override
//    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
//        return monitoringService.connectionsExist();
//    }
//
//}
