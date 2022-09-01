package ru.bmstu.iu9;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Test {

    private final String TEST_NAME_STRING = "testName";
    private final String EXPECTED_RESULT_STRING = "expectedResult";
    private final String PARAMS_STRING = "params";
    private final String RESULT_STRING = "result";

    @JsonProperty(TEST_NAME_STRING)
    private String testName;

    @JsonProperty(EXPECTED_RESULT_STRING)
    private String expectedResult;

    @JsonProperty(PARAMS_STRING)
    private ArrayList<Integer> params;

    @JsonProperty(RESULT_STRING)
    private boolean result;

    @JsonCreator
    public Test(@JsonProperty(TEST_NAME_STRING) String testName,
                @JsonProperty(EXPECTED_RESULT_STRING) String expectedResult,
                @JsonProperty(PARAMS_STRING) ArrayList<Integer> params){

        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
        this.result = false;

    }

    public Test(String testName, String expectedResult,
                ArrayList<Integer> params, boolean result){

        this.testName = testName;
        this.expectedResult = expectedResult;
        this.params = params;
        this.result = result;

    }

    public String getExpectedResult() {
        return this.expectedResult;
    }

    public String getTestName() {
        return this.testName;
    }

    public ArrayList<Integer> getParams() {
        return this.params;
    }

    public boolean getResult() {
        return this.result;
    }
}
