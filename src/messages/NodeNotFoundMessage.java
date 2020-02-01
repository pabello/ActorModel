package messages;

import lib.Actor;
import lib.Message;

public class NodeNotFoundMessage extends Message {
    private int value;
    private Actor clientAddress;

    public NodeNotFoundMessage(Actor sender, Actor clientAddress, int value) {
        super(sender);
        this.value = value;
        this.clientAddress = clientAddress;
    }

    public int getValue() {
        return value;
    }

    public Actor getClientAddress() { return clientAddress; }
}
