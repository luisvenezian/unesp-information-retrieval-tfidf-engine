package com.unesp.ri;

import java.util.ArrayList;

public class Token {
    public Token(String term) {
        this.term = term;
    }

    public String term;
    public ArrayList<Document> documentsList;
    public float idf;


    // add document to the token
    public void add(Document document) {
        if (this.documentsList == null) {
            this.documentsList = new ArrayList<Document>();
        }
        this.documentsList.add(document);
    }
    
    public void setIDF(float idf) {
        this.idf = idf;
    }

    public float getIDF() {
        return this.idf;
    }

}
