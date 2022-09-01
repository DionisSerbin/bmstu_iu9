package ru.bmstu.iu9;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class MessageStor {
    private final String ID_PACKAGE_STRING = "packageId";
    private final String TEST_STRING = "test";

    @JsonProperty(ID_PACKAGE_STRING)
    private Integer packageId;

    @JsonProperty(TEST_STRING)
    private ArrayList<Test> test;

    @JsonCreator
    public MessageStor(@JsonProperty(ID_PACKAGE_STRING) Integer packageId,
                       @JsonProperty(TEST_STRING) ArrayList<Test> test){

        this.packageId = packageId;
        this.test = test;

    }

    public Integer getPackageId(){
        return this.packageId;
    }

    public ArrayList<Test> getTest(){
        return this.test;
    }

}
