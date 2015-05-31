package ch.shaped.nlp.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by christof on 5/19/15.
 */
public class TermFrequency {

    public Map<String, Integer> getFrequency(List<String> words) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String w : words) {
            Integer n = map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
        }


        return map;
    }
}
