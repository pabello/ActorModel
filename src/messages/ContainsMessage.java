package messages;

import static java.lang.System.nanoTime;

import lib.Actor;
import lib.Message;

public class ContainsMessage extends Message {
    private int nodeValue;
    private long requestTime;

    public ContainsMessage(Actor sender, int value) {
        super(sender);
        this.nodeValue = value;
        this.requestTime = nanoTime();
    }

    public int getNodeValue() {
        return nodeValue;
    }

    public long getRequestTime() { return requestTime; }
}
