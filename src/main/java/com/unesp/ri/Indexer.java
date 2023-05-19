package com.unesp.ri;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;

import java.util.HashMap;
import java.util.List;

public class Indexer {

    public Indexer() {
        // constructor
    }

    public String filePath="src/main/java/com/unesp/ri/vocabulary/en-us/stopwords.txt";

    public ArrayList<String> getTerms(String raw) throws IOException {
        // gets a file and returns a list of terms
        ArrayList<String> terms = new ArrayList<String>();
        String refinedInput = raw.replaceAll("[^a-zA-Z|.|@]+", "\n").replaceAll("\\n+", " ");

        // apply lemma
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = pipeline.processToCoreDocument(refinedInput);
        
        // read stop words txt file and put it in a list
        List<String> stopWordList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Add each word to the list
                stopWordList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (CoreLabel tok : document.tokens()) {
            String word = tok.word();
            if (word.length() > 0 && !word.matches("[0-9]+")) {
                String lema = tok.lemma().toLowerCase();
                if (!stopWordList.contains(lema) && lema.length() > 1) {
                    terms.add(lema);
                }
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
