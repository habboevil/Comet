package com.cometproject.server.game.commands.staff.fun;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang3.StringUtils;

public class RollCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) return;

        if (!StringUtils.isNumeric(params[0])) {
            return;
        }

        int number = Integer.parseInt(params[0]);

        if (number < 1) number = 1;
        if (number > 6) number = 6;

        client.getPlayer().getEntity().setAttribute("diceRoll", number);

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = "%s rolled the dice in room '%b' and the result was %r"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", client.getPlayer().getEntity().getRoom().getData().getName())
                .replace("%r", Integer.toString(number));
    }

    @Override
    public String getPermission() {
        return "roll_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.number", "%number%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roll.description");
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }

}
