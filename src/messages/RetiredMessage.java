package messages;

import lib.Actor;
import lib.Message;

public class RetiredMessage extends Message {
    private Actor leftNode;
    private Actor rightNode;

    public RetiredMessage(Actor sender, Actor left, Actor right) {
        super(sender);
        leftNode = left;
        rightNode = right;
    }

    public Actor getLeftNode() { return leftNode; }

    public Actor getRightNode() {
        return rightNode;
    }
}
