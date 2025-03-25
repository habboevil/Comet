package com.cometproject.server.network.messages.outgoing.gamecenter.snowwar;

/*
 * ****************
 * @author capos *
 * ****************
 */

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;

public class UserLeftGameComposer extends MessageComposer {

    private final int playerId;

    public UserLeftGameComposer(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.playerId);
    }

    @Override
    public short getId() {
        return 5011;
    }
}
