package org.openvpn.telegram.telnet;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.openvpn.telegram.telnet.events.TelnetEvent;
import org.openvpn.telegram.telnet.parser.TelnetMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UnprocessCommandReceiver {

    private final TelnetEventManager eventManager;
    private final List<TelnetMessageParser<?>> telnetMessageParsers;

    private final BlockingQueue<String> block;

    private static final Logger logger = LoggerFactory.getLogger(UnprocessCommandReceiver.class);

    public UnprocessCommandReceiver(
            TelnetEventManager eventManager,
            List<TelnetMessageParser<?>> telnetMessageParsers
    ) {
        this.telnetMessageParsers = telnetMessageParsers;
        this.eventManager = eventManager;
        this.block = new LinkedBlockingQueue<>();
    }

    public synchronized void receive(List<String> output) {
        block.addAll(output);
    }

    public synchronized void process() {
        logger.info("Pull unprocessing commands, pull size: {}", block.size());

        var chunk = block.stream().toList();

        for (TelnetMessageParser<?> parser : telnetMessageParsers) {
            TelnetEvent event = parser.parse(chunk);

            if (event != null) {
                eventManager.publish(event);
                logger.info("Event with type {} generated", event.getClass().getSimpleName());

                block.removeAll(chunk);
            }
        }
    }
}
