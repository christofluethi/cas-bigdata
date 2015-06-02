package ch.shaped.nlp.sentiment.train;

import ch.shaped.nlp.sentiment.WordlistBasedSentimentAnalyzer;
import ch.shaped.nlp.util.OpenNLPFactory;
import com.google.common.io.Files;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.apache.commons.io.Charsets;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christof on 6/2/15.
 */
public class MaxEntSentimentTrainGenerator {

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

    public void writeModelFile(File trainModelIn, File modelOut) {
        DoccatModel model = null;

        InputStream dataIn = null;
        OutputStream os = null;
        try {
            dataIn = new FileInputStream(trainModelIn);
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            model = DocumentCategorizerME.train("en", sampleStream);
            os = new BufferedOutputStream(new FileOutputStream(modelOut));
            model.serialize(os);
        } catch (IOException e) {
            // Failed to read or parse training data, training failed
            e.printStackTrace();
        } finally {
            if (dataIn != null) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    // Not an issue, training already finished.
                    // The exception should be logged and investigated
                    // if part of a production system.
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch(IOException e) {
                    /* ignore */
                }
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
