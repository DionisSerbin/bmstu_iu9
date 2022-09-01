package ru.bmstu.iu9;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class MassageTestPackage {
    private final String ID_PACKAGE_STRING = "packageId";
    private final String JS_SCRIPT_STRING = "jsScript";
    private final String FUNCTION_NAME_STRING = "functionName";
    private final String TESTS_STRING = "tests";

    @JsonProperty(ID_PACKAGE_STRING)
    private Integer packageId;

    @JsonProperty(JS_SCRIPT_STRING)
    private String jsScript;

    @JsonProperty(FUNCTION_NAME_STRING)
    private String functionName;

    @JsonProperty(TESTS_STRING)
    private ArrayList<Test> tests;


    @JsonCreator
    MassageTestPackage(@JsonProperty(ID_PACKAGE_STRING) int packageId,
                       @JsonProperty(JS_SCRIPT_STRING) String jsScript,
                       @JsonProperty(FUNCTION_NAME_STRING) String functionName,
                       @JsonProperty(TESTS_STRING) ArrayList<Test> tests){

        this.packageId = packageId;
        this.jsScript = jsScript;
        this.functionName = functionName;
        this.tests = tests;
    }

    public Integer getPackageId() {
        return this.packageId;
    }

    public ArrayList<Test> getTests() {
        return this.tests;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public String getJsScript() {
        return this.jsScript;
    }
}
