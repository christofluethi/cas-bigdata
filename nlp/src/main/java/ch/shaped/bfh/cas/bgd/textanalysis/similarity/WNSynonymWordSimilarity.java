package ch.shaped.bfh.cas.bgd.textanalysis.similarity;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by christof on 5/26/15.
 */
public class WNSynonymWordSimilarity implements WordSimilarityStrategy {
    private static Logger logger = Logger.getLogger(WNSynonymWordSimilarity.class.getName());

    private Dictionary dictionary;

    public WNSynonymWordSimilarity(String file) throws InstantiationException {
        try {
            JWNL.initialize(new FileInputStream((new File(file).getAbsoluteFile())));
            this.dictionary = Dictionary.getInstance();
        } catch(IOException | JWNLException e) {
            throw new InstantiationException("Could not instantiate class WNSynonymWordSimilarity");
        }
    }

    @Override
    public double getSimilarity(String word1, String word2) {
        if(word1 == null || word1.length() == 0 || word2 == null || word2.length() == 0) {
            return 0.0;
        }

        if(word1.equalsIgnoreCase(word2)) {
            return 1.0;
        }

        double rval = 0.0;
        try {
            Set<String> synWords1 = getSynonym(word1);
            if(synWords1.isEmpty()) {
                return rval;
            }

            Set<String> synWords2 = getSynonym(word2);
            if(synWords2.isEmpty()) {
                return rval;
            }

            int totalSynonym = synWords1.size() + synWords2.size();
            synWords1.retainAll(synWords2);
            int intersection = synWords1.size();

            /*for (String synWord1 : synWords1) {
                for (String synWord2 : synWords2) {
                    countAll++;
                    if(synWord1.equalsIgnoreCase(synWord2)) {
                        countEqual++;
                    }
                }
            }*/

            rval = intersection / (double)totalSynonym;
        } catch(JWNLException e) {
            logger.error("Cannot get similarity for '"+word1+"' and '"+word2+"'", e);
           return rval;
        }

        return rval;
    }

    private Set<String> getSynonym(String word) throws JWNLException {
        Set<String> synonyms = new HashSet<String>();
        List<IndexWord> indexWords = this.getIndexWords(word);
        for (IndexWord indexWord : indexWords) {
            Synset[] synsets = indexWord.getSenses();
            for (Synset synset : synsets) {
                Word[] words = synset.getWords();
                for (Word w : words) {
                    synonyms.add(w.getLemma());
                }
            }
        }

        logger.debug("Found "+synonyms.size()+" synonyms for word "+word+": "+synonyms.toString());

        return synonyms;
    }

    private List<IndexWord> getIndexWords(String word) throws JWNLException {
        List<IndexWord> iwlist = new ArrayList<>();
        List<POS> posList = POS.getAllPOS();
        for (POS pos : posList) {
            IndexWord iw = dictionary.getIndexWord(pos, word);
            if(iw != null) {
                iwlist.add(iw);
            }
        }

        return iwlist;
    }
}
