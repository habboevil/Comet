package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class WhisperAlertCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        NetworkManager.getInstance().getSessions().broadcast(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), "[NOTIFICATION] " + this.merge(params) + " -" + client.getPlayer().getData().getUsername(), 5));
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getPermission() {
        return "wha_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.message", "%message%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.wha.description");
    }
}