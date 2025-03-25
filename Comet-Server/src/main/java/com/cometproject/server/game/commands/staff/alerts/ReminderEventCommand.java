package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class ReminderEventCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        NetworkManager.getInstance().getSessions().broadcast(new TalkMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("none.ishere", "There is a new event created by %username%, click <a href='event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId() + "'><b>here</b></a> to go to the event.").replace("%message%", this.merge(params)).replace("%username%", client.getPlayer().getData().getUsername()) + "<br><br><i> " + client.getPlayer().getData().getUsername() + "</i>", ChatEmotion.NONE, 34));
    }

    @Override
    public String getPermission() {
        return "reminder_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reminder.description");
    }
}
