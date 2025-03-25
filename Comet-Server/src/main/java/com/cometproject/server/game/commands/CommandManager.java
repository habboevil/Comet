package com.cometproject.server.game.commands;

import com.cometproject.api.commands.CommandInfo;
import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.api.config.CometSettings;
import com.cometproject.api.config.Configuration;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.development.*;
import com.cometproject.server.game.commands.gimmicks.*;
import com.cometproject.server.game.commands.notifications.NotificationManager;
import com.cometproject.server.game.commands.staff.*;
import com.cometproject.server.game.commands.staff.alerts.*;
import com.cometproject.server.game.commands.staff.banning.*;
import com.cometproject.server.game.commands.staff.bundles.BundleCommand;
import com.cometproject.server.game.commands.staff.bundles.CloneRoomCommand;
import com.cometproject.server.game.commands.staff.cache.ReloadCommand;
import com.cometproject.server.game.commands.staff.fun.BubbleCommand;
import com.cometproject.server.game.commands.staff.fun.RollCommand;
import com.cometproject.server.game.commands.staff.muting.MuteCommand;
import com.cometproject.server.game.commands.staff.muting.RoomMuteCommand;
import com.cometproject.server.game.commands.staff.muting.UnmuteCommand;
import com.cometproject.server.game.commands.staff.rewards.*;
import com.cometproject.server.game.commands.staff.rewards.mass.*;
import com.cometproject.server.game.commands.staff.rewards.room.RoomMassCoinsCommand;
import com.cometproject.server.game.commands.staff.rewards.room.RoomMassPointsCommand;
import com.cometproject.server.game.commands.staff.rewards.room.RoomMassSeasonalCommand;
import com.cometproject.server.game.commands.staff.security.LogsClientCommand;
import com.cometproject.server.game.commands.user.*;
import com.cometproject.server.game.commands.user.group.DeleteGroupCommand;
import com.cometproject.server.game.commands.user.group.EjectAllCommand;
import com.cometproject.server.game.commands.user.muting.MuteBotsCommand;
import com.cometproject.server.game.commands.user.muting.MutePetsCommand;
import com.cometproject.server.game.commands.user.muting.MuteUsersCommand;
import com.cometproject.server.game.commands.user.room.*;
import com.cometproject.server.game.commands.user.settings.*;
import com.cometproject.server.game.commands.user.ws.TwitchCommand;
import com.cometproject.server.game.commands.user.ws.YoutubeCommand;
import com.cometproject.server.game.commands.vip.*;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.CommandLogEntry;
import com.cometproject.server.modules.ModuleManager;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommandManager implements Initialisable {
    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(CommandManager.class.getName());
    private static CommandManager commandManagerInstance;
    private final ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(Configuration.currentConfig().get("comet.system.commandExecutorThreads")));
    private NotificationManager notifications;
    private Map<String, ChatCommand> commands;


    private CommandManager() {

    }

    public static CommandManager getInstance() {
        if (commandManagerInstance == null) {
            commandManagerInstance = new CommandManager();
        }

        return commandManagerInstance;
    }

    public static String[] getParams(String[] splitStr) {
        final String[] a = new String[splitStr.length - 1];

        for (int i = 0; i < splitStr.length; i++) {
            if (i == 0) continue;

            a[i - 1] = splitStr[i];
        }

        return a;
    }

    @Override
    public void initialize() {
        this.commands = new HashMap<>();

        this.reloadAllCommands();
        log.info("Loaded " + commands.size() + " chat commands");

        this.notifications = new NotificationManager();
        log.info("CommandManager initialized");
    }

    public void reloadAllCommands() {
        this.commands.clear();

        this.loadUserCommands();
        this.loadStaffCommands();
        this.notifications = new NotificationManager();

        // Staff commands only
        this.addCommand("instancestats", new InstanceStatsCommand());
        this.addCommand("roomgrid", new RoomGridCommand());
        this.addCommand("pos", new PositionCommand());
        this.addCommand("itemdata", new ItemDataCommand());
        this.addCommand("itemid", new ItemVirtualIdCommand());
    }

    private void loadUserCommands() {
        this.addCommand(Locale.get("command.commands.name"), new CommandsCommand());
        this.addCommand(Locale.get("command.mimicoff.name"), new MimicOfflineCommand());
        this.addCommand(Locale.get("command.about.name"), new AboutCommand());
        this.addCommand(Locale.get("command.pickall.name"), new PickAllCommand());
        this.addCommand(Locale.get("command.ejectall.name"), new EjectAllCommand());
        this.addCommand(Locale.get("command.empty.name"), new EmptyCommand());
        this.addCommand(Locale.get("command.sit.name"), new SitCommand());
        this.addCommand(Locale.get("command.lay.name"), new LayCommand());
        this.addCommand(Locale.get("command.stand.name"), new StandCommand());
        this.addCommand(Locale.get("command.home.name"), new HomeCommand());
        this.addCommand(Locale.get("command.setmax.name"), new SetMaxCommand());
        this.addCommand(Locale.get("command.position.name"), new PositionCommand());
        this.addCommand(Locale.get("command.deletegroup.name"), new DeleteGroupCommand());
        this.addCommand(Locale.get("command.togglefriends.name"), new ToggleFriendsCommand());
        this.addCommand(Locale.get("command.enablecommand.name"), new EnableCommand());
        this.addCommand(Locale.get("command.disablecommand.name"), new DisableCommand());
        this.addCommand(Locale.get("command.pyramid.name"), new PyramidCommand());
        this.addCommand(Locale.get("command.screenshot.name"), new ScreenshotCommand());
        this.addCommand(Locale.get("command.logsclient.name"), new LogsClientCommand());
        this.addCommand(Locale.get("command.look.name"), new LookCommand());
        this.addCommand(Locale.get("command.colour.name"), new ColourCommand());
        this.addCommand(Locale.get("command.flagme.name"), new FlagMeCommand());
        this.addCommand(Locale.get("command.flaguser.name"), new FlagUserCommand());
        this.addCommand(Locale.get("command.randomize.name"), new RandomizeCommand());
        this.addCommand(Locale.get("command.emptypets.name"), new EmptyPetsCommand());
        this.addCommand(Locale.get("command.emptybots.name"), new EmptyBotsCommand());
        this.addCommand(Locale.get("command.mutebots.name"), new MuteBotsCommand());
        this.addCommand(Locale.get("command.mutepets.name"), new MutePetsCommand());
        this.addCommand(Locale.get("command.muteusers.name"), new MuteUsersCommand());
        this.addCommand(Locale.get("command.toggleevents.name"), new ToggleEventsCommand());
        this.addCommand(Locale.get("command.emptyfriends.name"), new EmptyFriendsCommand());
        this.addCommand(Locale.get("command.setz.name"), new SetZCommand());
        this.addCommand(Locale.get("command.setrot.name"), new SetRotationCommand());
        this.addCommand(Locale.get("command.setstate.name"), new SetStateCommand());
        this.addCommand(Locale.get("command.override.name"), new OverrideCommand());
        this.addCommand(Locale.get("command.updatefloor.name"), new UpdateFloorCommand());
        this.addCommand(Locale.get("command.brb.name"), new BrbCommand());
        this.addCommand(Locale.get("command.setidletimer.name"), new SetIdleTimerCommand());
        this.addCommand(Locale.get("command.push.name"), new PushCommand());
        this.addCommand(Locale.get("command.pull.name"), new PullCommand());
        this.addCommand(Locale.get("command.moonwalk.name"), new MoonwalkCommand());
        this.addCommand(Locale.get("command.enable.name"), new EffectCommand());
        this.addCommand(Locale.get("command.setspeed.name"), new SetSpeedCommand());
        this.addCommand(Locale.get("command.mimic.name"), new MimicCommand());
        this.addCommand(Locale.get("command.transform.name"), new TransformCommand());
        this.addCommand(Locale.get("command.noface.name"), new NoFaceCommand());
        this.addCommand(Locale.get("command.follow.name"), new FollowCommand());
        this.addCommand(Locale.get("command.superpull.name"), new SuperPullCommand());
        this.addCommand(Locale.get("command.mentionsettings.name"), new MentionSettingsCommand());
        this.addCommand(Locale.get("command.handitem.name"), new HandItemCommand());
        this.addCommand(Locale.get("command.togglediagonal.name"), new DiagonalCommand());
        this.addCommand(Locale.get("command.fastwalk.name"), new FastWalkCommand());
        this.addCommand(Locale.get("command.hidewired.name"), new HideWiredCommand());
        this.addCommand(Locale.get("command.disablewhisper.name"), new DisableWhisperCommand());
        this.addCommand(Locale.get("command.disablehotelalert.name"), new DisableHotelAlertCommand());
        this.addCommand(Locale.get("command.disablehotelalertlinks.name"), new DisableHotelAlertLinksCommand());
        this.addCommand(Locale.get("command.namecolour.name"), new NameColourCommand());
        // this.addCommand(Locale.getOrDefault("command.emoji.name", "emoji"), new EmojiCommand());
        this.addCommand(Locale.get("command.bubble.name"), new BubbleCommand());
        this.addCommand(Locale.get("command.clearscore.name"), new ClearHighscoreCommand());
        this.addCommand(Locale.get("command.warpbot.name"), new WarpBotCommand());
        this.addCommand(Locale.get("command.superwired.name"), new SuperWiredCommand());
        this.addCommand(Locale.get("command.mentions.name"), new MentionsCommand());
        this.addCommand(Locale.get("command.disablemimic.name"), new DisableMimicCommand());
        this.addCommand(Locale.get("command.pathfinder.name"), new FutnitroCommand());
        this.addCommand(Locale.get("command.kiss.name"), new KissCommand());
        this.addCommand(Locale.get("command.hug.name"), new HugCommand());
        this.addCommand(Locale.get("command.online.name"), new OnlineCommand());
        this.addCommand(Locale.get("command.disabledcommands.name"), new ListDisabledCommandsCommand());
        this.addCommand(Locale.get("command.toggleshoot.name"), new ToggleShootCommand());
        this.addCommand(Locale.get("command.bell.name"), new BellCommand());
    }

    private void loadStaffCommands() {
        this.addCommand(Locale.get("command.warp.name"), new WarpCommand());
        this.addCommand(Locale.get("command.tradeban.name"), new TradeBanCommand());
        this.addCommand(Locale.get("command.notilook.name"), new NotificationLookCommand());
        this.addCommand(Locale.get("command.teleport.name"), new TeleportCommand());
        this.addCommand(Locale.get("command.massmotd.name"), new MassMotdCommand());
        this.addCommand(Locale.get("command.hotelalert.name"), new HotelAlertCommand());
        this.addCommand(Locale.get("command.invisible.name"), new InvisibleCommand());
        this.addCommand(Locale.get("command.superban.name"), new SuperBanCommand());
        this.addCommand(Locale.get("command.ban.name"), new BanCommand());
        this.addCommand(Locale.get("command.unban.name"), new UnBanCommand());
        this.addCommand(Locale.get("command.kick.name"), new KickCommand());
        this.addCommand(Locale.get("command.disconnect.name"), new DisconnectCommand());
        this.addCommand(Locale.get("command.ipban.name"), new IpBanCommand());
        this.addCommand(Locale.get("command.alert.name"), new AlertCommand());
        this.addCommand(Locale.get("command.roomalert.name"), new RoomAlertCommand());
        this.addCommand(Locale.get("command.givebadge.name"), new GiveBadgeCommand());
        this.addCommand(Locale.get("command.removebadge.name"), new RemoveBadgeCommand());
        this.addCommand(Locale.get("command.roomkick.name"), new RoomKickCommand());
        this.addCommand(Locale.get("command.coins.name"), new CoinsCommand());
        this.addCommand(Locale.get("command.points.name"), new PointsCommand());
        this.addCommand(Locale.get("command.duckets.name"), new DucketsCommand());
        this.addCommand(Locale.get("command.unload.name"), new UnloadCommand(false));
        this.addCommand(Locale.get("command.reloadroom.name"), new UnloadCommand(true));
        this.addCommand(Locale.get("command.roommute.name"), new RoomMuteCommand());
        this.addCommand(Locale.get("command.reload.name"), new ReloadCommand());
        this.addCommand(Locale.get("command.dance.name"), new DanceCommand());
        this.addCommand(Locale.get("command.maintenance.name"), new MaintenanceCommand());
        this.addCommand(Locale.get("command.roomaction.name"), new RoomActionCommand());
        this.addCommand(Locale.get("command.eventalert.name"), new EventAlertCommand());
        this.addCommand(Locale.get("command.machineban.name"), new MachineBanCommand());
        this.addCommand(Locale.get("command.makesay.name"), new MakeSayCommand());
        this.addCommand(Locale.get("command.mute.name"), new MuteCommand());
        this.addCommand(Locale.get("command.unmute.name"), new UnmuteCommand());
        this.addCommand(Locale.get("command.masscoins.name"), new MassCoinsCommand());
        this.addCommand(Locale.get("command.massbadge.name"), new MassBadgeCommand());
        this.addCommand(Locale.get("command.massduckets.name"), new MassDucketsCommand());
        this.addCommand(Locale.get("command.masspoints.name"), new MassPointsCommand());
        this.addCommand(Locale.get("command.playerinfo.name"), new PlayerInfoCommand());
        this.addCommand(Locale.get("command.roombadge.name"), new RoomBadgeCommand());
        this.addCommand(Locale.get("command.shutdown.name"), new ShutdownCommand());
        this.addCommand(Locale.get("command.summon.name"), new SummonCommand());
        this.addCommand(Locale.get("command.hotelalertlink.name"), new HotelAlertLinkCommand());
        this.addCommand(Locale.get("command.gotoroom.name"), new GotoRoomCommand());
        this.addCommand(Locale.get("command.notification.name"), new NotificationCommand());
        this.addCommand(Locale.get("command.quickpoll.name"), new QuickPollCommand());
        this.addCommand(Locale.get("command.roomoption.name"), new RoomOptionCommand());
        this.addCommand(Locale.get("command.massfreeze.name"), new MassFreezeCommand());
        this.addCommand(Locale.get("command.masswarp.name"), new MassWarpCommand());
        this.addCommand(Locale.get("command.listen.name"), new ListenCommand());
        this.addCommand(Locale.get("command.staffalert.name"), new StaffAlertCommand());
        this.addCommand(Locale.get("command.staffinfo.name"), new StaffInfoCommand());
        this.addCommand(Locale.get("command.roomnotification.name"), new RoomNotificationCommand());
        this.addCommand(Locale.get("command.youtube.name"), new YoutubeCommand());
        this.addCommand(Locale.get("command.twitch.name"), new TwitchCommand());
        this.addCommand(Locale.get("command.closedice.name"), new CloseDiceCommand());
        this.addCommand(Locale.get("command.publicroom.name"), new PublicRoomCommand());
        this.addCommand(Locale.get("command.roommmasscoins.name"), new RoomMassCoinsCommand());
        this.addCommand(Locale.get("command.roommmasspoints.name"), new RoomMassPointsCommand());
        this.addCommand(Locale.get("command.roommass.seasonal.name"), new RoomMassSeasonalCommand());
        this.addCommand(Locale.get("command.advban.name"), new AdvBanCommand());
        this.addCommand(Locale.get("command.softban.name"), new SoftBanCommand());
        this.addCommand(Locale.get("command.masseffect.name"), new MassEffectCommand());
        this.addCommand(Locale.get("command.masshanditem.name"), new MassHandItemCommand());
        this.addCommand(Locale.get("command.freeze.name"), new FreezeCommand());
        this.addCommand(Locale.get("command.unfreeze.name"), new UnfreezeCommand());
        this.addCommand(Locale.get("command.eventreward.name"), new EventRewardCommand());
        this.addCommand(Locale.get("command.eventwon.name"), new EventWonCommand());
        this.addCommand(Locale.get("command.viewinventory.name"), new ViewInventoryCommand());
        this.addCommand(Locale.get("command.giverank.name"), new GiveRankCommand());
        this.addCommand(Locale.get("command.furnifix.name"), new FurniFixCommand());
        this.addCommand(Locale.get("command.welcome.name"), new WelcomeCommand());
        this.addCommand(Locale.get("command.whisperalert.name"), new WhisperAlertCommand());
        this.addCommand(Locale.get("command.nospam.name"), new NoSpamCommand());
        this.addCommand(Locale.get("command.me.name"), new MeCommand());
        this.addCommand(Locale.get("command.reminderevent.name"), new ReminderEventCommand());
        this.addCommand(Locale.get("command.senduser.name"), new SendUserCommand());
        this.addCommand(Locale.get("command.toggletrade.name"), new ToggleTradeCommand());
        this.addCommand(Locale.get("command.command.prefix.nameaddfilter.name"), new FilterWordsCommand());
        this.addCommand(Locale.get(""), new PrefixCommand());
        this.addCommand(Locale.get("command.ignore_invites.name"), new IgnoreInvitesCommand());

        // Room bundles
        this.addCommand(Locale.get("command.bundle.name"), new BundleCommand());
        this.addCommand(Locale.get("command.cloneroom.name"), new CloneRoomCommand());

        // Fun
        this.addCommand(Locale.get("command.roll.name"), new RollCommand());
    }

    public boolean isCommand(String message) {
        if (message.length() <= 1) return false;

        if (message.startsWith(" ")) return false;

        String executor = message.split(" ")[0].toLowerCase();

        if (executor.startsWith(" ")) {
            executor = executor.substring(1);
        }

        final boolean isCommand = executor.equals(":" + Locale.get("command.commands.name")) || commands.containsKey(executor.substring(1)) || ModuleManager.getInstance().getEventHandler().getCommands().containsKey(executor);

        if (!isCommand) {
            for (final String keys : this.commands.keySet()) {
                final List<String> keyList = Lists.newArrayList(keys.split(","));

                if (keyList.contains(executor)) {
                    return true;
                }
            }
        }

        return isCommand;
    }

    public boolean parse(String message, Session client) {
        final String executor = message.split(" ")[0].toLowerCase();
        final ChatCommand chatCommand = this.get(executor);

        if (message.startsWith(" "))
            return false;

        final CommandInfo moduleCommandInfo = ModuleManager.getInstance().getEventHandler().getCommands().get(executor);
        final String commandName = chatCommand == null ? (moduleCommandInfo != null ? moduleCommandInfo.getPermission() : null) : chatCommand.getPermission();

        if (commandName == null) {
            return false;
        }

        if (!PermissionsManager.getInstance().getCommands().containsKey(commandName)) {
            return false;
        }

        final boolean userIsVip = client.getPlayer().getData().getRank() == CometSettings.vipRank || client.getPlayer().getData().getRank() >= CometSettings.rankCanSeeVipContent;
        final boolean userHasCommand = client.getPlayer().getPermissions().hasCommand(commandName);
        final boolean commandIsVipOnly = PermissionsManager.getInstance().getCommands().get(commandName).isVipOnly();

        if ((userHasCommand && !commandIsVipOnly) || (commandIsVipOnly && userIsVip) || commandName.equals("")) {
            if (client.getPlayer().getEntity().getRoom().getData().getDisabledCommands().contains(executor)) {
                ChatCommand.sendNotif(Locale.get("command.disabled"), client);
                return true;
            }
            final String[] params = getParams(message.split(" "));

            if (chatCommand == null) {
                ModuleManager.getInstance().getEventHandler().handleCommand(client, executor, params);
            } else {
                if (chatCommand.isAsync()) {
                    this.executorService.submit(new ChatCommand.Execution(chatCommand, params, client));
                } else {
                    chatCommand.execute(client, params);
                }
            }

            try {
                if (LogManager.ENABLED) {
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new CommandLogEntry(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getId(), message));

                    if (chatCommand != null && CometExternalSettings.enableStaffMessengerLogs && client.getPlayer().getData().getRank() >= Integer.parseInt(Locale.getOrDefault("logchat.minrank", "10")) && chatCommand.isLoggable()) {
                        for (final Session player : ModerationManager.getInstance().getLogChatUsers()) {
                            player.send(new InstantChatMessageComposer(chatCommand.getLoggableDescription(), Integer.MAX_VALUE - 1));
                        }
                    }
                }
            } catch (Exception ignored) {

            }

            return true;
        } else {
            if (commandIsVipOnly) {
                ChatCommand.sendNotif(Locale.get("command.vip"), client);
            }

            return false;
        }
    }

    private ChatCommand get(final String executor) {
        if (this.commands.containsKey(executor))
            return this.commands.get(executor);

        for (final String keys : this.commands.keySet()) {
            final List<String> keyList = Lists.newArrayList(keys.split(","));

            if (keyList.contains(executor)) {
                return this.commands.get(keys);
            }
        }

        return null;
    }

    private void addCommand(String executor, ChatCommand command) {
        final List<String> keyList = Lists.newArrayList(executor.split(","));

        for (final String key : keyList) {
            this.commands.put(":" + key, command);
        }
    }

    public NotificationManager getNotifications() {
        return notifications;
    }

    public Map<String, ChatCommand> getChatCommands() {
        return this.commands;
    }
}
