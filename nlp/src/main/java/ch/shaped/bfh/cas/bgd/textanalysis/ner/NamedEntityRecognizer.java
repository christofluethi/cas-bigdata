package ch.shaped.bfh.cas.bgd.textanalysis.ner;

import ch.shaped.bfh.cas.bgd.textanalysis.nlp.OpenNLPFactory;
import com.google.common.io.Files;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christof on 5/26/15.
 */
public class NamedEntityRecognizer {

    /**
     * Extract all named Entities from a given File
     * @param file File to extract NER
     * @param tokenizer String Tokenizer
     * @param nerFinders List of ToenNameFinder algorithms to apply
     * @param sentenceDetector SentenceDetector
     * @throws IOException Thrown if input file is not found
     */
    public void extract(File file, SentenceDetector sentenceDetector, Tokenizer tokenizer, List<TokenNameFinder> nerFinders) throws IOException {
        String[] sentence = sentenceDetector.sentDetect(Files.toString(file, Charsets.UTF_8));
        for (String s : sentence) {
            String[] words = tokenizer.tokenize(s);
            for (TokenNameFinder nerFinder : nerFinders) {
                Span nameSpans[] = nerFinder.find(words);
                for (Span nameSpan : nameSpans) {
                    String name = "";
                    for (int i = nameSpan.getStart(); i < nameSpan.getEnd(); i++) {
                        name += words[i]+" ";
                    }
                    System.out.println(nameSpan.getType()+": "+name.trim());
                }
            }
        }
    }


    public static void main(String[] args) throws InstantiationException, IOException {
        if (args == null || args.length != 1) {
            System.out.println("Usage: file");
            System.exit(1);
        }

        NamedEntityRecognizer ner = new NamedEntityRecognizer();

        SentenceDetector detector = OpenNLPFactory.createSentenceDetector();
        Tokenizer tokenizer = OpenNLPFactory.createTokenizer();
        List<TokenNameFinder> nerFinders = new ArrayList<>();
        nerFinders.add(OpenNLPFactory.createNameFinder());

        ner.extract(new File(args[0]), detector, tokenizer, nerFinders);
    }
}
