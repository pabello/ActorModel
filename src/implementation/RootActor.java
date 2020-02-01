package implementation;

import static java.lang.System.nanoTime;

import lib.Actor;
import lib.Message;
import messages.*;

import java.util.Arrays;

public class RootActor extends Actor {

    public RootActor() {
        super(null, null);
        nodeList = Arrays.asList(new Actor[1]);
    }

    @Override
    protected void processCustomMessage(Message m) {
        if(m instanceof SpawnMessage) {
            processSpawnMessage( (SpawnMessage)m );
        }
        else if(m instanceof ContainsMessage) {
            if( nodeList.get(0) != null ) {
                nodeList.get(0).reciveMessage(m);
            }
        }
        else if(m instanceof ContainsAnswer) {
            ((ContainsAnswer) m).getClientAddress().reciveMessage(m);
            long answerTime = nanoTime() - ((ContainsAnswer) m).getRequestTime();
            System.out.println("Answer in " + ((float)answerTime)/1000000 + "ms");
        }
        else if(m instanceof RemoveMessage) {
            processRemoveMessage( (RemoveMessage)m );
        }
        else if(m instanceof RetiredMessage) {
            processRetiredMessage( (RetiredMessage)m );
        }
        else if(m instanceof NodeNotFoundMessage) {
            ((NodeNotFoundMessage)m).getClientAddress().reciveMessage(m);
        }
    }

    private void processSpawnMessage(SpawnMessage m) {
        if( nodeList.get(0) == null ) {
            nodeList.set(0, new NodeActor(m.getNodeValue(), this, this));
        } else {
            nodeList.get(0).reciveMessage(m);
        }
    }

    private void processRemoveMessage(RemoveMessage m) {
        if( nodeList.get(0) == null ) {
            m.getSenderAddress().reciveMessage(new NodeNotFoundMessage(this, m.getSenderAddress(), m.getValue()));
        } else {
            nodeList.get(0).reciveMessage(m);
        }
    }

    private void processRetiredMessage(RetiredMessage m) {
        if( m.getRightNode() != null ) {
            nodeList.set(0, m.getRightNode());
            if( m.getLeftNode() != null ) {
                nodeList.get(0).reciveMessage(new AppendNodeMessage(this, m.getLeftNode(), true));
            }
        } else if ( m.getLeftNode() != null ) {
            nodeList.set(0, m.getLeftNode());
        } else {
            nodeList.set(0, null);
        }
    }
}
