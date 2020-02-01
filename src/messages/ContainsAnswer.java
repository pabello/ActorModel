package messages;

import static java.lang.System.nanoTime;

import lib.Actor;
import lib.Message;

public class ContainsAnswer extends Message {
    private boolean contains;
    private int value;
    private long requestTime;
    private Actor clientAddress;

    public ContainsAnswer(Actor sender, int value, boolean contains, long requestTime, Actor clientAddress) {
        super(sender);
        this.value = value;
        this.contains = contains;
        this.requestTime = requestTime;
        this.clientAddress = clientAddress;
    }

    public boolean getContains() {
        return contains;
    }

    public int getValue() { return value; }

    public long getRequestTime() { return requestTime; }

    public Actor getClientAddress() { return clientAddress; }
}
