package org.openvpn.telegram.telnet;

import org.openvpn.telegram.telnet.listeners.ITelnetEventListener;
import org.openvpn.telegram.telnet.listeners.TelnetEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TelnetEventManager {

    private final Map<String, List<ITelnetEventListener>> listeners;

    private final Logger logger = LoggerFactory.getLogger(TelnetEventManager.class);

    public TelnetEventManager(Map<String, List<ITelnetEventListener>> listeners) {
        this.listeners = listeners;
    }

    public void subscribe(TelnetEventType eventType, ITelnetEventListener listener) {
        logger.trace("Subscribing to Telnet event: {}", eventType);
        List<ITelnetEventListener> listeners = this.listeners.get(eventType.name());

        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        listeners.add(listener);
    }

    public void unsubscribe(TelnetEventType eventType, ITelnetEventListener listener) {
        logger.trace("Unsubscribing from Telnet event: {}", eventType);
        List<ITelnetEventListener> listeners = this.listeners.get(eventType.name());
        listeners.remove(listener);
    }

    public void publish(TelnetEventType eventType, String message) {
        logger.trace("Publishing Telnet event: {}", eventType);
        List<ITelnetEventListener> listeners = this.listeners.get(eventType.name());

        for (ITelnetEventListener listener : listeners) {
            listener.publish(message);
        }
    }
}
