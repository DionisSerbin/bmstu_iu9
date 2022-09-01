package ru.bmstu.iu9;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreActor extends AbstractActor {

    private Map<Integer, ArrayList<Test>> store = new HashMap<>();

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().
                match(
                        MessageStor.class, m -> {
                        if (store.containsKey(m.getPackageId())) {
                            ArrayList<Test> testNow = store.get(m.getPackageId());
                            testNow.addAll(m.getTest());
                            store.replace(
                                    m.getPackageId(),
                                    testNow
                            );
                        } else {
                            store.put(
                                    m.getPackageId(),
                                    m.getTest()

                            );
                        }
                    }
                ).
                match(
                        GetMessagePackage.class, s -> {
                            sender().tell(
                                    new MessageStor(
                                            s.getPackageId(),
                                            store.get(
                                                    s.getPackageId()
                                            )
                                    ),
                                    self()
                            );
                        }
                ).
                build();
    }

    public Map<Integer, ArrayList<Test>> getStore() {
        return this.store;
    }
}
