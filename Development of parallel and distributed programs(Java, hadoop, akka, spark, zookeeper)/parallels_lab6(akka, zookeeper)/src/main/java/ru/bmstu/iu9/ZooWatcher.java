package ru.bmstu.iu9;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;


import java.util.ArrayList;
import java.util.List;

public class ZooWatcher implements Watcher {
    final private static String SERVERS = "/servers";
    private ZooKeeper zoo;
    private ActorRef storage;

    public ZooWatcher(ZooKeeper zoo, ActorRef storage) throws KeeperException, InterruptedException {
        this.zoo = zoo;
        this.storage = storage;
        sendServers();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            zoo.getChildren(SERVERS, this);
            sendServers();
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendServers() throws KeeperException, InterruptedException {
        List<String> servers = new ArrayList<>();
        for (String s : zoo.getChildren(SERVERS, this)) {
            servers.add(new String(zoo.getData(SERVERS + "/" + s, false, null)));
        }
        storage.tell(
                new MessageServersList(servers),
                ActorRef.noSender()
        );
    }

    static class MessageServersList {
        private final List<String> servers;

        MessageServersList(List<String> servers){
            this.servers = servers;
        }

        public List<String> getServers() {
            return servers;
        }
    }

}
