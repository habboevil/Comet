package com.cometproject.server.game.catalog.purchase.handlers.items;

import com.cometproject.api.game.catalog.types.ICatalogBundledItem;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.game.catalog.purchase.IPurchaseHandler;

public class DefaultCatalogHandler extends BasicItemCatalogHandler implements IPurchaseHandler {

    public void purchase(ICatalogItem item, int itemId, ISession client, int amount, ICatalogPage page, GiftData giftData, FurnitureDefinition definition, ICatalogBundledItem bundledItem, String data) {
        String extraData = "";
        super.execute(item, client, amount, page, giftData, definition, bundledItem, data, extraData);
    }

    @Override
    public boolean canPurchase(ICatalogItem item, ISession client) {
        return true;
    }
}
