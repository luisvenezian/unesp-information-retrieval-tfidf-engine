package com.unesp.ri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryEngine {
    
    public QueryEngine(Collection c, String query) {
        this.givenCollection = c;
        this.userProvidedDocument = new Document(query, "user_query");
        this.userProvidedDocument.setTFIDF(this.givenCollection.calculateTFIDF(userProvidedDocument));
        this.cossineSimilarity = this.calculateSimilarity();
    }
    
    // attributes 
    private Collection givenCollection;
    private Document userProvidedDocument;
    private HashMap<String, Float> cossineSimilarity;


    // calculates cossine similarity between the query and the documents in the collection
    public HashMap<String, Float> calculateSimilarity() {
        
        HashMap<String, Float> cossineSimilarity = new HashMap<String, Float>();
        for (Document document : this.givenCollection.documents) {
            cossineSimilarity.put(document.id, this.cossineSimilarityBetweenVectors(this.userProvidedDocument.getTFIDF(), document.getTFIDF()));
        }

       
        // order hashmap by rank values and return
        if (cossineSimilarity.size() == 1) {
            return cossineSimilarity;
        } else if (cossineSimilarity.size() == 0) {
            HashMap<String, Float> errorMap = new HashMap<>();
            errorMap.put("error", -1.0f);
            return errorMap;
        } else {
        return cossineSimilarity.entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue,
                LinkedHashMap::new));
        }
    }

    // calculate cossine similarity between the query and a document
    public Float cossineSimilarityBetweenVectors(ArrayList<Float> vQ, ArrayList<Float> vC){
        Float cossineSimilarity = 0f;
        
        try {
            float dotProduct = 0f;
            float v1Magnitude = 0f;
            float v2Magnitude = 0f;

            for (int i = 0; i < vQ.size(); i++) {
                dotProduct += vQ.get(i) * vC.get(i);
                v1Magnitude += Math.pow(vQ.get(i), 2);
                v2Magnitude += Math.pow(vC.get(i), 2);
            }
            v1Magnitude = (float)Math.sqrt(v1Magnitude);
            v2Magnitude = (float)Math.sqrt(v2Magnitude);
            cossineSimilarity = dotProduct / (v1Magnitude * v2Magnitude);
        } catch (Exception e) {
            cossineSimilarity = -1f;
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        return (cossineSimilarity.isNaN()) ? 0 : cossineSimilarity;
    }
    

    // getCossineSimilarity
    public HashMap<String, Float> getCossineSimilarity() {
        return this.cossineSimilarity;
    }

}
