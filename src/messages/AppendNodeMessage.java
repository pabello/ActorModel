package messages;

import lib.Actor;
import lib.Message;

public class AppendNodeMessage extends Message {
    private Actor node;
    private boolean left; // 1 -> append left | 0 -> append right

    public AppendNodeMessage(Actor sender, Actor node, boolean left) {
        super(sender);
        this.node = node;
        this.left = left;
    }

    public Actor getNode() {
        return node;
    }

    public boolean isLeft() {
        return left;
    }
}
