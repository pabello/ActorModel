package lib.messageTypes;

import lib.Actor;

public class ExceptionMessage extends SystemMessage {
    Exception exception;

    public ExceptionMessage(Actor sender, Exception exception) {
        super(sender);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
