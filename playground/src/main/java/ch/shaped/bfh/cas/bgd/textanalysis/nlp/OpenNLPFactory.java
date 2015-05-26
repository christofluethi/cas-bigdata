package ch.shaped.bfh.cas.bgd.textanalysis.nlp;

import com.google.common.io.Closeables;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by christof on 5/26/15.
 */
public class OpenNLPFactory {
    private static Logger logger = Logger.getLogger(OpenNLPFactory.class.getName());

    public static final String DEFAULT_SENTENCE_DETECTOR_MODEL = "playground/src/main/resources/en-sent.bin";
    public static final String DEFAULT_NER_PERSON_MODEL = "playground/src/main/resources/en-ner-person.bin";
    public static final String DEFAULT_TOKENIZER_MODEL = "playground/src/main/resources/en-token.bin";



    /**
     * Returns default EN Person NameFinder
     * @return
     * @throws InstantiationException
     */
    public static TokenNameFinder createNameFinder() throws InstantiationException {
      return OpenNLPFactory.createNameFinder(new File(DEFAULT_NER_PERSON_MODEL));
    }

    public static TokenNameFinder createNameFinder(File model) throws InstantiationException {
        NameFinderME nameFinder = null;
        InputStream stream = null;

        try {
            stream = new FileInputStream(model.getAbsoluteFile());
            nameFinder = new NameFinderME(new TokenNameFinderModel(stream));
        } catch (IOException e) {
            logger.error("Model '"+model+"' not found.");
        } finally {
            Closeables.closeQuietly(stream);
        }

        if (nameFinder == null) {
            throw new InstantiationException("Couldn't load NameFinder class");
        }

        return nameFinder;
    }



    /**
     * Returns default EN Tokenizer
     * @return
     * @throws InstantiationException
     */
    public static Tokenizer createTokenizer() throws InstantiationException {
        return OpenNLPFactory.createTokenizer(new File(DEFAULT_TOKENIZER_MODEL));
    }

    public static Tokenizer createTokenizer(File model) throws InstantiationException {
        TokenizerME tokenizer = null;
        InputStream stream = null;

        try {
            stream = new FileInputStream(model.getAbsoluteFile());
            tokenizer = new TokenizerME(new TokenizerModel(stream));
        } catch (IOException e) {
            logger.error("Model '"+model+"' not found.");
        } finally {
            Closeables.closeQuietly(stream);
        }
        if (tokenizer == null) {
            throw new InstantiationException("Couldn't load Tokenizer class");
        }

        return tokenizer;
    }




    /**
     * Returns default EN Tokenizer
     * @return
     * @throws InstantiationException
     */
    public static SentenceDetector createSentenceDetector() throws InstantiationException {
        return OpenNLPFactory.createSentenceDetector(new File(DEFAULT_SENTENCE_DETECTOR_MODEL));
    }

    public static SentenceDetector createSentenceDetector(File model) throws InstantiationException {
        SentenceDetector detector = null;
        InputStream stream = null;

        try {
            stream = new FileInputStream(model.getAbsoluteFile());
            detector = new SentenceDetectorME(new SentenceModel(stream));
        } catch (IOException e) {
            logger.error("Model '"+model+"' not found.");
        } finally {
            Closeables.closeQuietly(stream);
        }

        if (detector == null) {
            throw new InstantiationException("Couldn't load SentenceDetector class");
        }

        return detector;
    }
}
