package com.company;

import javax.print.Doc;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Document implements Serializable {
    private String id;
    private String name;
    private String location;

    Document(String id, String name, String location){
        this.id = id;
        this.name = name;
        this.location = location;
    }

    Document(String name, String location){
        this.name = name;
        this.location = location;
    }

    private Map<String, Object> tags = new HashMap<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void addTag(String key, Object obj){
        tags.put(key, obj);
    }
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Document)) {
            return false;
        }
        Document other = (Document) obj;
        return id.equals(other.id);
    }
}
