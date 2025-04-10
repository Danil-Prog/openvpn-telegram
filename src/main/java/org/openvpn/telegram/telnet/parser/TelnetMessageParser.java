package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.springframework.lang.Nullable;

import java.util.List;

public interface TelnetMessageParser<T extends TelnetEvent> {

    @Nullable
    T parse(List<String> lines);

}
