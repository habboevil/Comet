package com.cometproject.server.network.messages.outgoing.nux;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.nuxs.NuxGift;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class NuxGiftSelectionViewMessageComposer extends MessageComposer {
    private static int pageType = 0;

    public NuxGiftSelectionViewMessageComposer(int pageType) {
        NuxGiftSelectionViewMessageComposer.pageType = pageType;
    }

    @Override
    public short getId() {
        return Composers.NewUserGiftMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

        msg.writeInt(1);

        msg.writeInt(1);
        msg.writeInt(3);
        msg.writeInt(CatalogManager.getInstance().getNuxGiftsSelectionView(pageType).size());

        for (NuxGift gift : CatalogManager.getInstance().getNuxGiftsSelectionView(pageType)) {
            msg.writeString(gift.getIcon());
            msg.writeInt(1);
            msg.writeString(gift.getProductData());
            msg.writeString("");
        }
    }
}
