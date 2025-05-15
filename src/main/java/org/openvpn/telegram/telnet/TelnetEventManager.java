package org.openvpn.telegram.telnet;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.openvpn.telegram.telnet.listeners.ITelnetEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelnetEventManager {

    private final List<ITelnetEventListener<?>> listeners;
    private final BlockingQueue<TelnetEvent> events = new LinkedBlockingQueue<>();

    private final Logger logger = LoggerFactory.getLogger(TelnetEventManager.class);

    @Autowired
    public TelnetEventManager(List<ITelnetEventListener<?>> listeners) {
        this.listeners = listeners;
    }

    @PostConstruct
    public void init() {
        new Thread(this::handle).start();
    }

    /**
     * Publishes events to a blocking queue
     */
    public void publish(TelnetEvent event) {
        events.add(event);
    }

    /**
     * Handle generated event
     */
    private void handle() {
        while (true) {
            try {
                TelnetEvent event = events.take();
                for (ITelnetEventListener<?> listener : listeners) {
                    if (listener.getSupportedEventType().isInstance(event)) {
                        invokeListener(listener, event);
                    }
                }
            } catch (InterruptedException e) {
                logger.error("Interrupted while processing event, error: {}", e.getMessage());
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
