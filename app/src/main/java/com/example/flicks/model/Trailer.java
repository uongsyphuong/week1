package com.example.flicks.model;

public class Trailer {

    private String id ;

    private String key;

    private String name;

    private String site;

    private Integer size;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Trailer(String id, String key, String name, Integer size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.size = size;
        this.type = type;
    }
}
