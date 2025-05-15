package org.openvpn.telegram.telnet.parser;

import java.util.List;
import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.springframework.lang.Nullable;

public interface TelnetMessageParser<T extends TelnetEvent> {

    @Nullable
    T parse(List<String> lines);

}
