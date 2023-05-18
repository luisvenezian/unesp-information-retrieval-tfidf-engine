## How to run this project

You must have:
```
Apache Maven  --version >= 3.91 
Java JDK      --version >= 1.8.0_292
```

And then its just run the command below in the same path as the pom.xml:   
```
mvn clean compile exec:java
```

## How does it works? 
It starts an API Spark that's at localhost:4567 where we can add, get, and search values, see the endpoints already created:

#### Returns a rank of documents based on TF-IDF Cossine Similarity 
```GET /search/:query```
#### Returns a list of terms in the given query and its TF-IDF values
```GET /search/validate/:query```

#### Returns a list of terms in the requested document and its TF-IDF values
```GET /document/get/:documentId```

#### Insert a new document in the collection !!! DO NOT RE RUN THE INVERTED INDEX !!!
```POST /document/add/:documentId```

#### Run the indexer and re-calculate the TF-IDF values for all terms in the dictionary, put the new added documents ready to be queried 
```GET /indexer/run/```

#### Useless root endpoint 
```GET /```


I would appreciate if someone could help this project by creating the necessary tests and improving the effiency of its processes, although any kind of help would be very welcome.
