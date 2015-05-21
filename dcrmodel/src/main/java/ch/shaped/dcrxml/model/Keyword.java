package ch.shaped.dcrxml.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by christof on 5/15/15.
 */
public class Keyword {
    private int id = 0;
    private Map<String, String> keywords = new HashMap<String, String>();
    private double score = 0.0;

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public void setLang(String lang, String value) {
        if(lang != null && value != null && !lang.isEmpty() && !value.isEmpty()) {
            keywords.put(lang, value);
        }
    }

    public String getKeyword(String lang) {
        if(lang != null) {
            if (keywords.containsKey(lang)) {
                return this.keywords.get(lang);
            }
        }

        return null;
    }

    public void setScore(String score) {
        if(score != null) {
            try {
                this.score = Double.parseDouble(score);
            } catch(NumberFormatException e) {
                /* ignore */
            }
        }
    }

    public void setId(String id) {
        if(id != null) {
            try {
                this.id = Integer.parseInt(id);
            } catch(NumberFormatException e) {
                /* ignore */
            }
        }
    }


    public void setScore(double score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }
}
