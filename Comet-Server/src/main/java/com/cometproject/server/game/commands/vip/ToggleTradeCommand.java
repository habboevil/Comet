package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class ToggleTradeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final PlayerEntity entity = client.getPlayer().getEntity();

        if (params.length < 1) {
            entity.getPlayer().getSession().send(new TalkMessageComposer(entity.getId(), Locale.getOrDefault("command.toggle_trade.info", "To activate exchanges, type :exchanges on and to deactivate :exchanges off"), ChatEmotion.NONE, 34));
            return;
        }

        switch (params[0]) {
            default:
            case "on":
                entity.getPlayer().getSession().send(new TalkMessageComposer(entity.getId(), Locale.getOrDefault("command.toggle_trade.enabled", "Exchanges activated."), ChatEmotion.NONE, 34));
                client.getPlayer().getSettings().setAllowTrade(true);
                break;

            case "off":
                entity.getPlayer().getSession().send(new TalkMessageComposer(entity.getId(), Locale.getOrDefault("command.toggle_trade.disabled", "Disabled exchanges."), ChatEmotion.NONE, 34));
                client.getPlayer().getSettings().setAllowTrade(false);
                break;
        }
    }

    @Override
    public String getPermission() {
        return "toggletrade_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.toggle_trade.parameters", "[on|off]");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.toggle_trade.description", "Toggle trade, prevents you from being traded by having the trade off.");
    }
}
