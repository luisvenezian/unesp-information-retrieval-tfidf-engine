package com.unesp.ri;

import java.util.ArrayList;

public class Root {
    // constructor
    public Root() {
        this.collections = new ArrayList<Collection>();
        this.size = 0;
        this.name = "UNESP TFIDF - In Memory Search Engine";
        this.version = 0.1f;
    }

    // attribute list of collection
    private ArrayList<Collection> collections;
    private Integer size;
    public String name;
    public float version;

    // add the collection to list
    public void addCollection(Collection collection) {
        this.collections.add(collection);
        this.size = this.collections.size();
    }

    // get the collection list
    public ArrayList<Collection> getCollections() {
        return this.collections;
    }

    // get size
    public Integer getSize() {
        return this.size;
    }
}
