package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.server.composers.catalog.CatalogPageMessageComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class GetCataPageMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int pageId = msg.readInt();
        int unknown = msg.readInt();
        String mode = msg.readString();

        final ICatalogPage page = CatalogManager.getInstance().getPage(pageId);

        if (page == null || !page.isEnabled()) return;

        client.send(
                new CatalogPageMessageComposer("NORMAL", CatalogManager.getInstance().getPage(pageId), client.getPlayer(), CatalogManager.getInstance())
        );
    }
}
