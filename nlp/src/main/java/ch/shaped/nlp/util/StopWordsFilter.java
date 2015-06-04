package ch.shaped.nlp.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;

/**
 * Created by christof on 5/22/15.
 */
public class StopWordsFilter {
    private HashSet<String> stopwords = null;
    public static final String STOPWORDS_LIST = "playground/src/main/resources/stopwords.txt";

    public StopWordsFilter(String file) throws IOException {
        this.stopwords = new HashSet<>(Files.readLines(new File(file).getAbsoluteFile(), Charsets.UTF_8));
    }

    public StopWordsFilter() throws IOException {
        this.stopwords = new HashSet<>(Files.readLines(new File(STOPWORDS_LIST).getAbsoluteFile(), Charsets.UTF_8));
    }

    public StopWordsFilter(String file, Charset encoding) throws IOException {
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

    public void addStopWord(String word) {
        if(word != null) {
            stopwords.add(word);
        }
    }

    public void addStopWord(String[] words) {
        if(words != null) {
            for (String word : words) {
                if(word != null) {
                    this.stopwords.add(word);
                }
            }
        }
    }
}
