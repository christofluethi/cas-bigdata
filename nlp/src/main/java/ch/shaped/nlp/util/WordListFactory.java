package ch.shaped.nlp.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christof on 6/2/15.
 */
public class WordListFactory {
    private static Logger logger = Logger.getLogger(WordListFactory.class.getName());

    public static final String DEFAULT_POSITIVE_WORDLIST = "nlp/src/main/resources/positive-words.txt";
    public static final String DEFAULT_NEGATIVE_WORDLIST = "nlp/src/main/resources/negative-words.txt";

    public static List<String> getPositiveWords() {
        return WordListFactory.getLines(DEFAULT_POSITIVE_WORDLIST);
    }

    public static List<String> getNegativeWords() {
        return WordListFactory.getLines(DEFAULT_NEGATIVE_WORDLIST);
    }

    public static List<String> getLines(String file) {
        List<String> list = new ArrayList<String>();
        try {
            list = Files.readLines(new File(file), Charsets.UTF_8);
        } catch (IOException e) {
            logger.error("Model '"+file+"' not found.");
        }

        return list;
    }
}
