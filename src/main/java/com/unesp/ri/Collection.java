package com.unesp.ri;
import java.io.IOException;
import java.util.ArrayList;


public class Collection {
    public Collection(String name) {
        this.name = name;
    }

    public String name;
    public ArrayList<Document> documents = new ArrayList<Document>();
    public Integer size;

    public void addDocument(Document document) throws IOException {
        document.calculateIndex();
        this.documents.add(document);
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
        float SizeByfrequency = (float)this.getSize() / (float)this.getTermFrequency(term);
        float idf = (float)(Math.log(SizeByfrequency) / Math.log(2)); 
        return idf;
    }

    public void refreshCollection() throws IOException {
        System.out.println("Refreshing collection: " + this.name);

        // refresh the tfidf of every term in every document in the collection
        for (Document document : this.documents) {
            ArrayList<Float> tfidf = new ArrayList<Float>();

            for (String term : document.getTermFrequencyMap().keySet()) {
                // calculate tfidf for each term in the document
                float tf = document.calculateTF(term);
                float idf = this.getIDF(term);
                float tfidfValue = tf * idf;
                tfidf.add(tfidfValue);
                
                // print log
                System.out.println("Loading TF("+tf+") * IDF("+idf+") in doc "+ document.id +" for term:" + term + ": " + tfidfValue);
            }
            
            // updates the tfidf of the document
            document.setTFIDF(tfidf);
        }
    }
}