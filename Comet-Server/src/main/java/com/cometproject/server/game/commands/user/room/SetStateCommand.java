package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class SetStateCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendWhisper("You typed the command incorrectly. :state [value] ~ :state -1.", client);
            return;
        }

        if (params[0].equalsIgnoreCase("stop") || params[0].equalsIgnoreCase("-1")) {
            if (client.getPlayer().getEntity().hasAttribute("state.height")) {
                client.getPlayer().getEntity().removeAttribute("state.height");
            }

            sendWhisper("Closed.", client);
            return;
        }

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            try {
                final int state = Integer.parseInt(params[0]);

                if (state < 0 || state > 100) {
                    sendWhisper("Enter a value between 0 and 100.", client);
                    return;
                }

                client.getPlayer().getEntity().setAttribute("state.height", state);

                sendWhisper("Situation, " + state + " is set to.", client);
            } catch (Exception e) {
                sendWhisper("You typed the command incorrectly. :state [value] ~ :state -1.", client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "setstate_command";
    }

    @Override
    public String getParameter() {
        return "(value)";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.setstate.description", "Use this command to change the state of the furniture, available states in a range between 0 and 100.");
    }
}