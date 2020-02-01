package lib;

public abstract class Message {
    private Actor senderAddress;

    public Message(Actor sender) {
        this.senderAddress = sender;
    }

    public Actor getSenderAddress() {
        return senderAddress;
    }
}
