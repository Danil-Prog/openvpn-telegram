package org.openvpn.telegram.telnet.listeners;

import org.openvpn.telegram.telnet.events.TelnetEvent;

public interface ITelnetEventListener<T extends TelnetEvent> {

    Class<T> getSupportedEventType();

    void onEvent(T event);
}
