package org.openvpn.telegram.telnet;

import org.openvpn.telegram.entity.Connection;

import java.io.InputStream;

public interface ITelnetClient {

    void connect();

    void disconnect();

    Connection getConnection();

    Boolean isConnected();

    InputStream getInputStream();
}
