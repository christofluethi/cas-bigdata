# CAS BigData
* Lecture code
* OrientDB code
* Neo4j code
* Model for dcrxml imports to graphdb
* NLP Code
* Utils 

# NLP
## Positive and Negative Sentiment Analysis (Classifying)

* http://www.cs.uic.edu/~liub/FBS/sentiment-analysis.html
* Wordlist: http://www.cs.uic.edu/~liub/FBS/opinion-lexicon-English.rar
* Test/Training set: http://www.cs.uic.edu/~liub/FBS/pros-cons.rar

```
-- Wordlist Based ----
analyzed 22941 positive sentences of which 17362 were categorized correctly: 75.68109498278191%
analyzed 22936 negative sentences of which 9933 were categorized correctly: 43.307464248343216%
```

```
-- OpenNLP Maximum Entropy (identical training and test) ----
analyzed 22941 positive sentences of which 21616 were categorized correctly: 94.22431454600932%
analyzed 22936 negative sentences of which 21359 were categorized correctly: 93.12434600627834%
```

## Classification of CH-Law-Documents
Automatically assign category based on keywords and legislation cites.

* Data: decisions from BGer and BVGer
* Training set: 15000 documents
* Test set: 16474 documents

```
analyzed 16474 documents of which 14784 were categorized correctly: 89.74141070778197%
```

# Named-entity recognition (NER)
* NER using the OpenNLP `en-ner-person.bin` model
* Sentence Detection using the OpenNLP `en-sent.bin` model
* Tokenized using the OpenNLP `en-token.bin` model
* Class: NamedEntityRecognizer
* Test data: Section `1976–84: Founding and incorporation` on Apple Inc wikipedia Article

```
person: Steve Jobs
person: Steve Wozniak
person: Ronald Wayne
person: Wozniak
person: RAM
person: Wayne
person: Mike Markkula
```

# GraphDB
## Neo4j
![Neo4j Graph](/neo4j/src/main/resources/neo4j-sample.png?raw=true "Neo4j Graph")

## OrientDB
![OrientDB Graph](/orientdb/src/main/resources/orientdb-sample.png?raw=true "OrientDB Graph")
