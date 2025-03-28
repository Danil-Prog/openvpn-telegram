package org.openvpn.telegram.telnet.event;

public class ClientConnectListener implements ITelnetEventListener {

    @Override
    public void publish(String message) {
        System.out.println("Client connected: " + message);
    }

}
