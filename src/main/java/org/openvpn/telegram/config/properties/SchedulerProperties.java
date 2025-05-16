package org.openvpn.telegram.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Naming: {jobName}Interval
 */
@ConfigurationProperties("scheduler")
public class SchedulerProperties {

    private Long telnetStatusInterval;

    public Long getTelnetStatusInterval() {
        return telnetStatusInterval;
    }
}
