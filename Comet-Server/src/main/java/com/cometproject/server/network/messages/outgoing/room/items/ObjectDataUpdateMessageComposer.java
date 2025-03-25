package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.battleball.outgoing.Outgoing;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

// you didnt include it anywhere
public class ObjectDataUpdateMessageComposer extends MessageComposer {

    private short id = Composers.ItemsDataUpdateMessageComposer;

    private List<RoomItemFloor> items;

    public ObjectDataUpdateMessageComposer(List<RoomItemFloor> items) {
        this.items = items;
    }

    @Override
    public short getId() {
        return id;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.items.size());

        for (RoomItemFloor item : this.items) {
            msg.writeInt(item.getVirtualId());
            item.composeItemData(msg);
        }
    }
}
