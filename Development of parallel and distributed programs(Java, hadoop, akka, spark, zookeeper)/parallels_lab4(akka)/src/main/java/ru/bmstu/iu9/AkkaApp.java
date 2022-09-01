package ru.bmstu.iu9;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.routing.RoundRobinPool;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

public class AkkaApp{
    private ActorRef storeActor;
    private ActorRef testPackageActor;
    private ActorRef testPerformActor;
    private static final String STORE_ACTOR = "storeActor";
    private static final String TEST_PACKAGE_ACTOR = "testPackageActor";
    private static final String TEST_PERFORMER_ACTOR = "testPerformerActor";
    private static final Integer POOLS_NUMBER = 5;
    private static final String ID_PACKAGE_STRING = "packageId";
    private static final Integer TIME_OUT = 5000;
    private static final String SERVER_IP = "localhost";
    private static final Integer PORT = 8080;

    private AkkaApp(final ActorSystem system) {
        storeActor = system.actorOf(Props.create(StoreActor.class), STORE_ACTOR );
        testPackageActor = system.actorOf(Props.create(TestPackageActor.class),
                TEST_PACKAGE_ACTOR );
        testPerformActor = system.actorOf(
                new RoundRobinPool(POOLS_NUMBER).props(Props.create(TestActor.class)),
                TEST_PERFORMER_ACTOR);
    }

    private Route createRoute() {
        return route(
                get(
                        () -> parameter(
                                ID_PACKAGE_STRING,
                                (packageId) -> {
                                    CompletionStage<Object> res = PatternsCS.ask(
                                            storeActor,
                                            new GetMessagePackage(Integer.parseInt(packageId)),
                                            TIME_OUT
                                    );
                                    return completeOKWithFuture(
                                            res,
                                            Jackson.marshaller()
                                    );
                                }
                        )
                ),
                post(
                        () -> entity(
                                Jackson.unmarshaller(MassageTestPackage.class),
                                m -> {
                                    testPackageActor.tell(m, ActorRef.noSender());
                                    return complete("Test is going");
                                }
                        )

                )
        );

    }

    public static void main(String[] args) throws IOException {
        final ActorSystem system = ActorSystem.create("lab4");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);


        final AkkaApp akkaApp = new AkkaApp(system);
        final Flow<
                HttpRequest,
                HttpResponse,
                NotUsed
                > route = akkaApp.createRoute().flow(
                            system,
                            materializer
        );

        final CompletionStage<ServerBinding> serverBinding = http.
                bindAndHandle(
                        route,
                        ConnectHttp.toHost(SERVER_IP, PORT),
                        materializer
                );

        System.out.println("Server is going");
        System.in.read();
        serverBinding.
                thenCompose(ServerBinding::unbind).
                thenAccept(unbound -> system.terminate());
    }
}
