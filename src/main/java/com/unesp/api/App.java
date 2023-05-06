package com.unesp.api;
import java.io.IOException;
import java.util.HashMap;
import com.unesp.ri.Collection;
import com.unesp.ri.Document;
import com.unesp.ri.Root;
import static spark.Spark.get;

public final class App {
    private App() {
        
    }

    /**
     * Initializes the application.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // creating the collection
        Root root = new Root();
        Collection corpus = new Collection("IR2023");
        root.addCollection(corpus);

        // creating the documents
        Document d1 = new Document("To do is to be To be is to do", "d1") ;
        Document d2 = new Document("To be or not to be. I am what I am.", "d2") ;
        Document d3 = new Document("I think therefore I am. Do be do be do.", "d3") ;
        Document d4 = new Document("Do do do, da da da Let it be, let it be.", "d4") ;

        // adding the documents to the collection
        corpus.addDocument(d1);
        corpus.addDocument(d2);
        corpus.addDocument(d3);
        corpus.addDocument(d4);

        // refreshing the collection
        corpus.refreshCollection();

        // printing the tfidf of each term in each document
        for (Document document : corpus.documents) {
            System.out.println("TFIDF of document " + document.id + ": " + document.getTFIDF());
        }

        //  do a query
        get("/search/:query", (request, response) -> {
            String query = request.params(":query");
            Document userQueryDocument = new Document(query, "user_query");
            userQueryDocument.setTFIDF(corpus.calculateTFIDF(userQueryDocument));

            HashMap<String, Object> queryInfo = new HashMap<>();
            queryInfo.put("id", userQueryDocument.id);
            queryInfo.put("data", userQueryDocument.raw);
            queryInfo.put("vector", userQueryDocument.getTFIDF());
            return queryInfo;
        }, new Json());
        
        // get document
        get("/document/:document", (request, response) -> {
            String documentId = request.params(":document");
            Document document = corpus.getDocument(documentId);
            HashMap<String, Object> documentInfo = new HashMap<>();
            documentInfo.put("id", document.id);
            documentInfo.put("data", document.raw);
            documentInfo.put("vector", document.getTFIDF());
            return documentInfo;
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
