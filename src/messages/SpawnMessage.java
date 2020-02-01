package messages;

import lib.Actor;
import lib.Message;

public class SpawnMessage extends Message {
    int nodeValue;

    public SpawnMessage(Actor sender, int value) {
        super(sender);
        this.nodeValue = value;
    }

    public int getNodeValue() {
        return nodeValue;
    }
}
