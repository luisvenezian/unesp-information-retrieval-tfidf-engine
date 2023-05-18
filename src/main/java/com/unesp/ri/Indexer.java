package com.unesp.ri;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Indexer {

    public Indexer() {
        // constructor
    }

    public ArrayList<String> getTerms(String raw) throws IOException {
        // gets a file and returns a list of terms
        ArrayList<String> terms = new ArrayList<String>();
        String refinedInput = raw.replaceAll("[^a-zA-Z0-9]+", " ").replaceAll("\\n+", " ");

        for (String term: refinedInput.split("\\s")) {
            // test if its greater than zero and can not be integer
            if (term.length() > 0 && !term.matches("[0-9]+")) {
                
                // if so then put it in a list of terms
                terms.add(term.toLowerCase());
            }
        }
        
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
