package lib.messageTypes;

import lib.Actor;
import lib.Message;

public class DeadNodeMessage extends SystemMessage {
    public DeadNodeMessage(Actor sender) {
        super(sender);
    }
}
