package com.unesp.ri;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;

public class Document {
    public Document(String path) {
        this.path = path;
        this.id = path.split("/")[path.split("/").length - 1];
    }

    public String path;
    public Map<String, Integer> termFrequencyMap;
    public String id;
    private ArrayList<Float> tfidf = new ArrayList<Float>();
    
    
    public void setTFIDF(ArrayList<Float> tfidf) {
        this.tfidf = tfidf;
    }

    public ArrayList<Float> getTFIDF() {
        return this.tfidf;
    }

    public Map<String, Integer> getTermFrequencyMap() {
        this.calculateIndex();
        return this.termFrequencyMap;
    }

    // apply the indexer to the document and loads the terms and their frequency into a map 
    public void calculateIndex(){
        try {
            File file = new File(this.path);
            Indexer idx = new Indexer();
            ArrayList<String> terms = idx.getTerms(file);
            this.termFrequencyMap = idx.getTermsFrequency(terms);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } 
    }

    // returns the term frequency of a term in a document
    public Float calculateTF(String term){
        Integer tf = this.getTermFrequencyMap().get(term);
        return (tf > 0) ? 1 + ((float)(Math.log(tf) / Math.log(2))) : 0;
    }
}