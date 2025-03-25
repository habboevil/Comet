package com.cometproject.server.network.messages.outgoing.landing.calendar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class CampaignCalendarDataMessageComposer extends MessageComposer {

    private final boolean[] openBox;
    private final int unlockDay;
    private final int openSize;
    private int openCount;
    private int lateCount;

    public CampaignCalendarDataMessageComposer(boolean[] o) {
        this.openBox = o;
        this.unlockDay = LandingManager.getInstance().getUnlockDays();
        this.openSize = o.length;
    }


    @Override
    public short getId() {
        return Composers.AdventCalendarDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("xmas14");
        msg.writeString("");

        msg.writeInt(this.unlockDay); // día actual.
        msg.writeInt(LandingManager.getInstance().getTotalDays()); // días totales

        for (int i = 0; i < this.openSize; i++) {
            if (this.openBox[i]) {
                this.openCount++;
            } else {
                if (this.unlockDay == i)
                    continue;

                this.lateCount++;
            }
        }

        msg.writeInt(this.openCount); // Open boxes.

        for (int i = 0; i < this.openSize; i++) {
            if (this.openBox[i])
                msg.writeInt(i);
        }

        msg.writeInt(this.lateCount); // Boxes that have been out-of-date.

        for (int i = 0; i < this.openSize; i++) {

            if (this.unlockDay == i)
                continue;

            if (!this.openBox[i]) {
                msg.writeInt(i);
            }
        }
    }
}
