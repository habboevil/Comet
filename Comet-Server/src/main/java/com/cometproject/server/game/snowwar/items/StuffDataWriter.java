package com.cometproject.server.game.snowwar.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SpreedBlood on 2017-12-22.
 */
public class StuffDataWriter {
    public List<Integer> savedPositions = new ArrayList<>();
    public int writer;
    private byte[] bytes;

    public StuffDataWriter(final int type, final int Size) {
        bytes = new byte[Size];
        writeInt8(type);
    }

    public StuffDataWriter(final int type) {
        this(type, 1000); // default size
    }

    public byte[] getData() {
        if (writer == bytes.length) {
            return bytes;
        }

        final byte[] rtn = new byte[writer];
        System.arraycopy(bytes, 0, rtn, 0, writer);
        bytes = rtn;

        return bytes;
    }

    public void writeInt32(int in) {
        bytes[writer++] = (byte) ((in >>> 24) & 0xFF);
        bytes[writer++] = (byte) ((in >>> 16) & 0xFF);
        bytes[writer++] = (byte) ((in >>> 8) & 0xFF);
        bytes[writer++] = (byte) ((in) & 0xFF);
    }

    public void writeInt16(int in) {
        bytes[writer++] = (byte) ((in >>> 8) & 0xFF);
        bytes[writer++] = (byte) ((in) & 0xFF);
    }

    public void writeInt8(int in) {
        bytes[writer++] = (byte) ((in) & 0xFF);
    }

    public void writeString(String in) {
        final int len = in.length();
        writeInt16(len);
        for (int i = 0; i < len; i++) {
            bytes[writer++] = (byte) (in.charAt(i) & 0xff);
        }
    }

    public void writeBytes(byte[] in) {
        final int len = in.length;
        writeInt16(len);
        for (byte b : in) {
            bytes[writer++] = b;
        }
    }

    /* Saving positions and writing later... */
    public Object setSaved(final Object add) {
        savedPositions.add(writer);
        return add;
    }

    public void writeSaved(final Object add) {
        if (add instanceof Integer) {
            final int tmp = writer;
            writer = savedPositions.remove(savedPositions.size() - 1);
            writeInt32((Integer) add);
            writer = tmp;
        } else {
            throw new UnsupportedOperationException("Bad Param in SetWriter " + add.getClass());
        }
    }

    public void writeSavedInt8(final int add) {
        final int tmp = writer;
        writer = savedPositions.remove(savedPositions.size() - 1);
        writeInt8(add);
        writer = tmp;
    }
}
