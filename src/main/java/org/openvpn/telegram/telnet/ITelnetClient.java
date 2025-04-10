package org.openvpn.telegram.telnet;

import java.io.BufferedReader;
import java.io.OutputStream;

public interface ITelnetClient {

    void connect();

    void disconnect();

    Boolean isConnected();

    BufferedReader getStreamReader();

    OutputStream getStreamWriter();
}
