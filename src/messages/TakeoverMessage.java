package messages;

import lib.Actor;
import lib.Message;

public class TakeoverMessage extends Message {
    public TakeoverMessage(Actor sender) {
        super(sender);
    }
}
