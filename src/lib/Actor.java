package lib;

import lib.messageTypes.SuicideMessage;
import lib.messageTypes.SystemMessage;
import lib.messageTypes.DeadNodeMessage;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Actor extends Thread implements Runnable {
    protected Actor rootAddress;
    protected Actor supervisorAddress;
    protected List<Actor> nodeList;
    protected ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    protected LinkedBlockingQueue<Message> postponedMessageQueue = new LinkedBlockingQueue<>();
    private boolean isAlive = true;
    protected boolean removeBlocked = false;

    public Actor(Actor root, Actor supervisor) {
        this.rootAddress = root;
        this.supervisorAddress = supervisor;
        this.start();
    }

    protected void processMessage(Message m) {
        if (m instanceof SystemMessage) {
            processSystemMessage(m);
        } else {
            processCustomMessage(m);
        }
    }

    private void processSystemMessage(Message m) {
        if (m instanceof SuicideMessage)
            commitSuicide();
        if (m instanceof DeadNodeMessage) {
            processDeadNodeMessage( (DeadNodeMessage)m );
        }
    }

    private void processDeadNodeMessage(DeadNodeMessage m) {
        if( !nodeList.isEmpty() ) {
            for(int i=0; i<nodeList.size(); i++) {
                if( nodeList.get(i) == m.getSenderAddress() ) {
                    nodeList.set(1, null);
                    return;
                }
            }
        }
    }

    protected abstract void processCustomMessage(Message m);

    public void reciveMessage(Message m) {
        messageQueue.add(m);
    }

    // legacy, not used in the project
    protected void registerNode(Actor n) {
        nodeList.add(n);
    }

    @Override
    public void run() {
        int sleepCounter = 10;
        while(isAlive) {
            if( !removeBlocked && !postponedMessageQueue.isEmpty() ) {
                Message m = postponedMessageQueue.remove();
                processMessage(m);
            } else if( !messageQueue.isEmpty() ) {
                Message m = messageQueue.remove();
                processMessage(m);
            } else {
                try {
//                    System.out.println("sleeping for " + sleepCounter + "ms");
                    sleep(sleepCounter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    sleepCounter = 2 * sleepCounter;
                    if(sleepCounter > 1024) {
                        sleepCounter = 1024;
                    }
                }
            }
        }
    }

    protected void commitSuicide() {
        if(nodeList != null) {
            for (int i=0; i<nodeList.size(); i++) {
                Actor actor = nodeList.get(i);
                if( actor != null )
                    actor.reciveMessage(new SuicideMessage(this));
            }
        }
        if( supervisorAddress != null ) {
            supervisorAddress.reciveMessage(new DeadNodeMessage(this));
        }
        die();
    }

    /**
     * Sets isAlive boolean to false.
     */
    protected void die() {
        this.isAlive = false;
    }

//    protected abstract boolean handleException(ExceptionMessage m);
}
