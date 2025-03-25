package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class FlagUserCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.flaguser.none", "Who do you want to change the name of?"), client);
            return;
        }

        String username = params[0];
        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.flaguser.user_not_found", "User not found: " + username), client);
            return;
        }

        if (user == client) {
            sendNotif(Locale.getOrDefault("command.flaguser.self_flag", "You cannot flag yourself for a name change."), client);
            return;
        }

        if (user != null && user.getPlayer() != null && user.getPlayer().getData() != null) {
            user.getPlayer().getData().setFlaggingUser(true);
            user.getPlayer().getData().setChangingName(true);
            user.send(new UserObjectMessageComposer(user.getPlayer()));
        }
    }

    @Override
    public String getPermission() {
        return "flaguser_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "(username)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.flaguser.description", "Grants the ability to change the username to another user");
    }
}
