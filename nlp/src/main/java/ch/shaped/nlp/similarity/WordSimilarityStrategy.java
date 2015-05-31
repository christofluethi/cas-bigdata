package ch.shaped.nlp.similarity;

/**
 * Created by christof on 5/26/15.
 */
public interface WordSimilarityStrategy {

    double getSimilarity(String word1, String word2);
}
