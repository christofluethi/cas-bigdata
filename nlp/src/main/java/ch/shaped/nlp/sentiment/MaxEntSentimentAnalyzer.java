package ch.shaped.nlp.sentiment;

import ch.shaped.nlp.util.OpenNLPFactory;
import com.google.common.io.Files;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.IOException;

/**
 * Created by christof on 5/26/15.
 */
public class MaxEntSentimentAnalyzer {


    public static void main(String[] args) throws InstantiationException, IOException {
        MaxEntSentimentAnalyzer ner = new MaxEntSentimentAnalyzer();
        DocumentCategorizerME cat = OpenNLPFactory.createCategorizer(new File("nlp/src/main/resources/sentiments-gen/en-sentiments.bin"));
//        double[] outcome = cat.categorize("this product is very good");
//        String bestcat = cat.getBestCategory(outcome);
//        System.out.println("Categorized as: "+bestcat);

        int posTotal = 0;
        int posCorrect = 0;
        for (String s  : Files.readLines(new File(WordlistBasedSentimentAnalyzer.SENTENCE_POSITIVE), Charsets.UTF_8)) {
            posTotal++;
            double[] outcome = cat.categorize(s);
            String bestcat = cat.getBestCategory(outcome);
            if(bestcat.equals("POSITIVE")) {
                posCorrect++;
            }
        }
        System.out.println("analyzed "+posTotal+" positive sentences of which "+posCorrect+" were categorized correctly: "+(100.0/posTotal * posCorrect)+"%");

        int negTotal = 0;
        int negCorrect = 0;
        for (String s  : Files.readLines(new File(WordlistBasedSentimentAnalyzer.SENTENCE_NEGATIVE), Charsets.UTF_8)) {
            negTotal++;
            double[] outcome = cat.categorize(s);
            String bestcat = cat.getBestCategory(outcome);
            if(bestcat.equals("NEGATIVE")) {
                negCorrect++;
            }
        }
        System.out.println("analyzed "+negTotal+" negative sentences of which "+negCorrect+" were categorized correctly: "+ (100.0/negTotal * negCorrect)+"%");
    }
}
