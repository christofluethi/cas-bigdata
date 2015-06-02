package ch.shaped.nlp.sentiment;

import ch.shaped.nlp.util.OpenNLPFactory;
import ch.shaped.nlp.util.WordListFactory;
import com.google.common.io.Files;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by christof on 6/2/15.
 */
public class WordlistBasedSentimentAnalyzer {
    public static final String SENTENCE_POSITIVE = "nlp/src/main/resources/sentence-plain-positive.txt";
    public static final String SENTENCE_NEGATIVE = "nlp/src/main/resources/sentence-plain-negative.txt";

    private Set<String> positive = new HashSet<String>();
    private Set<String> negative = new HashSet<String>();
    private Tokenizer tokenizer;

    public WordlistBasedSentimentAnalyzer(Set<String> pos, Set<String> neg, Tokenizer tokenizer) {
        this.positive = pos;
        this.negative = neg;
        this.tokenizer = tokenizer;
    }

    /**
     * Extract all named Entities from a given File
     *
     * @param input             File to extract NER
     * @throws IOException Thrown if input file is not found
     */
    public Sentiment analyze(String input) throws IOException {
        /*String[] sentence = sentenceDetector.sentDetect(input);*/
        int neg = 0;
        int pos = 0;

        for (String s : new String[]{input.toLowerCase()}) {
            String[] words = tokenizer.tokenize(s);
            for (String word : words) {
                if(this.negative.contains(word)) {
                    neg++;
                }

                if(this.positive.contains(word)) {
                    pos++;
                }
            }
        }

        if(neg == pos) {
            return Sentiment.NEUTRAL;
        } else if(pos > neg) {
            return Sentiment.POSITIVE;
        } else {
            return Sentiment.NEGATIVE;
        }
    }


    public static void main(String[] args) throws InstantiationException, IOException {
        List<String> pos = WordListFactory.getPositiveWords();
        List<String> neg = WordListFactory.getNegativeWords();
        Tokenizer tokenizer = OpenNLPFactory.createTokenizer();

        WordlistBasedSentimentAnalyzer sa = new WordlistBasedSentimentAnalyzer(new HashSet<String>(pos), new HashSet<String>(neg), tokenizer);
        int posTotal = 0;
        int posCorrect = 0;
        for (String s  : Files.readLines(new File(SENTENCE_POSITIVE), Charsets.UTF_8)) {
            posTotal++;
            if(sa.analyze(s).equals(Sentiment.POSITIVE)) {
                posCorrect++;
            }
        }
        System.out.println("analyzed "+posTotal+" positive sentences of which "+posCorrect+" were categorized correctly: "+(100.0/posTotal * posCorrect)+"%");

        int negTotal = 0;
        int negCorrect = 0;
        for (String s  : Files.readLines(new File(SENTENCE_NEGATIVE), Charsets.UTF_8)) {
            negTotal++;
            if(sa.analyze(s).equals(Sentiment.NEGATIVE)) {
                negCorrect++;
            }
        }
        System.out.println("analyzed "+negTotal+" negative sentences of which "+negCorrect+" were categorized correctly: "+ (100.0/negTotal * negCorrect)+"%");
    }
}
