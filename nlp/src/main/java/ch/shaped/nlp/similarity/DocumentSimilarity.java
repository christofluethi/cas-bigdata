package ch.shaped.nlp.similarity;

import ch.shaped.nlp.util.OpenNLPFactory;
import ch.shaped.nlp.util.StopWordsFilter;
import com.google.common.io.Files;
import net.didion.jwnl.JWNLException;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by christof on 5/26/15.
 */
public class DocumentSimilarity {
    private static Logger logger = Logger.getLogger(DocumentSimilarity.class.getName());

    public static final String WORDNET_CONFIG = "playground/src/main/resources/file_properties.xml";

    private WordSimilarityStrategy similarityStrategy;
    private Tokenizer tokenizer;
    private StopWordsFilter stopWordsFilter;


    public DocumentSimilarity(Tokenizer tokenizer, StopWordsFilter stopwords, WordSimilarityStrategy simStrategy) {
        this.tokenizer = tokenizer;
        this.stopWordsFilter = stopwords;
        this.similarityStrategy = simStrategy;
    }



    public double computeSimilarity(File f1, File f2) {
        double totalSimilarty = 0.0;
        int checks = 0;

        try {
            Collection<String> doc1 = getStopWordFilter(tokenize(f1), false);
            Collection<String> doc2 = getStopWordFilter(tokenize(f2), false);

            dumpWords(doc1, doc2);

            for (String l : doc1) {
                for (String s : doc2) {
                    checks++;
                    double wordSimilarity = this.similarityStrategy.getSimilarity(l, s);

                    logger.debug(String.format("%.6f  %15s %15s", wordSimilarity, l, s));
                    totalSimilarty += wordSimilarity;
                }
            }
        } catch (IOException e) {
            logger.error("File not found. ", e);
        }

        return totalSimilarty / (double)checks;
    }


    private String[] tokenize(File f) throws IOException {
        return tokenizer.tokenize(Files.toString(f, Charsets.UTF_8).toLowerCase());
    }


    private Collection<String> getStopWordFilter(String[] words, boolean unique) {
        List<String> collection = new ArrayList<>();
        for (String s : words) {
            if (!stopWordsFilter.isStopWord(s)) {
                collection.add(s);
            }
        }

        if(unique) {
            return new HashSet<>(collection);
        } else {
            return collection;
        }
    }


    public void dumpWords(Collection<String> first, Collection<String> second) {
        logger.debug("Document One Words:");
        for (String s : first) {
            logger.debug(s);
        }
        logger.debug("Total: " + first.size()+"\n\n");

        logger.debug("Document Two Words:");
        for (String s : second) {
            logger.debug(s);
        }
        logger.debug("Total: " + second.size());
    }


    public static void main(String[] args) throws InstantiationException, IOException, JWNLException {
        if (args == null || args.length != 2) {
            System.out.println("Usage: reference candidate");
            System.exit(1);
        }

        WordSimilarityStrategy simStrategy = new WNSynonymWordSimilarity(WORDNET_CONFIG);

        Tokenizer tokenizer = OpenNLPFactory.createTokenizer();

        StopWordsFilter swf = new StopWordsFilter();
        swf.addStopWord(new String[]{".", ",", "!", "?", "-", ";"});

        DocumentSimilarity ds = new DocumentSimilarity(tokenizer, swf, simStrategy);
        double sim = ds.computeSimilarity(new File(args[0]), new File(args[1]));
        logger.info("Similarity: "+sim);
    }
}
