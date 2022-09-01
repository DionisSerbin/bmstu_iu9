package ru.bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ZookeeperApp {
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";
    final private static String LOCAL_HOST = "localhost";
    private static final int INDEX_OF_ZOOKEEPER_ADDRESS = 0;
    private static final int ZOOKEEPER_TIMEOUT = 3000;

    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        ActorSystem system = ActorSystem.create("routes");
        ActorRef storage = system.actorOf(Props.create(StorageActor.class));
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Http http = Http.get(system);
        ZooKeeper zk = null;

        try {
            zk = new ZooKeeper(args[INDEX_OF_ZOOKEEPER_ADDRESS], ZOOKEEPER_TIMEOUT, null);
            new ZooWatcher(zk, storage);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        StringBuilder serversInfo = new StringBuilder("Servers online at\n");

        try {
            StorageServer server = new StorageServer(http, storage, zk, args[1]);
            final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = server.createRoute().flow(system, materializer);
            final CompletionStage<ServerBinding> bind = http.bindAndHandle(
                    routeFlow,
                    ConnectHttp.toHost(LOCAL_HOST, Integer.parseInt(args[1])),
                    materializer
            );
            System.out.println("Server is starting at http://" + LOCAL_HOST + ":" + Integer.parseInt(args[1]));
            System.in.read();

            print(serversInfo + "\nPress RETURN to stop...");
            bind.
                    thenCompose(ServerBinding::unbind).
                    thenAccept(unbound -> system.terminate());
            serversInfo.append("http://localhost:").append(args[1]).append("/\n");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }


    public static void print(String s) {
        System.out.println(GREEN + s + RESET);
    }

}
