/* A class that is responsable for indexing the documents and creating the inverted index for
suporrting TF-IDF, BM25 or related algorithms. */

package com.unesp.ri;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Indexer {

    public ArrayList<String> getTerms(File file) throws IOException {
        // gets a file and returns a list of terms
        Scanner myReader = new Scanner(file);
        ArrayList<String> terms = new ArrayList<String>();

        while (myReader.hasNextLine()) { 
            String data = myReader.nextLine();
            String line = data.replaceAll("[^a-zA-Z0-9]+", " ");

            for (String s: line.split("\\s")) {
                String term = s.replace("\\n+", "\n");

                // test if its greater than zero and can not be integer
                if (term.length() > 0 && !term.matches("[0-9]+")) {
                    
                    // if so then put it in a list of terms
                    terms.add(term.toLowerCase());
                }
            }
        }
        myReader.close();
        
        return terms;
    }

    public Map<String, Integer> getTermsFrequency(ArrayList<String> terms) {
        // Returns a map of terms and their frequency in a document
        Map<String, Integer> termFrequency = new HashMap<String, Integer>();

        for (String term: terms) {
            if (termFrequency.containsKey(term)) {
                termFrequency.put(term, termFrequency.get(term) + 1);
            } else {
                termFrequency.put(term, 1);
            }
        }
        return termFrequency;
    }
    
}
