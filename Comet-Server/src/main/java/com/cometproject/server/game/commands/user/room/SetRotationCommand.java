package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class SetRotationCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendWhisper("You typed the command incorrectly: :rotation [value] ~ :rotation -1.", client);
            return;
        }

        if (params[0].equalsIgnoreCase("stop") || params[0].equalsIgnoreCase("-1")) {
            if (client.getPlayer().getEntity().hasAttribute("rotation.height")) {
                client.getPlayer().getEntity().removeAttribute("rotation.height");
                sendWhisper("Rotation off.", client);
                return;
            }

            sendWhisper("Rotation off.", client);
            return;
        }

        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            try {
                final int rotation = Integer.parseInt(params[0]);

                if (rotation < 0 || rotation > 7) {
                    sendWhisper("Enter a value between 0 and 7. To close, type -1 in the value field.", client);
                    return;
                }

                client.getPlayer().getEntity().setAttribute("rotation.height", rotation);

                sendWhisper("Lap " + rotation + " has been configured.", client);
            } catch (Exception e) {
                sendWhisper("You typed the command incorrectly: :rotation [value] ~ :rotation -1.", client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "setrot_command";
    }

    @Override
    public String getParameter() {
        return "(number)";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.setrot.description", "Use this command to rotate the furniture, the available rotations are in a range between 0 and 7.");
    }
}