package ru.bmstu.iu9;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.japi.pf.ReceiveBuilder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;

public class TestActor extends AbstractActor {

    private ActorSelection store = getContext().actorSelection("/user/storeActor");
    private final String SCRIPT = "nashorn";

    private ArrayList<Test> buildTest(String jsScript, String functionName, String testName,
                                      String expectedResult, ArrayList<Integer> params) throws ScriptException, NoSuchMethodException {

        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName(SCRIPT);
        scriptEngine.eval(jsScript);
        Invocable invocable = (Invocable) scriptEngine;
        String res = invocable.invokeFunction(functionName, params.toArray()).toString();
        Test test = new Test(testName, expectedResult,
                params, expectedResult.equals(res));
        ArrayList<Test> testsNow = new ArrayList<>();
        testsNow.add(test);
        return testsNow;
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().
                match(TestMessage.class, m -> {
                    store.
                            tell(
                                    new MessageStor(
                                            m.getPackageId(),
                                            buildTest(
                                                    m.getJsScript(),
                                                    m.getFunctionName(),
                                                    m.getTest().getTestName(),
                                                    m.getTest().getExpectedResult(),
                                                    m.getTest().getParams()
                                            )

                                    ),
                                    self()
                            );
                }
                ).
                build();

    }

}
