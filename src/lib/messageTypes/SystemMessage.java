package lib.messageTypes;

import lib.Actor;
import lib.Message;

public abstract class SystemMessage extends Message {

    public SystemMessage(Actor sender) { super(sender); }
}
