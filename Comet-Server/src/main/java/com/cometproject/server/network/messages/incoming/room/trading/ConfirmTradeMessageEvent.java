package com.cometproject.server.network.messages.incoming.room.trading;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.pin.EmailVerificationWindowMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ConfirmTradeMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        Trade trade = client.getPlayer().getEntity().getRoom().getTrade().get(client.getPlayer().getEntity());

        if (trade == null) {
            return;
        }

        trade.confirm(trade.getUserNumber(client.getPlayer().getEntity()));
    }
}
