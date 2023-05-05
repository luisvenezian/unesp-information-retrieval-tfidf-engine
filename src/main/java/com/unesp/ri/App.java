package com.unesp.ri;
import java.io.IOException;

public final class App {
    private App() {
        
    }

    /**
     * Initializes the application.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // creating the collection
        Collection corpus = new Collection("IR-Classes-2023");
        
        // initializing the documents
        Document d1 = new Document("src/main/java/com/unesp/ri/data/d1");
        Document d2 = new Document("src/main/java/com/unesp/ri/data/d2");
        Document d3 = new Document("src/main/java/com/unesp/ri/data/d3");
        Document d4 = new Document("src/main/java/com/unesp/ri/data/d4");

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
    }
}
