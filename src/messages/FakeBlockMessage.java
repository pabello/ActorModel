package messages;

import lib.Actor;
import lib.Message;

public class FakeBlockMessage extends Message {
    public FakeBlockMessage(Actor sender) {
        super(sender);
    }
}
