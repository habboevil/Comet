package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.api.game.players.data.types.IWardrobeItem;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.players.components.types.settings.WardrobeItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.List;

public class SaveWardrobeMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int slot = msg.readInt();
        String figure = msg.readString();
        final String gender = msg.readString();

        if (figure.isEmpty() || gender.isEmpty()) return;

        if (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
            return;
        }

        List<IWardrobeItem> wardrobe = client.getPlayer().getSettings().getWardrobe();
        boolean wardrobeUpdated = false;
        for (IWardrobeItem item : wardrobe) {
            if (item.getSlot() == slot) {
                item.setFigure(figure);
                item.setGender(gender);

                wardrobeUpdated = true;
                break;
            }
        }

        if (!wardrobeUpdated) {
            wardrobe.add(new WardrobeItem(slot, gender, figure));
        }

        client.getPlayer().getSettings().setWardrobe(wardrobe);
        PlayerDao.saveWardrobe(JsonUtil.getInstance().toJson(wardrobe), client.getPlayer().getId());
    }
}