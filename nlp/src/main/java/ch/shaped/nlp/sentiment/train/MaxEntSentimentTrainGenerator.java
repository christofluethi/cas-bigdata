package ch.shaped.nlp.sentiment.train;

import ch.shaped.nlp.sentiment.WordlistBasedSentimentAnalyzer;
import ch.shaped.nlp.trainer.OpenNLPTrainer;
import ch.shaped.nlp.util.OpenNLPFactory;
import com.google.common.io.Files;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.commons.io.Charsets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christof on 6/2/15.
 */
public class MaxEntSentimentTrainGenerator extends OpenNLPTrainer {

    private Map<String, File> categories = new HashMap<String, File>();
    private Tokenizer tokenizer = null;

    public MaxEntSentimentTrainGenerator(Map<String, File> categories, Tokenizer tokenizer) {
        this.categories = categories;
        this.tokenizer = tokenizer;
    }

    public void trainToModel(File trainDataOut, File modelOut) {
        this.writeModelTrainFile(trainDataOut);
        this.writeModelFile(trainDataOut, modelOut);
    }

    private void writeModelTrainFile(File trainDataOut) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(trainDataOut);

            for (Map.Entry<String, File> categoryEntry : categories.entrySet()) {
                String category = categoryEntry.getKey();
                File src = categoryEntry.getValue();

                try {
                    for (String s : Files.readLines(src, Charsets.UTF_8)) {
                        String[] words = tokenizer.tokenize(s);
                        StringBuffer sb = new StringBuffer();
                        for (String word : words) {
                            if(!word.trim().isEmpty()) {
                                sb.append(" " + word);
                            }
                        }

                        if(sb.toString().trim().length() > 0) {
                            pw.write(category+" "+sb.toString().trim()+"\n");
                        }
                    }
                } catch (IOException e) {
                /* ignore */
                }
            }
        } catch (FileNotFoundException e) {
            /* ignore */
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }



    public static void main(String[] args) throws InstantiationException, IOException {
        Tokenizer tokenizer = OpenNLPFactory.createTokenizer();
        Map<String, File> categories = new HashMap<>();
        categories.put("POSITIVE", new File(WordlistBasedSentimentAnalyzer.SENTENCE_POSITIVE));
        categories.put("NEGATIVE", new File(WordlistBasedSentimentAnalyzer.SENTENCE_NEGATIVE));

        MaxEntSentimentTrainGenerator stg = new MaxEntSentimentTrainGenerator(categories, tokenizer);
        stg.trainToModel(new File("nlp/src/main/resources/sentiments-gen/en-sentiments.train"), new File("nlp/src/main/resources/sentiments-gen/en-sentiments.bin"));
    }
}
