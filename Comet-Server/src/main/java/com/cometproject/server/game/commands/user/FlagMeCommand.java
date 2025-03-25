package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class FlagMeCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        if (message.length != 1) {
            String command = Locale.get("command.flagme.name");
            String yes = Locale.getOrDefault("command.flagme.yes", "yes");
            sendNotif(Locale.getOrDefault("command.flagme.none", String.format("Type: %s %s'", command, yes)), client);
        } else if (client != null && client.getPlayer() != null && client.getPlayer().getData() != null) {
            String command = Locale.get("command.flagme.name");
            String yes = Locale.getOrDefault("command.flagme.yes", "yes");

            if (!message[0].equals(yes)) {
                sendNotif(Locale.getOrDefault("command.flagme.incorrect", String.format("To continue, type: %s %s'", command, yes)), client);
            } else if (!client.getPlayer().getData().getChangingName()) {
                client.getPlayer().getData().setChangingName(true);
                client.send(new UserObjectMessageComposer(client.getPlayer()));
                sendNotif(Locale.getOrDefault("command.flagme.bought", "You can change your username by clicking on yourself in a room and then click on 'Change your name'. You will be banned if your new name violates the rules!"), client);
            } else {
                sendNotif(Locale.getOrDefault("command.flagme.alreadybought", "Hey! You have entered this command already, click on your avatar and after that on the button 'Change your name'."), client);
            }
        }
    }

    @Override
    public String getPermission() {
        return "flagme_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.flagme.description", "Get the ability to change your username");
    }
}
