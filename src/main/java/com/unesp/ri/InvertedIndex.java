package com.unesp.ri;
import java.util.HashMap;
import java.util.Map;

public class InvertedIndex {
    public InvertedIndex(Collection collection) {
        this.buildIndex(collection);
    }

    public HashMap<String, Token> index = new HashMap<String, Token>();

    // build the inverted index
    public void buildIndex(Collection collection) {

        // build list of documents
        for (Document document : collection.documents) {
            for (String term : document.termFrequencyMap.keySet()) {
                if (this.index.containsKey(term)) {
                    // if the term already exists in the index
                    // add the document to the token
                    Token token = this.index.get(term);
                    token.add(document);
                } else {
                    // if the term does not exist in the index
                    // create a new token and add the document to the token
                    Token token = new Token(term);
                    token.add(document);
                    this.index.put(term, token);
                }
            }
        }

        // build document term frequency map
        for (String term : this.index.keySet()) {
            // calculating the idf of the term
            Token token = this.index.get(term);
            token.setIDF(collection.getIDF(term));
        }
    }

    public Map<String, Float> getRank(String query){
        Document queryDocument = new Document(query, "userQuery");
        Map<String, Integer> queryTermFrequency = queryDocument.getTermFrequencyMap();
        HashMap<String, Float> queryTermTFIDF = new HashMap<>();
        HashMap<String, Document> documentsThatContainsAQueryTerm = new HashMap<>();
        

        // lookup terms in the inverted index which are in the query
        for (String term : queryTermFrequency.keySet()) {
            if (this.index.containsKey(term)) {
                Token token = this.index.get(term);
                queryTermTFIDF.put(term, queryTermFrequency.get(term) * token.idf);
                
                // add document to hm
                for (Document document : token.documentsList) {
                    documentsThatContainsAQueryTerm.put(document.id, document);
                }
            }
        }

        // so now we have the tfidf of each term in the query and we only need to compare its
        // values with the tfidf of each term in the documents that contains a query term
        // and calculate the cosine similarity
        Map<String, Float> cossineSimilarityMap = new HashMap<>();
        for (String documentId : documentsThatContainsAQueryTerm.keySet()) {
            
            // get document object
            Document document = documentsThatContainsAQueryTerm.get(documentId);
            float cossineSimilarity = 0;

            // for each term in the query
            for (String term : queryTermTFIDF.keySet()) {
                
                // if the document contains the term
                if (document.termFrequencyMap.containsKey(term)) {
                
                    // calculate cossine similarity for each queryTermTFIDF vs documentTermTFIDF
                    float numerator = 0;
                    float denominator = 0;
                    float queryTermTFIDFValue = queryTermTFIDF.get(term);
                    float documentTermTFIDFValue = document.calculateTF(term) * this.index.get(term).idf;
                    numerator += queryTermTFIDFValue * documentTermTFIDFValue;
                    denominator += Math.pow(queryTermTFIDFValue, 2) * Math.pow(documentTermTFIDFValue, 2);
                    cossineSimilarity += denominator > 0 ? numerator/denominator : 0;
                }
            }

            // add document id to a tree map with the cossine similarity as key
            cossineSimilarityMap.put(documentId, cossineSimilarity);
        }

        return cossineSimilarityMap;
    }


    public Map<String, Float> getDocumentTermsTFIDF(Document document){
        Map<String, Integer> queryTermFrequency = document.getTermFrequencyMap();
        Map<String, Float> queryTermTFIDF = new HashMap<>();
    
        for (String term : queryTermFrequency.keySet()) {
            float tokenIDF = this.getIndexToken(term).getIDF();
            queryTermTFIDF.put(term, document.calculateTF(term) * tokenIDF);
        }
        return queryTermTFIDF;
    }


    public Token getIndexToken(String term){
        if (this.index.containsKey(term)) {
            return this.index.get(term);
        }
        else {
            return new Token(term);
        }
    }
}
