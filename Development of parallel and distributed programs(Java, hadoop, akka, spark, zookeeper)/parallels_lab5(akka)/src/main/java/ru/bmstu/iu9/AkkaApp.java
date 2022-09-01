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
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;


import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.pattern.Patterns;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class AkkaApp {

    private static final String LOCAL_HOST = "localhost";
    private static final int PORT = 8080;
    private static final String TEST_URL = "testUrl";
    private static final String COUNT = "count";
    private static final int MAP_ASYNC = 1;
    private static final int TIME_OUT = 5;
    private static final String TIME_RESPONSE = "Response time = ";
    private static final String AVG_RESPONSE_TIME_PTR = "Average resPonse time = ";

    private static Flow<HttpRequest, HttpResponse, NotUsed> createFLow(Http http, ActorSystem system,
                                                                       ActorMaterializer materializer, ActorRef actor) {
        return Flow.of(HttpRequest.class).map(
                (req) -> {
                    Query query = req.getUri().query();
                    String url = query.get(TEST_URL).get();
                    int count = Integer.parseInt(
                            query.get(COUNT).get()
                    );
                    System.out.println(url + " - " + count + "");
                    return new Pair<String, Integer>(url, count);
                }
        ).mapAsync(MAP_ASYNC,
                req -> {
                    CompletionStage<Object> completionStage = Patterns.ask(
                            actor,
                            new Message(
                                    req.first()
                            ),
                            Duration.ofSeconds(TIME_OUT));
                    return completionStage
                            .thenApply(res -> (Long) res)
                            .thenCompose(
                                    res -> {
                                        if (res >= 0) {
                                            return CompletableFuture.completedFuture(
                                                    new Pair<>(
                                                            req.first(),
                                                            res
                                                    )
                                            );
                                        }
                                        Flow<Pair<String, Integer>, Long, NotUsed> flow = Flow.<Pair<String, Integer>>create()
                                                .mapConcat(
                                                        pair -> Collections.nCopies(
                                                                pair.second(),
                                                                pair.first()
                                                        )

                                                ).mapAsync(
                                                        req.second(),
                                                        url -> {
                                                            long start = System.currentTimeMillis();
                                                            CompletableFuture<Long> resss = asyncHttpClient()
                                                                    .prepareGet(url)
                                                                    .execute()
                                                                    .toCompletableFuture().thenApply(
                                                                            resp -> {
                                                                                long end = System.currentTimeMillis();
                                                                                System.out.println(TIME_RESPONSE + (end - start) + "\n");
                                                                                return end - start;
                                                                            }
                                                                    );
                                                            return resss;

                                                        }
                                                );
                                        return Source.single(req).via(flow).
                                                toMat(Sink.fold(
                                                        0l,
                                                        Long::sum
                                                        ),
                                                        Keep.right()
                                                ).
                                                run(materializer).
                                                thenApply(
                                                        sum -> new Pair<>(
                                                                req.first(),
                                                                (sum / req.second())
                                                        )
                                                );
                                    }
                            );
                }
        ).
                map(
                        req -> {
                            actor.tell(
                                    new StorageMessage(
                                            req.first(),
                                            (Long) req.second()
                                    ),
                                    ActorRef.noSender()
                            );
                            System.out.println(AVG_RESPONSE_TIME_PTR + req.second());
                            return HttpResponse.create().withEntity(
                                    req.second().toString() + '\n'
                            );
                        }
                );
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Server is going to start");
        ActorSystem system = ActorSystem.create("routes");
        ActorRef actor = system.actorOf(Props.create(CacheActor.class));
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<
                HttpRequest,
                HttpResponse,
                NotUsed
                > routeFlow = createFLow(http, system, materializer, actor);
        final CompletionStage<ServerBinding> bind = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(LOCAL_HOST, PORT),
                materializer
        );
        System.out.println("Server is starting at http://" + LOCAL_HOST + ":" + PORT);
        System.in.read();
        bind.
                thenCompose(ServerBinding::unbind).
                thenAccept(unbound -> system.terminate());
    }

}
