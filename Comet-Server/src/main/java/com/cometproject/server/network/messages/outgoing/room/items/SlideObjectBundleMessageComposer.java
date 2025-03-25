package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.game.utilities.Position;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollerFloorItem;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class SlideObjectBundleMessageComposer extends MessageComposer {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(RollerFloorItem.class.getName());
    private final Position from;
    private final Position to;
    private final int rollerItemId;
    private final int avatarId;
    private final Map<Integer, Double> items;

    public SlideObjectBundleMessageComposer(Position from, Position to, int rollerItemId, int avatarId, int itemId) {
        this.from = from;
        this.to = to;
        this.rollerItemId = rollerItemId;
        this.avatarId = avatarId;
        this.items = null;
    }

    public SlideObjectBundleMessageComposer(Position from, Position to, int rollerItemId, int avatarId, Map<Integer, Double> items) {
        this.from = from;
        this.to = to;
        this.rollerItemId = rollerItemId;
        this.avatarId = avatarId;
        this.items = items;
    }

    @Override
    public short getId() {
        return Composers.SlideObjectBundleMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

        msg.writeInt(from.getX());
        msg.writeInt(from.getY());

        msg.writeInt(to.getX());
        msg.writeInt(to.getY());

        if (this.items == null) {
            msg.writeInt(0);
        } else {
            msg.writeInt(this.items.size());

            if (!this.items.isEmpty()) {
                for (final Map.Entry<Integer, Double> item : this.items.entrySet()) {
                    msg.writeInt(item.getKey());
                    msg.writeDouble(item.getValue());
                    msg.writeDouble(item.getValue());
                }
            }
        }

        msg.writeInt(this.rollerItemId);

        if (this.avatarId != 0) {
            msg.writeInt(2); // 1 = mv, 2 = std
            msg.writeInt(this.avatarId);
            msg.writeDouble(from.getZ());
            msg.writeDouble(to.getZ());
        }
    }
}