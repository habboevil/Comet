package com.cometproject.server.utilities.comparators;

import com.cometproject.server.game.rooms.objects.RoomFloorObject;
import com.cometproject.server.game.rooms.objects.RoomObject;

import java.util.Comparator;


public class PositionComparator implements Comparator<RoomFloorObject> {
    private final RoomObject roomFloorObject;

    public PositionComparator(RoomObject roomFloorObject) {
        this.roomFloorObject = roomFloorObject;
    }

    @Override
    public int compare(RoomFloorObject o1, RoomFloorObject o2) {
        final double distanceOne = o1.getPosition().distanceTo(this.roomFloorObject.getPosition());
        final double distanceTwo = o2.getPosition().distanceTo(this.roomFloorObject.getPosition());

        return Double.compare(distanceOne, distanceTwo);

    }
}
