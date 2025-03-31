package org.openvpn.telegram.telnet.listeners;

public class ClientDisconnectListener implements ITelnetEventListener {

    @Override
    public void publish(String message) {
        System.out.println("Client disconnected: " + message);
    }

}
