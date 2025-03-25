package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.nux.NuxGiftEmailViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.storage.queries.catalog.SlotMachineDao;
import com.cometproject.server.utilities.RandomUtil;


public class SlotMachineFloorItem extends RoomItemFloor {
    private boolean isInUse = false;

    public SlotMachineFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!this.getPosition().touching(entity.getPosition())) {
                entity.moveTo(this.getPosition().squareInFront(this.getRotation()).getX(), this.getPosition().squareBehind(this.getRotation()).getY());
                return false;
            }
        }

        final PlayerEntity playerEntity = (PlayerEntity) entity;

        entity.cancelWalk();
        entity.lookTo(this.getPosition().squareInFront(this.getRotation()).getX() - 1, this.getPosition().squareBehind(this.getRotation()).getY() - 1);

        if (this.isInUse) {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(this.getVirtualId(), "This machine is being used, please wait.", 34));
            return false;
        }

        if (playerEntity.getPlayer().getData().getVipPoints() < playerEntity.getBetAmount() || playerEntity.getBetAmount() == 0) {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(this.getVirtualId(), "You don't have the amount you want to bet or your bet is 0. Adjust your bet with: bet (quantity)", 34));
            return false;
        }

        final int timeSinceLastUpdate = ((int) Comet.getTime() - playerEntity.getPlayer().getLastSlotMachineAction());

        if (timeSinceLastUpdate < 20) {
            playerEntity.getPlayer().getSession().send(new TalkMessageComposer(playerEntity.getPlayer().getEntity().getId(), "You must wait 20 seconds to place another bet.", ChatEmotion.NONE, 1));
            return false;
        }

        this.isInUse = true;
        boolean isWin = false;

        playerEntity.getPlayer().getData().decreaseVipPoints(playerEntity.getBetAmount());

        String rand1 = "";
        String rand2 = "";
        String rand3 = "";

        final int random1 = RandomUtil.getRandomInt(1, 3);
        final int random2 = RandomUtil.getRandomInt(1, 3);
        final int random3 = RandomUtil.getRandomInt(1, 3);

        if (random1 == random2 && random2 == random3) {
            int multiplier = 0;
            isWin = true;
            String image = "";

            switch (random1) {
                case 1:
                    multiplier = 10;
                    image = "bet_star";
                    break;
                case 2:
                    multiplier = 5;
                    image = "bet_heart";
                    break;
                case 3:
                    multiplier = 2;
                    image = "bet_skull";
                    break;
            }

            playerEntity.getPlayer().getSession().send(new NotificationMessageComposer(image, "You have won %q Diamonds with the slot machine.\n\n(%b x %m)"
                    .replace("%q", playerEntity.getBetAmount() * multiplier + "")
                    .replace("%b", playerEntity.getBetAmount() + "")
                    .replace("%m", multiplier + "")));

            playerEntity.getPlayer().getData().increaseVipPoints(playerEntity.getBetAmount() * multiplier);
        } else {
            playerEntity.getPlayer().getSession().send(
                    new NotificationMessageComposer("slot_loss", "Unfortunately the sequence was not the same and you lost %amount% diamonds.".replace("%amount%", playerEntity.getBetAmount() + ""))
            );
        }

        switch (random1) {
            case 1:
                rand1 = "¥";
                break;
            case 2:
                rand1 = "|";
                break;
            case 3:
                rand1 = "ª";
                break;
        }

        switch (random2) {
            case 1:
                rand2 = "¥";
                break;
            case 2:
                rand2 = "|";
                break;
            case 3:
                rand2 = "ª";
                break;
        }

        switch (random3) {
            case 1:
                rand3 = "¥";
                break;
            case 2:
                rand3 = "|";
                break;
            case 3:
                rand3 = "ª";
                break;
        }

        playerEntity.getPlayer().getSession().send(new TalkMessageComposer(entity.getId(), "You took it out " + rand1 + " " + rand2 + " " + rand3 + ".", ChatEmotion.NONE, 34));
        playerEntity.getPlayer().sendBalance();

        this.setTicks(RoomItemFactory.getProcessTime(1));

        this.getItemData().setData("1");
        this.sendUpdate();

        this.saveData();

        playerEntity.getPlayer().setLastSlotMachineAction(timeSinceLastUpdate);
        SlotMachineDao.insertBet(playerEntity.getPlayer().getData().getId(), "slot_machine", playerEntity.getBetAmount() + "", Comet.getTime() + "", isWin ? "win" : "share");

        return true;
    }

    @Override
    public void onPlaced() {
        if (!"0".equals(this.getItemData().getData())) {
            this.getItemData().setData("0");
        }
    }

    @Override
    public void onPickup() {
        this.cancelTicks();
    }

    @Override
    public void onTickComplete() {
        this.isInUse = false;

        this.getItemData().setData("0");
        this.sendUpdate();

        this.saveData();
    }
}
