package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class UserObjectMessageComposer extends MessageComposer {

    private final IPlayer player;

    public UserObjectMessageComposer(final IPlayer player) {
        this.player = player;
    }

    public static String getDate(int timestamp) {
        final Date d = new Date(timestamp * 1000L);
        final SimpleDateFormat df = new SimpleDateFormat(Locale.getOrDefault("date.format", "MMM d yyyy 'at' HH:mm"));

        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        return df.format(d);
    }

    @Override
    public short getId() {
        return Composers.UserObjectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(player.getId());
        msg.writeString(player.getData().getUsername());
        msg.writeString(player.getData().getFigure());
        msg.writeString(player.getData().getGender().toUpperCase());
        msg.writeString(player.getData().getMotto());
        msg.writeString(player.getData().getUsername().toLowerCase());
        msg.writeBoolean(true);

        msg.writeInt(player.getStats().getRespectPoints()); // ??? (8)
        msg.writeInt(player.getStats().getDailyRespects()); // daily respects!
        msg.writeInt(player.getStats().getScratches()); // (3) pet respects I guess

        msg.writeBoolean(true);
        msg.writeString(getDate(player.getData().getLastVisit()));

        msg.writeBoolean(player.getData().getChangingName()); // can change username
        msg.writeBoolean(false);
        msg.writeInt(!CometSettings.useNewKissSystem ? player.getData().getKisses() : player.getStats().getTotalKisses()); // kisses
    }
}
