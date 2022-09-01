package ru.bmstu.iu9;

public class TestMessage {
    private Integer packageId;
    private String jsScript;
    private String functionName;
    private Test test;

    public TestMessage(Integer packageId, String jsScript,
                       String functionName, Test test){

        this.packageId = packageId;
        this.functionName = functionName;
        this.jsScript = jsScript;
        this.test = test;

    }

    public Integer getPackageId() {
        return this.packageId;
    }

    public String getJsScript() {
        return this.jsScript;
    }

    public Test getTest() {
        return this.test;
    }

    public String getFunctionName() {
        return this.functionName;
    }
}
