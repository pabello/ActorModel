package implementation;

import lib.messageTypes.SuicideMessage;

public class Main {

    public static void main(String[] args) {

        RootActor root = new RootActor();
        ClientActor client = new ClientActor();

        client.setServiceAddres(root);

        setupTestTree(client);


        // test case
        client.removeValue(15);
        client.requestValue(12);
        client.removeValue(12);
        client.requestValue(12);
        client.removeValue(15);
        client.removeValue(14);
        client.removeValue(12);

        client.requestValue(13);
        client.requestValue(15);
        client.requestValue(12);
        client.requestValue(16);
        client.requestValue(14);
        //----------------------

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.reciveMessage(new SuicideMessage(null));
        root.reciveMessage(new SuicideMessage(null));
    }

    /**
     * Sets up a smot test tree that is close to being balanced.
     * @param client
     */
    private static void setupTestTree(ClientActor client) {
        client.inputValue(10);
        client.inputValue(5);
        client.inputValue(8);
        client.inputValue(15);
        client.inputValue(12);
        client.inputValue(11);
        client.inputValue(17);
        client.inputValue(14);
        client.inputValue(3);
        client.inputValue(19);
        client.inputValue(7);
        client.inputValue(1);
        client.inputValue(4);
        client.inputValue(18);
        client.inputValue(8);
        client.inputValue(2);
        client.inputValue(20);
        client.inputValue(13);
        client.inputValue(16);
        client.inputValue(6);

    }

}
