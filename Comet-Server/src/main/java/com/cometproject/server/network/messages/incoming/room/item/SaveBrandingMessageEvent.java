package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

//TODO: Fix and add info terminals too.
public class SaveBrandingMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int brandingId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }

        if ((!room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().roomFullControl())) {
            return;
        }

        int length = msg.readInt();
        StringBuilder data = new StringBuilder("state" + (char) 9 + "0");

        for (int i = 1; i <= length; i++) {
            data.append((char) 9).append(msg.readString());
        }

        //data = new StringBuilder(data.toString().replace("https", "http"));

        RoomItemFloor item = room.getItems().getFloorItem(brandingId);

        if (item == null) {
            return;
        }

        if (item.getDefinition().isAdFurni() && !client.getPlayer().getPermissions().getRank().canControlMpu()) {
            return;
        }

        item.getItemData().setData(data.toString());

        item.sendUpdate();
        item.saveData();
    }
}