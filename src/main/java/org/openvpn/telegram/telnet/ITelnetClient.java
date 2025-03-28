package org.openvpn.telegram.telnet;

import org.openvpn.telegram.entity.Connection;
import org.openvpn.telegram.entity.ConnectionParams;

public interface ITelnetClient {

    void connect(ConnectionParams connectionParams) throws Exception;

    void disconnect();

    Connection getConnection();

    Boolean isConnected();
}
