package com.cometproject.server.game.items.types;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.ItemType;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ItemDefinition implements FurnitureDefinition {
    private final int id;
    private final String publicName;
    private final String itemName;
    private final String type;
    private final ItemType itemType;
    private final int width;
    private final int length;
    private final double height;
    private final int spriteId;

    private final boolean canStack;
    private final boolean canSit;
    private final boolean canWalk;
    private final boolean canTrade;
    private final boolean canRecycle;
    private final boolean canMarket;
    private final boolean canGift;
    private final boolean canInventoryStack;

    private final int effectId;
    private final int offerId;
    private final String interaction;
    private final int interactionCycleCount;
    private final String[] vendingIds;
    private final boolean requiresRights;
    private final Double[] variableHeights;
    private final int songId;

    private final boolean deletable;

    public ItemDefinition(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.publicName = data.getString("public_name");
        this.itemName = data.getString("item_name");
        this.type = data.getString("type");
        this.width = data.getInt("width");
        this.length = data.getInt("length");
        final double height = data.getDouble("stack_height");
        this.spriteId = data.getInt("sprite_id");
        this.deletable = data.getString("deleteable").equals("1");

        this.canStack = data.getString("can_stack").equals("1");
        this.canSit = data.getString("can_sit").equals("1");
        this.canWalk = data.getString("is_walkable").equals("1");
        this.canTrade = data.getString("allow_trade").equals("1");
        this.canInventoryStack = data.getString("allow_inventory_stack").equals("1");

        this.offerId = data.getInt("flat_id");

        this.canRecycle = data.getString("allow_recycle").equals("1");
        this.canMarket = data.getString("allow_marketplace_sell").equals("1");
        this.canGift = data.getString("allow_gift").equals("1");

        this.effectId = data.getInt("effect_id");
        this.interaction = data.getString("interaction_type");
        this.interactionCycleCount = data.getInt("interaction_modes_count");
        this.vendingIds = data.getString("vending_ids").isEmpty() ? new String[0] : data.getString("vending_ids").split(",");

        this.requiresRights = data.getString("requires_rights").equals("1");

        this.songId = data.getInt("song_id");

        final String variableHeightData = data.getString("variable_heights");

        if (variableHeightData.contains(";")) {
            final String[] variableHeightArray = variableHeightData.split(";");
            this.variableHeights = new Double[variableHeightArray.length];

            for (int i = 0; i < variableHeightArray.length; i++) {
                try {
                    this.variableHeights[i] = Double.parseDouble(variableHeightArray[i]);
                } catch (Exception ignored) {

                }
            }
        } else {
            this.variableHeights = null;
        }

        if (height == 0) {
            this.height = 0.001;
        } else {
            this.height = height;
        }

        this.itemType = ItemType.forString(this.type);
    }

    public boolean isAdFurni() {
        return itemName.equals("ads_mpu_720") || this.itemName.equals("ads_background") || this.itemName.equals("ads_mpu_300") || this.itemName.equals("ads_mpu_160") || this.itemName.equals("backgroundk") || this.interaction.equals("ads_background") || this.itemName.equals("back_ground");
    }

    public boolean isRoomDecor() {
        return itemName.startsWith("wallpaper") || itemName.startsWith("landscape") || itemName.startsWith("floor");
    }

    public boolean isTeleporter() {
        return this.getInteraction().equals("teleport") || this.getInteraction().equals("teleport_door") || this.getInteraction().equals("teleport_pad");
    }

    public boolean isSong() {
        return this.songId != 0;
    }

    public int getId() {
        return this.id;
    }

    public String getPublicName() {
        return this.publicName;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getType() {
        return this.type;
    }

    public ItemType getItemType() {
        return this.itemType;
    }

    public int getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public int getLength() {
        return length;
    }

    public String getInteraction() {
        return interaction;
    }

    public int getInteractionCycleCount() {
        return this.interactionCycleCount;
    }

    public int getEffectId() {
        return effectId;
    }

    public String[] getVendingIds() {
        return vendingIds;
    }

    public int getOfferId() {
        return offerId;
    }

    public boolean canStack() {
        return canStack;
    }

    public boolean canDelete() {
        return deletable;
    }

    public boolean canSit() {
        return canSit;
    }

    public boolean canWalk() {
        return canWalk;
    }

    public boolean canTrade() {
        return canTrade;
    }

    public boolean canRecycle() {
        return canRecycle;
    }

    public boolean canMarket() {
        return canMarket;
    }

    public boolean canGift() {
        return canGift;
    }

    public boolean canInventoryStack() {
        return canInventoryStack;
    }

    public Double[] getVariableHeights() {
        return variableHeights;
    }

    public boolean requiresRights() {
        return requiresRights;
    }

    @Override
    public boolean isWired() {
        return this.getInteraction().startsWith("wf_act") || this.getInteraction().startsWith("wf_xtra") || this.getInteraction().startsWith("wf_trg") || this.getInteraction().startsWith("wf_cnd") || this.getInteraction().startsWith("wf_cstm") || this.getItemName().startsWith("tile_stackmagic") || this.getInteraction().equals("wf_hideitem") || this.getInteraction().equals("wf_sit_hideitem");
    }

    public int getSongId() {
        return this.songId;
    }

    /**
     * Items limited in stack by tile (usually achievement items)
     */
    public boolean isLimitableItem() {
        return this.getInteraction().startsWith("bb_patch")
                || this.getInteraction().startsWith("football_goal")
                || this.getInteraction().startsWith("horse_jump")
                || this.getInteraction().startsWith("gym_equipment")
                || this.getInteraction().startsWith("freeze_tile");
    }

    /**
     * Item stack limitation by tile
     */
    public int getItemLimitation() {
        switch (this.getInteraction()) {
            case "bb_patch":
                return CometSettings.maxBanzaiTilesInStack;

            case "football_goal":
                return CometSettings.maxFootballGatesInStack;

            case "horse_jump":
                return CometSettings.maxHorseJumpInStack;

            case "gym_equipment":
                return CometSettings.maxAcademyItemsInStack;

            case "freeze_tile":
                return CometSettings.maxFreezeTilesInStack;
        }

        return 100;
    }
}
