package ru.bmstu.iu9;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;

public class TestPackageActor extends AbstractActor {

    private ActorSelection testPerformRouter = getContext()
            .actorSelection("/user/testPerformerActor");

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().
            match(MassageTestPackage.class,
                m -> {
                    for (Test test : m.getTests()) {
                        testPerformRouter.tell(
                                new TestMessage(
                                        m.getPackageId(),
                                        m.getJsScript(),
                                        m.getFunctionName(),
                                        test
                                ),
                                self()
                        );
                    }
                }
            ).
            build();
    }
}
