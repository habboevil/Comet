package com.cometproject.server.game.snowwar.items;

import com.cometproject.api.networking.messages.IComposer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by SpreedBlood on 2017-12-22.
 */
public class MapStuffData extends ExtraDataBase {
    public static final int TYPE_ID = 1;
    public static final String STATE = "state";
    public static final String RARITY = "rarity";
    public Map<String, String> extraData;
    public MapStuffData(String data) {
        extraData = new ConcurrentHashMap<>();
        setExtraData(data);
    }

    @Override
    public int getType() {
        return TYPE_ID;
    }

    @Override
    public byte[] data() {
        final StuffDataWriter data = new StuffDataWriter(TYPE_ID);
        data.writeInt8(extraData.size());
        for (final String key : extraData.keySet()) {
            data.writeString(key);
            data.writeString(extraData.get(key));
        }
        return data.getData();
    }

    @Override
    public void serializeComposer(IComposer writer) {
        writer.writeInt(this.extraData.size());
        for (Map.Entry<String, String> key : extraData.entrySet()) {
            writer.writeString(key.getKey());
            writer.writeString(key.getValue());
        }
    }


    @Override
    public void setExtraData(Object data) {
        final String sData = (String) data;

        final String[] values = sData.split("\t");
        for (final String part : values) {
            if (part.isEmpty() || part.equals("=")) {
                continue;
            }

            final String[] a = part.split("=");
            if (a.length != 2) {
                continue;
            }

            extraData.put(a[0], a[1]);
        }
    }

    @Override
    public String getWallLegacyString() {
        if (extraData == null) {
            return "";
        }

        final String data = extraData.get(STATE);
        if (data == null) {
            return "";
        }

        return data;
    }
}
