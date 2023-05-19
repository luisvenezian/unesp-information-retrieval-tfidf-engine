package com.unesp.api;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.unesp.ri.Collection;
import com.unesp.ri.Document;
import com.unesp.ri.Root;
import com.unesp.ri.InvertedIndex;
import static spark.Spark.get;
import static spark.Spark.post;

public final class App {
    private App() {
        
    }

    public static void main(String[] args) throws IOException {
        
        // creating the collection
        Root root = new Root();
        Collection corpus = new Collection("IR2023");
        root.addCollection(corpus);

        // creating the documents
        Document d1 = new Document("To do is to be To be is to do", "d1");
        Document d2 = new Document("To be or not to be. I am what I am.", "d2");
        Document d3 = new Document("I think therefore I am. Do be do be do.", "d3");
        Document d4 = new Document("Do do do, da da da Let it be, let it be.", "d4");

        // adding the documents to the collection
        corpus.addDocument(d1);
        corpus.addDocument(d2);
        corpus.addDocument(d3);
        corpus.addDocument(d4);

        // given a collection we can lazily calculate the idf of each term
        InvertedIndex invertedIndex = new InvertedIndex(corpus);

        // do a query
        get("/search/:query", (request, response) -> {
            String query = request.params(":query");    
            Map<String, Float> rank = invertedIndex.getRank(query);

            return rank
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue,
                    LinkedHashMap::new
            ));
        }, new Json());

        //  do a query validation
        get("/search/validate/:query", (request, response) -> {
            String query = request.params(":query");
            Document queryDocument = new Document(query, "userQuery");

            return queryDocument.getTermFrequencyMap();
            // Map<String, Float> unsortedRank = invertedIndex.getDocumentTermsTFIDF(queryDocument);

            // return unsortedRank.entrySet()
            // .stream()
            // .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            // .collect(Collectors.toMap(
            //         Map.Entry::getKey,
            //         Map.Entry::getValue,
            //         (oldValue, newValue) -> oldValue,
            //         LinkedHashMap::new
            // ));
            
        }, new Json());

        // add document from post body
        post("/document/add/:documentId", (request, response) -> {
            String documentId = request.params(":documentId");
            String documentData = request.body();
            Document document = new Document(documentData, documentId);
            corpus.addDocument(document);

            return "Document has been added successfully. Don't forget to reindex the collection.";
        }, new Json());

        // run the indexer
        get("/indexer/run", (request, response) -> {
            invertedIndex.buildIndex(corpus);
            return "Indexer has been run successfully.";
        }, new Json());
    
        // get document
        get("/document/get/:documentId", (request, response) -> {
            String documentId = request.params(":documentId");
            Document document = corpus.getDocument(documentId);
            return invertedIndex.getDocumentTermsTFIDF(document);
        }, new Json());

        // root
        get("/", (req, res) -> {
            // get numbers of collections
            Integer rootSize = root.getSize();
            HashMap<String, Object> rootInfo = new HashMap<>();

            if (rootSize > 0){
                HashMap<String, Object> collectionInfo = new HashMap<>();
                rootInfo.put("name", root.name);
                rootInfo.put("version", root.version);

                // print the collections name
                for (Collection c : root.getCollections()){
                    collectionInfo.put(c.name, c.getSize());
                }
                rootInfo.put("collections", collectionInfo);

                
                // return map as json
                return rootInfo;
            }
            else {
                return "No collections found. Please add a collection.";
            }
        }, new Json());

    }

}
