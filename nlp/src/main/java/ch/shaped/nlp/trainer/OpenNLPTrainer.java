package ch.shaped.nlp.trainer;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.*;

/**
 * Created by christof on 6/3/15.
 */
public abstract class OpenNLPTrainer {

    public void writeModelFile(File trainModelIn, File modelOut) {
        DoccatModel model = null;

        InputStream dataIn = null;
        OutputStream os = null;
        try {
            dataIn = new FileInputStream(trainModelIn);
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            model = DocumentCategorizerME.train("en", sampleStream, 5, 100);
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
}
