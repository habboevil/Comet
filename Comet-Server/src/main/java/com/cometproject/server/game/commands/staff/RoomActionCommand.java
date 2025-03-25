package com.cometproject.server.game.commands.staff;

import com.cometproject.api.game.bots.BotMode;
import com.cometproject.api.game.bots.BotType;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.data.PlayerBotData;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;


public class RoomActionCommand extends ChatCommand {

    public final String[] looks = new String[]{

            "lg-3337-92.ha-3331-0.ca-1813-63.ch-3334-93-92.sh-3338-93.hd-180-1",
            "hd-625-1370.ch-630-1408.lg-695-1408.he-3274-1408",
            "hr-893-39.hd-180-1.ch-877-81-1408.lg-275-81.sh-905-1408.he-1601.ea-1401-1408.fa-1204.ca-1806-1408.wa-3211-1408-1408",
            "hr-890-42.hd-625-14.ch-3113-75-64.lg-720-64.sh-3115-75-64.he-1605-74",
            "hr-3090-45.hd-208-8.ch-3030-1408.lg-3023-64.sh-3068-64-64.wa-2009-64",
            "hr-545-35.hd-625-1.ch-655-71.lg-715-64.sh-907-64.he-3274-64.ca-1801-64.wa-2011",
            "hd-185-14.ch-235-1408.lg-3116-85-1408.sh-3115-1408-1408.ha-1002-1408.ca-1805-64",
            "hr-545-40.hd-600-1370.ch-665-1408.lg-715-1408.sh-730-1408",
            "hr-170-35.hd-190-10.ch-267-72.lg-3290-64.sh-3068-1408-72.cp-3125-64",
            "hr-545-45.hd-600-14.ch-650-76.lg-696-64.sh-907-76.he-1602-1408.wa-3210-1408-1408",
            "hr-3090-45.hd-190-1370.ch-225-1408.lg-3023-64.sh-300-64.ha-1006.he-1608.ea-1406.fa-1201.ca-1804-73.wa-2001.cp-3284-66",
            "hd-600-1.ch-630-1408.lg-695-1408",
            "hd-209-1370.ch-255-64.lg-270-1408",
            "hr-530-42.hd-600-1.ch-635-1408.lg-700-1408.sh-740-90.fa-1202-1408",
            "hr-165-39.hd-180-8.ch-255-82.lg-275-1408",
            "hr-890-35.hd-629-8.ch-665-76.lg-696-76.sh-730-64.ha-1003-64",
            "hr-893-39.hd-209-1.ch-267-64.lg-285-64.sh-300-1408",
            "hr-890-45.hd-600-1.ch-665-80.lg-3088-82-82.sh-740-75.he-3274-64",
            "hr-3090-39.hd-209-8.ch-805-64.lg-3088-66-82.sh-305-1408.cc-3294-64-1408",
            "hd-629-8.ch-630-1408.lg-695-1408.sh-730-1408.ca-1812",
            "hr-802-37.hd-185-1.ch-804-82.lg-280-73.sh-3068-1408-1408.wa-2001",
            "hr-515-47.hd-605-10.ch-660-83.lg-3216-85",
            "hr-893-36.hd-208-8.ch-225-73.lg-270-64.sh-300-64.ea-1406.wa-2001",
            "hr-890-38.hd-629-8.ch-685-1408.lg-700-81.he-1602-83.wa-2001",
            "hr-893-42.hd-180-1.ch-808-91.lg-3088-1408-91.sh-300-66.ha-1013-80.fa-1205-91",
            "hr-515-32.hd-600-1.ch-660-1408.lg-715-64",
            "hr-125-42.hd-180-1370.ch-225-64.lg-285-1408.sh-300-64.wa-2001",
            "hr-890-37.hd-605-8.ch-650-76.lg-715-76.sh-907-71.he-3274-71.fa-3276-1408.ca-1812.wa-2008",
            "hr-125-31.hd-209-14.ch-3030-64.lg-275-64.sh-295-64.ha-1020.fa-1201",
            "hr-890-34.hd-629-1.ch-665-73.lg-3088-1408-82.sh-725-1408.ha-3117-1408.fa-3276-72.ca-1815-64"
    };

    public final String[] names = new String[]{
            "Josefin",
            "Ebba",
            "Erik",
            "Adam",
            "Mohammed",
            "Selma",
            "Agda",
            "Twana",
            "Endrit",
            "Skeletor",
            "Susan",
            "Frida",
            "Fanny",
            "Abdi",
            "Simon",
            "Xnz",
            "Elin"
    };

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }

        final String action = params[0];

        switch (action) {
            case "list":
                client.send(new MotdNotificationMessageComposer(
                        "Here is a list of actions that you can use in your room:\n\n" +
                                "- effect\n" +
                                "- say\n" +
                                "- dance\n" +
                                "- sign\n" +
                                "- bots <i>(To delete, type \"minions leave\")</i>\n" +
                                "- handitem\n"
                ));
                break;
            case "effect":
                if (!StringUtils.isNumeric(params[1])) {
                    return;
                }

                int effectId = Integer.parseInt(params[1]);

                for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.applyEffect(new PlayerEffect(effectId, 0));
                }
                break;

            case "say":
                String msg = this.merge(params, 1);

                for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.getRoom().getEntities().broadcastMessage(new TalkMessageComposer(playerEntity.getId(), msg, RoomManager.getInstance().getEmotions().getEmotion(msg), 0));
                }
                break;

            case "dance":
                if (!StringUtils.isNumeric(params[1])) {
                    return;
                }

                int danceId = Integer.parseInt(params[1]);

                for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.setDanceId(danceId);
                    playerEntity.getRoom().getEntities().broadcastMessage(new DanceMessageComposer(playerEntity.getId(), danceId));
                }
                break;

            case "sign":
                if (!StringUtils.isNumeric(params[1])) {
                    return;
                }

                for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.addStatus(RoomEntityStatus.SIGN, String.valueOf(params[1]));

                    playerEntity.markDisplayingSign();
                    playerEntity.markNeedsUpdate();
                }
                break;

            case "bots":
                if (!StringUtils.isNumeric(params[1])) {
                    return;
                }

                int count = Integer.parseInt(params[1]);
                final Position entityPosition = client.getPlayer().getEntity().getPosition();

                if (count > 1000) {
                    count = 1000;
                } else if (count < 0) {
                    count = 1;
                }

                List<RoomEntity> addedEntities = Lists.newArrayList();

                for (int i = 0; i < count; i++) {
                    final int id = -(i + 1);
                    final String username = this.getRandomName();
                    final String motto = "";

                    BotData botData = new PlayerBotData(
                            id,
                            username,
                            motto,
                            this.getRandomLook(),
                            client.getPlayer().getData().getGender(),
                            this.getRandomName(),
                            client.getPlayer().getId(),
                            "[]",
                            false,
                            false,
                            7,
                            BotType.MIMIC,
                            BotMode.DEFAULT, "");

                    final BotEntity botEntity = client.getPlayer().getEntity().getRoom().getBots().addBot(botData,
                            entityPosition.getX(), entityPosition.getY(), entityPosition.getZ());

                    if (botEntity != null) {
                        addedEntities.add(botEntity);
                    }
                }

                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new AvatarsMessageComposer(addedEntities));
                break;

            case "handitem":
                int handItem = Integer.parseInt(params[1]);

                for (PlayerEntity playerEntity : client.getPlayer().getEntity().getRoom().getEntities().getPlayerEntities()) {
                    playerEntity.carryItem(handItem, false);
                }

                break;
        }
    }

    public String getRandomLook() {
        int random = new Random().nextInt(this.looks.length);
        return this.looks[random];
    }

    public String getRandomName() {
        int random = new Random().nextInt(this.names.length);
        return this.names[random];
    }


    @Override
    public String getPermission() {
        return "roomaction_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.roomaction.description");
    }

}
