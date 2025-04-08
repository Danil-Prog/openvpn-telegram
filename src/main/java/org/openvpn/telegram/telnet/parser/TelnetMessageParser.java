package org.openvpn.telegram.telnet.parser;

import org.openvpn.telegram.telnet.events.TelnetEvent;

import java.util.List;

public interface TelnetMessageParser<T extends TelnetEvent> {

    TelnetEvent parse(List<String> lines);

}
