package com.cometproject.server.composers.group;

import com.cometproject.api.game.groups.types.IGroup;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;
import org.apache.commons.lang3.StringUtils;


public class ManageGroupMessageComposer extends MessageComposer {

    private final IGroup group;

    public ManageGroupMessageComposer(final IGroup group) {
        this.group = group;
    }

    @Override
    public short getId() {
        return Composers.ManageGroupMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeInt(group.getData().getRoomId());
        msg.writeString(""); // room name
        msg.writeBoolean(false);
        msg.writeBoolean(true);
        msg.writeInt(group.getId());
        msg.writeString(group.getData().getTitle());
        msg.writeString(group.getData().getDescription());
        msg.writeInt(group.getData().getRoomId());
        msg.writeInt(group.getData().getColourA());
        msg.writeInt(group.getData().getColourB());
        msg.writeInt(group.getData().getType().getTypeId());
        msg.writeInt(group.getData().canMembersDecorate() ? 0 : 1);
        msg.writeBoolean(group.getData().hasForum());
        msg.writeString(""); // url

        msg.writeInt(5);

        String[] badgeData = group.getData().getBadge().replace("b", "").replace("X", "").split("s");

        int amountOfData = 5 - badgeData.length;
        int dataAppended = 0;

        for (String text : badgeData) {
            int num1 = (text.length() >= 6) ? Integer.parseInt(StringUtils.left(text, 3)) : Integer.parseInt(StringUtils.left(text, 2));
            int num2 = (text.length() >= 6) ? Integer.parseInt(StringUtils.left(StringUtils.right(text, 3), 2)) : Integer.parseInt(StringUtils.right(StringUtils.left(text, 4), 2));

            msg.writeInt(num1);
            msg.writeInt(num2);

            if (text.length() < 5) {
                msg.writeInt(0);
            } else if (text.length() >= 6) {
                msg.writeInt(Integer.parseInt(StringUtils.right(text, 1)));
            } else {
                msg.writeInt(Integer.parseInt(StringUtils.right(text, 1)));
            }
        }

        while (dataAppended != amountOfData) {
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            dataAppended++;
        }

        msg.writeString(group.getData().getBadge());
        msg.writeInt(group.getMembers().getAll().size());
    }
}
