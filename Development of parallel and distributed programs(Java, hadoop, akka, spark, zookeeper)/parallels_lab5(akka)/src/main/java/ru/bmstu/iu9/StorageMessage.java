package ru.bmstu.iu9;

public class StorageMessage {
    private String url;
    private Long time;

    public StorageMessage(String url, Long time){
        this.url = url;
        this.time = time;
    }

    public String getUrl(){
        return this.url;
    }

    public Long getTime() {
        return this.time;
    }
}
