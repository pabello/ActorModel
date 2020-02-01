package implementation;

import lib.Actor;
import lib.Message;
import messages.*;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class NodeActor extends Actor {
    private int value;
    private boolean goingToDie = false;
    private boolean leftNodeBlocked = false;
    private boolean rightNodeBlocked = false;

    public NodeActor(int value, Actor root, Actor supervisor) {
        super(root, supervisor);
        this.nodeList = Arrays.asList((new Actor[2]));
        this.value = value;
    }

    @Override
    protected void processCustomMessage(Message m) {

        if(m instanceof SpawnMessage) {
            processSpawnMessage( (SpawnMessage)m );
        }
        else if(m instanceof ContainsMessage) {
            processContainsMessage( (ContainsMessage)m );
        }
        else if(m instanceof RemoveMessage) {
            processRemoveMessage( (RemoveMessage)m );
        }
        else if(m instanceof RetiredMessage) {
            processRetiredMessage( (RetiredMessage)m );
        }
        else if(m instanceof AppendNodeMessage) {
            processAppendNodeMessage( (AppendNodeMessage)m );
        }
        else if(m instanceof NodeTransferedMessage) {
            processNodeTransferedMessage( (NodeTransferedMessage)m );
        }
        else if(m instanceof TakeoverMessage) {
            processTakeoverMessage( (TakeoverMessage)m );
        }
        else if(m instanceof FakeBlockMessage) {
            processFakeBlockMessage( (FakeBlockMessage)m );
        }
    }

    private void processSpawnMessage(SpawnMessage m) {
        if( m.getNodeValue() > this.value ) {
            if( nodeList.get(1) == null ) {
                nodeList.set(1, new NodeActor(m.getNodeValue(), rootAddress, this));
            } else {
                if( !rightNodeBlocked ) {
                    nodeList.get(1).reciveMessage(m);
                } else {
                    this.postponedMessageQueue.add(m);
                }
            }
        } else if( m.getNodeValue() < this.value ) {
            if( nodeList.get(0) == null ) {
                nodeList.set(0, new NodeActor(m.getNodeValue(), rootAddress, this));
            } else {
                if( !leftNodeBlocked ) {
                    nodeList.get(0).reciveMessage(m);
                } else {
                    this.postponedMessageQueue.add(m);
                }
            }
        }
    }

    private void processContainsMessage(ContainsMessage m) {
        if( m.getNodeValue() < this.value ) {
            if( nodeList.get(0) != null )
                if( !leftNodeBlocked ) {
                    nodeList.get(0).reciveMessage(m);
                } else {
                    this.postponedMessageQueue.add(m);
                }
            else {
                rootAddress.reciveMessage(new ContainsAnswer(this, m.getNodeValue(),false, m.getRequestTime(), m.getSenderAddress()));
            }
        } else if( this.value == m.getNodeValue() ) {
            if( !goingToDie ) {
                rootAddress.reciveMessage(new ContainsAnswer(this, value,true, m.getRequestTime(), m.getSenderAddress()));
            } else {
                this.postponedMessageQueue.add(m);
            }
        } else if( m.getNodeValue() > this.value ) {
            if( nodeList.get(1) != null ) {
                if( !rightNodeBlocked ) {
                    nodeList.get(1).reciveMessage(m);
                } else {
                    this.postponedMessageQueue.add(m);
                }
            } else {
                rootAddress.reciveMessage(new ContainsAnswer(this, m.getNodeValue(),false, m.getRequestTime(), m.getSenderAddress()));
            }
        }
    }

    private void processRemoveMessage(RemoveMessage m) {
        if( m.getValue() == value ) {
            if( !leftNodeBlocked && !rightNodeBlocked) {
                supervisorAddress.reciveMessage(new RetiredMessage(this, nodeList.get(0), nodeList.get(1)));
                if( nodeList.get(0) == null && nodeList.get(1) == null ){
                    die();
                }
            } else {
                goingToDie = true;
                this.postponedMessageQueue.add(m);
            }
        } else if( m.getValue() > value && nodeList.get(1) != null) {
            if( !rightNodeBlocked ) {
                nodeList.get(1).reciveMessage(m);
                rightNodeBlocked = true;
                super.removeBlocked = true;
            } else {
                this.postponedMessageQueue.add(m);
            }
            supervisorAddress.reciveMessage(new FakeBlockMessage(this));
        } else if( m.getValue() < value && nodeList.get(0) != null ) {
            if( !leftNodeBlocked ) {
                nodeList.get(0).reciveMessage(m);
                leftNodeBlocked = true;
                super.removeBlocked = true;
            } else {
                this.postponedMessageQueue.add(m);
            }
            supervisorAddress.reciveMessage(new FakeBlockMessage(this));
        } else {
            supervisorAddress.reciveMessage(new FakeBlockMessage(this));
            rootAddress.reciveMessage(new NodeNotFoundMessage(this, m.getSenderAddress(), m.getValue()));
        }
    }

    private void processRetiredMessage(RetiredMessage m) {
        if( m.getSenderAddress() == nodeList.get(0) ) {
            if( m.getLeftNode() != null ) {
                nodeList.set(0, m.getLeftNode());
                nodeList.get(0).reciveMessage(new TakeoverMessage(this));
                if( m.getRightNode() != null ) {
                    nodeList.get(0).reciveMessage(new AppendNodeMessage(this, m.getRightNode(), false));
                }
            } else if( m.getRightNode() != null ) {
                nodeList.set(0, m.getRightNode());
                nodeList.get(0).reciveMessage(new TakeoverMessage(this));
            } else {
                nodeList.set(0, null);
            }

            leftNodeBlocked = false;
        } else if( m.getSenderAddress() == nodeList.get(1) ){
            if( m.getRightNode() != null ) {
                nodeList.set(1, m.getRightNode());
                nodeList.get(1).reciveMessage(new TakeoverMessage(this));
                if( m.getLeftNode() != null ) {
                    nodeList.get(1).reciveMessage(new AppendNodeMessage(this, m.getLeftNode(), true));
                }
            } else if( m.getLeftNode() != null ) {
                nodeList.set(1, m.getLeftNode());
                nodeList.get(1).reciveMessage(new TakeoverMessage(this));
            } else {
                nodeList.set(1, null);
            }

            rightNodeBlocked = false;
        }

        if( !leftNodeBlocked && !rightNodeBlocked ) {
            super.removeBlocked = false;
        }
    }

    private void processAppendNodeMessage(AppendNodeMessage m) {
        if( m.isLeft() ) {
            if( nodeList.get(0) == null ) {
                nodeList.set(0, m.getNode());
                nodeList.get(0).reciveMessage(new TakeoverMessage(this));
            } else {
                nodeList.get(0).reciveMessage(m);
            }
        } else {
            if( nodeList.get(1) == null ) {
                nodeList.set(1, m.getNode());
                nodeList.get(1).reciveMessage(new TakeoverMessage(this));
            } else {
                nodeList.get(1).reciveMessage(m);
            }
        }
    }

    private void processNodeTransferedMessage(NodeTransferedMessage m) {
        if( m.getSenderAddress() == nodeList.get(0) ) {
            nodeList.set(0, null);
        } else if( m.getSenderAddress() == nodeList.get(1)) {
            nodeList.set(1, null);
        }

        if( nodeList.get(0) == null && nodeList.get(1) == null ) {
            die();
        }
    }

    private void processTakeoverMessage(TakeoverMessage m) {
        supervisorAddress.reciveMessage(new NodeTransferedMessage(this));
        supervisorAddress = m.getSenderAddress();
    }

    private void processFakeBlockMessage(FakeBlockMessage m) {
        if( m.getSenderAddress() == nodeList.get(0) ) {
            leftNodeBlocked = false;
        } else if( m.getSenderAddress() == nodeList.get(1) ) {
            rightNodeBlocked = false;
        }

        if( !leftNodeBlocked && !rightNodeBlocked ) {
            super.removeBlocked = false;
        }
    }

}
