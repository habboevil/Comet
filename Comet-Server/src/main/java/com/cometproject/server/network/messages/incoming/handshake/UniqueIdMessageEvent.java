package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UniqueIdMessageEvent implements Event {
    private static final int HASH_LENGTH = 64;

    private final Logger log = LogManager.getLogger(UniqueIdMessageEvent.class.getName());

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String storedMachineId = msg.readString();
        String clientFingerprint = msg.readString();
        String capabilities = msg.readString();

        if (storedMachineId.length() > HASH_LENGTH) {
            storedMachineId = storedMachineId.substring(0, HASH_LENGTH);
        }

        client.setUniqueId(storedMachineId);
    }
}
