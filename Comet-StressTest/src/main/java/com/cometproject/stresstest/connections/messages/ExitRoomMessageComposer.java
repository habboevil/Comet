package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Events;
import com.cometproject.server.protocol.messages.MessageComposer;

public class ExitRoomMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Events.GoToHotelViewMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
