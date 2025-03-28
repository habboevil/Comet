package com.cometproject.server.game.snowwar.gameobjects;

/*
 * ****************
 * @author capos *
 * ****************
 */

import com.cometproject.server.game.snowwar.*;

public abstract class GameItemObject {
    public static final int SNOWBALL = 1;
    public static final int TREE = 2;
    public static final int PILE = 3;
    public static final int MACHINE = 4;
    public static final int HUMAN = 5;
    public final int variablesCount;
    public com.cometproject.server.game.snowwar.items.BaseItem BaseItem;
    public Object Data;
    public int objectId;
    public boolean _active = false;

    protected GameItemObject(int varCount) {
        variablesCount = varCount;
    }

    // Abstract functions
    public abstract PlayerTile location3D();

    public abstract Direction360 direction360();

    public abstract int getVariable(int var);

    public abstract int[] boundingData();

    // Override in sub-class
    public void onRemove() {
    }

    public void subturn(SynchronizedGameStage _arg1) {
    }

    public void onSnowBallHit(SnowBallGameObject _arg2) {
    }

    public void GenerateCHECKSUM(final SnowWarRoom arena, final int multipler) {
        for (int i = 0; i < variablesCount; ) {
            arena.checksum += (getVariable(i) * ++i) * multipler;
        }
    }

    public int _3au() {
        return (-((objectId + 1)));
    }

    public int collisionHeight() {
        return (boundingData()[0]);
    }

    public boolean testSnowBallCollision(SnowBallGameObject _arg1) {
        return (_arg1.location3D().z() < collisionHeight() && CollisionDetection._pU(this, _arg1));
    }
}
