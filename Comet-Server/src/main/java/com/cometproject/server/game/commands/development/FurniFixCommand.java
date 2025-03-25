package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import org.apache.commons.lang3.StringUtils;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.FurniFixDao;

public class FurniFixCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }
        final PlayerEntity playerEntity = client.getPlayer().getEntity();
        RoomItemFloor floorItem = playerEntity.getTile().getTopItemInstance();
        if (floorItem == null) {
            client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "To fix or arrange a furni you must be on the furni you want to arrange.", ChatEmotion.NONE, 34));
            return;
        }
        final long itemId = floorItem.getItemData().getItemId();

        String type = params[0].toLowerCase();
        String option = params[1];
        String name = this.merge(params, 1);
        switch (type) {
            case "name":
                FurniFixDao.changeName(name, itemId);
                client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes, collect the item and put it on again.", ChatEmotion.NONE, 34));
                ItemManager.getInstance().loadItemDefinitions();
                break;

            case "cansit":
                if (option.equals("1") || option.equals("0")) {
                    FurniFixDao.canSit(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes, collect the item and put it on again.", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter only a value between 1 and 0.", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "canwalk":
                if (option.equals("1") || option.equals("0")) {
                    FurniFixDao.isWalkable(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes, collect the item and put it on again.", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter only a value between 1 and 0.", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "width":
                if (StringUtils.isNumeric(option)) {
                    FurniFixDao.changeWidth(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes pick up the item and put it back.", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter only a numeric value, not letters.", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "length":
                if (StringUtils.isNumeric(option)) {
                    FurniFixDao.changeLength(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes pick up the item and put it back.", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter only a numeric value, not letters.", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "interaction":
                if (!StringUtils.isNumeric(option)) {
                    FurniFixDao.changeInteractionType(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes pick up the item and put it back.", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter a valid interaction type.", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "interactioncount":
                if (StringUtils.isNumeric(option)) {
                    FurniFixDao.changeInteractionCount(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes pick up the item and put it back", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter a valid numerical value, NOT DECIMALS!", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "stackheight":
                try {
                    double options = Double.parseDouble(option);
                    FurniFixDao.changeStackHeight(options, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes pick up the item and put it back", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } catch (Exception e) {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter a valid numeric value!", ChatEmotion.NONE, 34));
                    return;
                }
                break;

            case "canstack":
                if (option.equals("1") || option.equals("0")) {
                    FurniFixDao.canStack(option, itemId);
                    client.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getId(), "Has modified the furni with id " + itemId + " to update the changes pick up the item and put it back.", ChatEmotion.NONE, 34));
                    ItemManager.getInstance().loadItemDefinitions();
                } else {
                    client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), "Please enter only a value between 1 and 0.", ChatEmotion.NONE, 34));
                    return;
                }
                break;
        }
    }

    @Override
    public String getPermission() {
        return "furnifix_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.furnifix.description");
    }
}