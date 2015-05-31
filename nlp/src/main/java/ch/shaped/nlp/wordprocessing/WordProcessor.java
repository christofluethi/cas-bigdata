package ch.shaped.nlp.wordprocessing;

import ch.shaped.nlp.statistics.TermDistribution;
import ch.shaped.nlp.util.PorterStemmer;
import ch.shaped.nlp.util.StopWordsFilter;
import com.google.common.io.Files;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by christof on 5/19/15.
 */
public class WordProcessor {

    private String path = null;
    private StopWordsFilter stopWords;

    public WordProcessor(String input) throws IOException {
        this.path = input;
        this.stopWords = new StopWordsFilter();
    }

    public static void main(String[] args) throws IOException {
        if(args == null || args.length < 1) {
            System.out.println("Usage: <program> inputfile");
            System.exit(1);
        }

        WordProcessor ss = new WordProcessor(args[0]);
        System.out.println("--- Original ------------------------");
        ss.runOriginal();
        System.out.println("--- End Original --------------------");
        System.out.println("--- Lowercase -----------------------");
        ss.runLowercase();
        System.out.println("--- End Lowercase -------------------");
        System.out.println("--- Stopwords -----------------------");
        ss.runStopwords();
        System.out.println("--- End Stopwords -------------------");
        System.out.println("--- Stemming ------------------------");
        ss.runStem();
        System.out.println("--- End Stemming --------------------");
    }

    public void runOriginal() throws IOException {
        StringTokenizer s = new StringTokenizer(Files.toString(new File(this.path), Charsets.UTF_8));
        TermDistribution td = new TermDistribution();
        while(s.hasMoreTokens()) {
            String token = s.nextToken();
            if(token != null) {
                td.addTerm(token);
            }
        }

        td.printFrequencyDistribution();
    }

    public void runLowercase() throws IOException {
        StringTokenizer s = new StringTokenizer(Files.toString(new File(this.path), Charsets.UTF_8));
        TermDistribution td = new TermDistribution();
        while(s.hasMoreTokens()) {
            String token = s.nextToken();
            if(token != null) {
                td.addTerm(token.toLowerCase());
            }
        }

        td.printFrequencyDistribution();
    }

    public void runStopwords() throws IOException {
        StringTokenizer s = new StringTokenizer(Files.toString(new File(this.path), Charsets.UTF_8));
        TermDistribution td = new TermDistribution();
        while (s.hasMoreTokens()) {
            String token = s.nextToken().toLowerCase();
            if (!this.stopWords.isStopWord(token)) {
                td.addTerm(token);

            }
        }

        td.printFrequencyDistribution();
    }

    public void runStem() throws IOException {
        StringTokenizer s = new StringTokenizer(Files.toString(new File(this.path), Charsets.UTF_8));
        TermDistribution td = new TermDistribution();
        while (s.hasMoreTokens()) {
            String token = s.nextToken().toLowerCase();
            if (!this.stopWords.isStopWord(token)) {
                PorterStemmer ps = new PorterStemmer();
                for (int i = 0; i < token.length(); ++i) {
                    ps.add(token.charAt(i));
                }
                ps.stem();
                td.addTerm(ps.toString());

            }
        }

        td.printFrequencyDistribution();
    }
}
