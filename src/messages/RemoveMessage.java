package messages;

import lib.Actor;
import lib.Message;

public class RemoveMessage extends Message {
    private int value;

    public RemoveMessage(Actor sender, int value) {
        super(sender);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
