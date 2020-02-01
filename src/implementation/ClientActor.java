package implementation;

import lib.Actor;
import lib.Message;
import messages.*;

public class ClientActor extends Actor {
    private Actor serviceAddres;

    public ClientActor() {
        super(null, null);
    }

    public void setServiceAddres(Actor serviceAddres) {
        this.serviceAddres = serviceAddres;
    }

    public void inputValue(int value) {
        if(serviceAddres != null) {
            serviceAddres.reciveMessage(new SpawnMessage(this, value));
        } else {
            noConnection();
        }
    }

    public void requestValue(int value) {
        if(serviceAddres != null) {
            serviceAddres.reciveMessage(new ContainsMessage(this, value));
        } else {
            noConnection();
        }
    }

    public void removeValue(int value) {
        if( serviceAddres != null ) {
            serviceAddres.reciveMessage(new RemoveMessage(this, value));
        } else {
            noConnection();
        }
    }

    @Override
    protected void processCustomMessage(Message m) {
        if(m instanceof ContainsAnswer) {
            requestAnswer( (ContainsAnswer)m );
        }
        if(m instanceof NodeRemovedMessage) {
            nodeRemoved( (NodeRemovedMessage)m );
        }
        if(m instanceof NodeNotFoundMessage) {
            nodeNotFound( (NodeNotFoundMessage)m );
        }
    }

    private void requestAnswer(ContainsAnswer m) {
        String juice;
        if( m.getContains() )
            juice = "contains";
        else
            juice = "does not contain";
        System.out.println("The tree " + juice + " a value of " + m.getValue());
    }

    private void nodeRemoved(NodeRemovedMessage m) {
        System.out.println("Node of value " + m.getValue() + " has been removed from the tree.");
    }

    private void nodeNotFound(NodeNotFoundMessage m) {
        System.out.println("Node containing a value of " + m.getValue() + " does not exist in the tree.");
    }

    private void noConnection() {
        System.out.println("Unable to complete the operation. There is no connection to a service.");
    }
}
