package ch.shaped.dcrxml.model;

import org.apache.log4j.Logger;

/**
 * Created by christof on 5/16/15.
 */
public class Reference {
    private static Logger logger = Logger.getLogger(Reference.class.getName());

    private String type;
    private String name;
    private String linksref;

    private String category;
    private String subcategory;
    private String area;

    private int id;
    private double score;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLinksref() {
        return linksref;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getArea() {
        return area;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public Reference(String type) {
        this.setType(type);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLinksref(String linksref) {
        this.linksref = linksref;
    }

    public void setType(String type) {
        this.type = type;
        switch(type) {
            case "jud-ch-bund-bge":
                this.category = "BGer";
                this.subcategory = "Major Decision";
                this.area = "CH";
                break;
            case "jud-ch-bund-bgu":
                this.category = "BGer";
                this.subcategory = "Decision";
                this.area = "CH";
                break;
            case "jud-ch-bund-bvge":
                this.category = "BVGer";
                this.subcategory = "Major Decision";
                this.area = "CH";
                break;
            case "jud-ch-bund-bvger":
                this.category = "BVGer";
                this.subcategory = "Decision";
                this.area = "CH";
                break;
            case "jud-ch-bund-tpf":
                this.category = "BStGer";
                this.subcategory = "Major Decision";
                this.area = "CH";
                break;
            case "jud-ch-bund-bstger":
                this.category = "BStGer";
                this.subcategory = "Decision";
                this.area = "CH";
                break;
            case "lex-ch-bund-sr-h":
                this.category = "Admin";
                this.subcategory = "Edict";
                this.area = "CH";
                break;
            case "lex-int-eu-amtsblatt":
                this.category = "EU Amtsblatt";
                this.subcategory = "Publication";
                this.area = "EU";
                break;
            case "lex-int-eu-richtlinie":
                this.category = "EU Richtlinie";
                this.subcategory = "Edict";
                this.area = "EU";
                break;
            case "lex-int-eu-verordnung":
                this.category = "EU Verordnung";
                this.subcategory = "Edict";
                this.area = "EU";
                break;
            case "jud-int-eu-eugh":
                this.category = "EUGH";
                this.subcategory = "Decision";
                this.area = "EU";
                break;
            case "lex-ch-bund-ab":
                this.category = "CH Amtsblatt";
                this.subcategory = "Publication";
                this.area = "CH";
                break;
            case "lex-ch-bund-as":
                this.category = "CH Amtliche Sammlung";
                this.subcategory = "Publication";
                this.area = "CH";
                break;
            case "lex-ch-bund-bbl":
                this.category = "CH Bundesblatt";
                this.subcategory = "Publication";
                this.area = "CH";
                break;
            case "jud-ch-bund-emark":
                this.category = "ARK";
                this.subcategory = "Decision";
                this.area = "CH";
                break;
            case "jud-ch-gr-kg":
                this.category = "GR KG";
                this.subcategory = "Decision";
                this.area = "CH-GR";
                break;
            case "jud-ch-gr-pkg":
                this.category = "GR KG";
                this.subcategory = "Major Decision";
                this.area = "CH-GR";
                break;
            case "jud-ch-gr-pvg":
                this.category = "GR VG";
                this.subcategory = "Major Decision";
                this.area = "CH-GR";
                break;
            case "jud-ch-gr-vg":
                this.category = "GR VG";
                this.subcategory = "Decision";
                this.area = "CH-GR";
                break;
            case "jud-ch-lu-lgve":
                this.category = "LGVE";
                this.subcategory = "Major Decision";
                this.area = "CH-LU";
                break;
            case "lex-ch-gr-br-h":
                this.category = "GR";
                this.subcategory = "Edict";
                this.area = "CH-GR";
                break;
            case "lex-ch-lu-srl-h":
                this.category = "LU";
                this.subcategory = "Edict";
                this.area = "CH-LU";
                break;
            case "lex-ch-vs-sgs-h":
                this.category = "VS";
                this.subcategory = "Edict";
                this.area = "CH-VS";
                break;
            case "lex-ch-zh-ls-h":
                this.category = "ZH";
                this.subcategory = "Edict";
                this.area = "CH-ZH";
                break;
            case "jud-ch-ag-agve":
                this.category = "AG";
                this.subcategory = "Decision";
                this.area = "CH-AG";
                break;
            case "lit-ch-ajp":
                this.category = "AJP";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-asa":
                this.category = "ASA";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-bn":
                this.category = "Berner Notar";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-ebkbulletin":
                this.category = "EBK Bulletin";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-ebkrs":
                this.category = "EBK Rundschreiben";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-finmars":
                this.category = "FINMA Rundschreiben";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-jusletter":
                this.category = "Jusletter";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-pra":
                this.category = "Praxis";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-recht":
                this.category = "Recht";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-rpw":
                this.category = "RPW";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-sjz":
                this.category = "SJZ";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-vpb":
                this.category = "VPB";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-zbgr":
                this.category = "ZBGR";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-zbjv":
                this.category = "ZBJV";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-zbl":
                this.category = "ZBL";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            case "lit-ch-zwr":
                this.category = "ZWR";
                this.subcategory = "Literature";
                this.area = "CH";
                break;
            default:
                this.category = "Unknown";
                this.subcategory = "Unknown";
                this.area = "Unknown";
                logger.debug("Cannot categorize : "+type);
        }
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setId(int id) {
        this.id = id;
    }
}
