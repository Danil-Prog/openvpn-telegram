package org.openvpn.telegram.telnet;

import org.openvpn.telegram.entity.Connection;

import java.io.BufferedReader;
import java.io.OutputStream;

public interface ITelnetClient {

    void connect();

    void disconnect();

    Connection getConnection();

    Boolean isConnected();

    BufferedReader getStreamReader();

    OutputStream getStreamWriter();
}
