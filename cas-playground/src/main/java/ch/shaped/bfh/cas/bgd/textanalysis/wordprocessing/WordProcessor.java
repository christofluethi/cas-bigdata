package ch.shaped.bfh.cas.bgd.textanalysis.wordprocessing;

import ch.shaped.bfh.cas.bgd.textanalysis.nlp.PorterStemmer;
import ch.shaped.bfh.cas.bgd.textanalysis.nlp.StopWords;
import ch.shaped.bfh.cas.bgd.textanalysis.statistics.TermDistribution;
import com.google.common.io.Files;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by christof on 5/19/15.
 */
public class WordProcessor {
    private TermDistribution tf = new TermDistribution();
    private String path = null;
    private StopWords stopWords;

    public WordProcessor(String input) throws IOException {
        this.path = input;
        this.stopWords = new StopWords();

    }

    public static void main(String[] args) throws IOException {
        if(args == null || args.length < 1) {
            System.out.println("Usage: <program> inputfile");
            System.exit(1);
        }

        String inputfile = args[0];

        WordProcessor ss = new WordProcessor(inputfile);
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
            String token = s.nextToken().toLowerCase();
            if(token != null) {
                td.addTerm(token);
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

   /* public static void dumpFrequency(String heading, Map<String, Integer> freq, int amount) {
        System.out.println("-------------------------\n"+heading+"\n-------------------------");
        ValueComparator bvc =  new ValueComparator(freq);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);

        sorted_map.putAll(freq);

        int count = 0;
        for(Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
            System.out.println(String.format("%5d %s", entry.getValue(), entry.getKey()));
            count ++;
            if(count > amount) {
                break;
            }
        }
    }
*/

    /*static class ValueComparator implements Comparator<String> {

        Map<String, Integer> base;
        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }*/
}
