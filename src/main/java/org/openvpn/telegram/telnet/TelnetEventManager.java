package org.openvpn.telegram.telnet;

import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.openvpn.telegram.telnet.listeners.ITelnetEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelnetEventManager {

    private final List<ITelnetEventListener<?>> listeners;

    private final Logger logger = LoggerFactory.getLogger(TelnetEventManager.class);

    @Autowired
    public TelnetEventManager(List<ITelnetEventListener<?>> listeners) {
        this.listeners = listeners;
    }

    /**
     * Receives the generated event, publishes
     */
    public void fire(TelnetEvent event) {
        for (ITelnetEventListener<?> listener : listeners) {
            if (listener.getSupportedEventType().isInstance(event)) {
                invokeListener(listener, event);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends TelnetEvent> void invokeListener(ITelnetEventListener<T> listener, TelnetEvent event) {
        try {
            listener.onEvent((T) event);
        } catch (ClassCastException e) {
            logger.warn("Typecasting error when calling the listener: {}", listener.getClass().getSimpleName(), e);
        }
    }
}
