package messages;

import lib.Actor;
import lib.Message;

public class NodeTransferedMessage extends Message {
    public NodeTransferedMessage(Actor sender) {
        super(sender);
    }
}
