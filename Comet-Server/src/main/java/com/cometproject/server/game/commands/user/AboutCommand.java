package com.cometproject.server.game.commands.user;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.stats.CometStats;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.CometRuntime;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

public class AboutCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        final String serverStatusLabel = Locale.getOrDefault("command.about.server_status", "<h5><b>Server Status</b><br></h5>");
        final String usersOnlineLabel = Locale.getOrDefault("command.about.users_online", "Users online: ");
        final boolean aboutDetailed = client.getPlayer().getPermissions().getRank().aboutDetailed();
        final boolean aboutStats = client.getPlayer().getPermissions().getRank().aboutStats();
        final CometStats cometStats = Comet.getStats();

        StringBuilder about = new StringBuilder(serverStatusLabel);

        if (CometSettings.aboutShowPlayersOnline || aboutDetailed)
            about.append(usersOnlineLabel).append(NumberFormat.getInstance().format(cometStats.getPlayers())).append("<br>");

        if (CometSettings.aboutShowRoomsActive || aboutDetailed)
            about.append(Locale.getOrDefault("command.about.active_rooms", "Active rooms: ")).append(NumberFormat.getInstance().format(cometStats.getRooms())).append("<br>");

        if (CometSettings.aboutShowUptime || aboutDetailed)
            about.append(Locale.getOrDefault("command.about.uptime", "Uptime: ")).append(cometStats.getUptime()).append("<br>");

        about.append("<br>").append(Locale.getOrDefault("command.about.credits_title", "<h5><b>Thanks To:</b></h5>")).append("<b>Leon Hartley (The Creator of Comet) ♥</b><br>" +
                "Skeletor<br>" +
                "Cen0n<br>" +
                "Hax0r<br>" +
                "iNicollas & Djinn<br>" +
                "Fxst and Wire<br>" +
                "DuckieTM<br>" +
                "Matuvs<br>"
        );

        if (aboutDetailed) {
            about.append(Locale.getOrDefault("command.about.server_info", "<br><h5><b>Server Info</b></h5>"));
            about.append(Locale.getOrDefault("command.about.allocated_memory", "Allocated memory: ")).append(NumberFormat.getInstance().format(cometStats.getAllocatedMemory())).append("MB<br>");
            about.append(Locale.getOrDefault("command.about.used_memory", "Used memory: ")).append(NumberFormat.getInstance().format(cometStats.getUsedMemory())).append("MB<br>");

            about.append(Locale.getOrDefault("command.about.proccess_id", "Process ID: ")).append(CometRuntime.processId).append("<br>");
            about.append(Locale.getOrDefault("command.about.os", "OS: ")).append(cometStats.getOperatingSystem()).append("<br>");
            about.append(Locale.getOrDefault("command.about.cpu_cores", "CPU cores:  ")).append(cometStats.getCpuCores()).append("<br>");
            about.append(Locale.getOrDefault("command.about.threads", "Threads:  ")).append(ManagementFactory.getThreadMXBean().getThreadCount()).append("<br>");
        }

        if (aboutStats) {
            about.append(Locale.getOrDefault("command.about.hotel_status", "<br><h5><b>Hotel Stats</b></h5>"));
            about.append(Locale.getOrDefault("command.about.online_record", "Online record: ")).append(GameCycle.getInstance().getOnlineRecord()).append("<br>");
            about.append(Locale.getOrDefault("command.about.last_reboot_record", "Record since last reboot: ")).append(GameCycle.getInstance().getCurrentOnlineRecord()).append("<br><br>");
        }

        client.send(new AdvancedAlertMessageComposer(
                Locale.getOrDefault("command.about.widget_title", "Comet Server"),
                about.toString(),
                Locale.getOrDefault("command.about.link_name", "www.krews.org"),
                Locale.getOrDefault("command.about.link_location", "https://www.krews.org"), CometSettings.aboutImg
        ));
    }

    @Override
    public String getPermission() {
        return "about_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.about.description", "Review the server information.");
    }
}
