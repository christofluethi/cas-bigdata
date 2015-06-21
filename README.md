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
* Test data: Section `1976–84: Founding and incorporation` on [Apple Inc.](https://en.wikipedia.org/wiki/Apple_Inc.) Wikipedia Article

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

# Apache Spark & Hadoop setup
* http://blog.prabeeshk.com/blog/2014/10/31/install-apache-spark-on-ubuntu-14-dot-04/
* https://rstudio-pubs-static.s3.amazonaws.com/78508_abe89197267240dfb6f4facb361a20ed.html

## Scala
* Download scala 2.11.6 deb file
```
sudo dpkg -i scala-2.11.6.deb
```

version `scala -version`

```
Scala code runner version 2.11.6 -- Copyright 2002-2013, LAMP/EPFL
```

## Apache Hadoop
```
sudo addgroup hadoop
sudo adduser --ingroup hadoop hduser
sudo adduser hduser sudo
```

* download `http://www.motorlogy.com/apache/hadoop/common/current/hadoop-2.7.0.tar.gz`
* extract
* mv hadoop-2.7.0 /usr/local/hadoop 
* 

## Apache Spark
* http://mirror.sdunix.com/apache/spark/spark-1.4.0/spark-1.4.0.tgz 
* unpack tar
* cd spark-1.4.0
* sbt/sbt assembly (may take some time)
