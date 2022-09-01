package ru.bmstu.iu9;

public class GetMessagePackage {
    private Integer packageId;

    public GetMessagePackage(Integer packageId){
        this.packageId = packageId;
    }

    public Integer getPackageId(){
        return this.packageId;
    }
}
