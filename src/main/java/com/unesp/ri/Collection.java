package com.unesp.ri;
import java.util.ArrayList;


public class Collection {
    public Collection(String name) {
        this.name = name;
    }

    public String name;
    public ArrayList<Document> documents = new ArrayList<Document>();

    public Document getDocument(String id) {
        for (Document document : this.documents) {
            if (document.id.equals(id)) {
                return document;
            }
        }
        return null;
    }

    public void addDocument(Document document) {
        try {               
            this.documents.add(document);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Integer getSize() {
        return this.documents.size();
    }

    public Integer getTermFrequency(String term) {
        // get term frequency in the collection
        // how many times a term such "cars" appears in the entire collection
        Integer frequency = 0;

        for (Document document : this.documents) {
            if (document.termFrequencyMap.containsKey(term)) {
                frequency += 1;
            }
        }

        return frequency;
    }


    public float getIDF(String term) {
        // get inverse document frequency of a term in the collection
        float termFrequencyInCollection = (float)this.getTermFrequency(term);
        if (termFrequencyInCollection > 0) {
            float SizeByfrequency = (float)this.getSize() / termFrequencyInCollection;
            float idf = (float)(Math.log(SizeByfrequency) / Math.log(2)); 
            return idf;
        }
        
        return 0f;
        
    }

    public ArrayList<Float> calculateTFIDF(Document doc){
        ArrayList<Float> tfidf = new ArrayList<Float>();

        for (String term : doc.getTermFrequencyMap().keySet()) {
            // calculate tfidf for each term in the document
            float tf = doc.calculateTF(term);
            float idf = this.getIDF(term);
            float tfidfValue = tf * idf;
            tfidf.add(tfidfValue);
        }

        return tfidf;
    }
}