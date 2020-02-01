package messages;

import lib.Actor;
import lib.Message;

public class NodeRemovedMessage extends Message {
    private int value;

    public NodeRemovedMessage(Actor sender) {
        super(sender);
    }

    public int getValue() {
        return value;
    }
}
