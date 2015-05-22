package ch.shaped.bfh.cas.bgd.textanalysis.nlp;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;

/**
 * Created by christof on 5/22/15.
 */
public class StopWords {
    private HashSet<String> stopwords = null;
    public static final String STOPWORDS_LIST = "cas-playground/src/main/resources/stopwords.txt";

    public StopWords() throws IOException {
        this.stopwords = new HashSet<>(Files.readLines(new File(STOPWORDS_LIST).getAbsoluteFile(), Charsets.UTF_8));
    }

    public StopWords(String file, Charset encoding) throws IOException {
        this.stopwords = new HashSet<>(Files.readLines(new File(file), encoding));
    }

    public boolean isStopWord(String word) {
        return stopwords != null && this.stopwords.contains(word.toLowerCase());
    }

    public String filter(String in) {
        if (stopwords.contains(in.toLowerCase())) {
            return null;
        } else {
            return in;
        }
    }
}
