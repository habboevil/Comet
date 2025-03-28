package com.cometproject.server.game.commands.user;

import com.cometproject.api.commands.CommandInfo;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.modules.ModuleManager;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final List<String> list = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, CommandInfo> commandInfoEntry : ModuleManager.getInstance().getEventHandler().getCommands().entrySet()) {
            String commandName = commandInfoEntry.getKey();
            CommandInfo commandInfo = commandInfoEntry.getValue();

            if (client.getPlayer().getPermissions().hasCommand(commandInfo.getPermission()) || commandInfo.getPermission().isEmpty()) {
                list.add(commandName + " - " + commandInfo.getDescription() + "\n\n");
            }
        }

        for (Map.Entry<String, ChatCommand> commandEntry : CommandManager.getInstance().getChatCommands().entrySet()) {
            String commandName = commandEntry.getKey();
            ChatCommand chatCommand = commandEntry.getValue();

            if (chatCommand.isHidden() || !client.getPlayer().getPermissions().hasCommand(chatCommand.getPermission())) {
                continue;
            }

            list.add(commandName.split(",")[0] + " " + chatCommand.getParameter() + " " + chatCommand.getDescription() + "\n\n");
        }

        list.sort(String::compareToIgnoreCase);

        for (String value : list) {
            builder.append(value);
        }

        client.send(new MotdNotificationMessageComposer(Locale.get("command.commands.title") + "\n\n" + builder.toString()));
    }

    @Override
    public String getPermission() {
        return "commands_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.commands.description");
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}