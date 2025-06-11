package org.openvpn.telegram.telnet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnprocessCommandReceiver {

    private final BlockingQueue<String> out;

    private static final Logger logger = LoggerFactory.getLogger(UnprocessCommandReceiver.class);

    public UnprocessCommandReceiver(LinkedBlockingQueue<String> out) {
        this.out = out;
    }

    public synchronized void receive(String command) {
        out.add(command);
    }

    private synchronized void process() {
        logger.info("Pull unprocessing commands, pull size: {}", out.size());

    }
}
