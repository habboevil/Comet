package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.network.messages.incoming.catalog.data.UnseenItemsMessageComposer;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import com.google.common.collect.Sets;

import java.util.Set;

public class FishingPoolFloorItem
        extends RoomItemFloor {
    private final int fishingPoleBaseItem = 101;
    private final int fishingBugBaseItem = 100;
    public PlayerEntity incomingEntity = null;
    private int machineStatus = 0;

    public FishingPoolFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onPlaced() {
        this.disposeItem();
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }
            Player player = ((PlayerEntity) entity).getPlayer();
            if (player == null) {
                return false;
            }
            if (this.getPosition().distanceTo(entity.getPosition()) > 5.0) {
                player.sendBubble("fishing_distance", "You must be m\u00e1s nearby to start fishing.");
                return false;
            }
            if (!player.getInventory().hasBaseItem(this.fishingBugBaseItem) && !player.getInventory().hasBaseItem(this.fishingPoleBaseItem)) {
                player.sendBubble("fishing_items", "You need a fishing ca\u00f1a and a bait to start fishing.");
                return false;
            }
            if (this.incomingEntity != null) {
                if (this.getRoom().getEntities().getPlayerEntities().contains(this.incomingEntity)) {
                    player.sendBubble("fishing_in_use", "This fishing point is in use, try once it has been vacated.");
                    return false;
                }
                this.disposeItem();
            } else {
                if (this.machineStatus != 0) {
                    player.sendBubble("fishing_in_use", "This fishing point is in use, try once it has been vacated.");
                    return false;
                }
                this.incomingEntity = (PlayerEntity) entity;
            }
            this.machineStatus = 1;
            this.flash(1);
            this.setTicks(RoomItemFactory.getProcessTime(2.5));
            this.incomingEntity.getPlayer().sendBubble("fishing_start", "You start trying to fish in the node...");
            return true;
        }
        return false;
    }

    @Override
    public void onTickComplete() {
        if (this.incomingEntity != null) {
            switch (this.machineStatus) {
                case 1: {
                    this.startFishing();
                    this.flash(0);
                    this.setTicks(RoomItemFactory.getProcessTime(Integer.parseInt(Locale.getOrDefault("fishing.step1.cooldown", "5"))));
                    break;
                }
                case 2: {
                    this.doFishingChance();
                    this.flash(1);
                    this.setTicks(RoomItemFactory.getProcessTime(Integer.parseInt(Locale.getOrDefault("fishing.step2.cooldown", "5"))));
                    break;
                }
                case 3: {
                    this.finishFishing();
                    this.flash(0);
                    break;
                }
                case 4: {
                    break;
                }
                case 5: {
                    this.disposeItem();
                }
            }
        } else {
            this.disposeItem();
        }
    }

    private void startFishing() {
        this.incomingEntity.getPlayer().getSession().send(new WhisperMessageComposer(this.incomingEntity.getId(), "<i>You throw the ca\u00f1a next to the bait to the fishing spot...</i>", 1));
        this.incomingEntity.carryItem(34, 30);
        this.machineStatus = 2;
    }

    private void doFishingChance() {
        double percentage = Math.random() * 100.0;
        if (percentage <= 80.0) {
            this.machineStatus = 3;
        } else {
            this.removeFishingReagents(true);
        }
    }

    private void removeFishingReagents(boolean isFishingFail) {
        if (isFishingFail) {
            this.incomingEntity.getPlayer().getSession().send(new WhisperMessageComposer(this.incomingEntity.getId(), "<i>Your ca\u00f1a has just broken after a battle with the fish that bite\u00f3 the bait...</i>", 1));
            this.disposeItem();
        } else {
            this.removeReagent(this.fishingBugBaseItem);
        }
    }

    private void removeReagent(int baseId) {
        if (this.incomingEntity.getPlayer().getInventory().getInventoryItems() == null) {
            this.disposeItem();
            return;
        }
        for (PlayerItem inventoryItem : this.incomingEntity.getPlayer().getInventory().getInventoryItems().values()) {
            if (inventoryItem.getBaseId() != baseId) continue;
            this.incomingEntity.getPlayer().getInventory().removeItem(inventoryItem);
            ItemDao.deleteItem(inventoryItem.getId());
            break;
        }
        this.incomingEntity.getPlayer().getSession().send(new UpdateInventoryMessageComposer());
    }

    private void finishFishing() {
        if (!this.isValidAction()) {
            return;
        }
        int fishOneBaseItem = 10109;
        int fishTwoBaseItem = 10105;
        int fishThreeBaseItem = 10069;
        int fishFourBaseItem = 10072;
        Data itemIdData = Data.createEmpty();
        double percentage = Math.random() * 100.0;
        int fishFinalBaseItem = percentage <= 15.0 ? fishOneBaseItem : (percentage <= 35.0 ? fishTwoBaseItem : (percentage <= 58.0 ? fishThreeBaseItem : fishFourBaseItem));
        StorageContext.getCurrentContext().getRoomItemRepository().createItem(this.incomingEntity.getPlayer().getId(), fishFinalBaseItem, "0", ((Data) itemIdData)::set);
        InventoryItem playerItem = new InventoryItem((Long) itemIdData.get(), fishFinalBaseItem, "0");
        this.incomingEntity.getPlayer().getInventory().addItem(playerItem);
        this.incomingEntity.getPlayer().getSession().send(new UpdateInventoryMessageComposer());
        this.incomingEntity.getPlayer().getSession().send(new UnseenItemsMessageComposer((Set) Sets.newHashSet((Object[]) new PlayerItem[]{playerItem})));
        //this.incomingEntity.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_134, 1);
        this.incomingEntity.getPlayer().sendBubble("fishing_start", Locale.getOrDefault("fishing.completed", "You have just finished the fishing process, you hunt a %fishtype%.").replace("%fishtype%", playerItem.getDefinition().getPublicName()));
        this.disposeItem();
    }

    private void disposeItem() {
        this.incomingEntity = null;
        this.machineStatus = 0;
    }

    private boolean isValidAction() {
        if (!this.getRoom().getEntities().getPlayerEntities().contains(this.incomingEntity)) {
            this.disposeItem();
            return false;
        }
        if (this.incomingEntity == null || this.incomingEntity.getPlayer() == null || this.incomingEntity.getPlayer().getData() == null || this.incomingEntity.getPlayer().getSession() == null) {
            this.disposeItem();
            return false;
        }
        return true;
    }

    private void flash(int data) {
        this.getItemData().setData(data);
        this.sendUpdate();
    }
}

