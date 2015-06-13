package ch.shaped.dcrxml.model;

import org.apache.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by christof on 5/15/15.
 */
public class DCRDoc {
    private static Logger logger = Logger.getLogger(DCRDoc.class.getName());

    public static final String DCR_METAINFO = "metainfo";
    public static final String DCR_TEXT = "text";
    public static final String DCR_DESCRIPTOR = "desc";
    public static final String DCR_ITEM = "item";

    public static final String DCR_LEID = "leid";
    public static final String DCR_CATEGORY = "category";

    public static final String DCR_ID = "id";
    public static final String DCR_SCORE = "score";
    public static final String DCR_LANG = "lang";
    public static final String DCR_NAME = "name";
    public static final String DCR_LINKSREF = "linksref";

    public static final String DCR_LSLEXREF = "lslexrefs";

    private boolean inMetainfo = false;
    private boolean inDescriptor = false;
    private boolean inLslexrefs = false;

    private int level = 0;

    private TextContent content;
    private int contentStart = -1;
    private int contentEnd = -1;

    private Map<String, List<String>> metainfo = new HashMap<String, List<String>>();
    private List<Keyword> keywords = new ArrayList<Keyword>();
    private List<Reference> references = new ArrayList<Reference>();
    private Keyword currentKeyword = null;
    private String currentLexrefType = null;

    public Map<String, List<String>> getMetainfo() {
        return metainfo;
    }
    public List<Keyword> getKeywords() {
        return keywords;
    }
    public List<Reference> getReferences() {
        return references;
    }

    private File input;

    public DCRDoc(File f) throws FileNotFoundException, InstantiationException {
        try {
            this.input = f;
            this.initFromXML(f);
        } catch(XMLStreamException e) {
            throw new InstantiationException("XMLStreamException while parsing xml: "+e.getMessage());
        }
    }

    public TextContent getContent() throws InstantiationException {
        return new TextContent(this.input, this.contentStart, this.contentEnd);
    }

    public String getTitle() {
        String title = null;
        if(metainfo != null) {
            title = metainfo.get(DCR_LEID).get(0);

            if(metainfo.containsKey("title")) {
                title = metainfo.get("title").get(0);
            } else if(metainfo.containsKey("title_forced")) {
                title = metainfo.get("title_forced").get(0);
            } else if(metainfo.containsKey("magnoliaTitle")) {
                title = metainfo.get("magnoliaTitle").get(0);
            } else if(metainfo.containsKey("url")) {
                title = metainfo.get("url").get(0);
            }
        }

        return title;
    }

    public String getSubjectArea() {
        String area = "UnknownArea";
        if(metainfo != null) {
            if(metainfo.containsKey(DCR_CATEGORY)) {
                area = metainfo.get(DCR_CATEGORY).get(0);
            } else if(metainfo.containsKey("label_de")) {
                area = metainfo.get("label_de").get(0);
            }
        }

        return area;
    }

    public String getSource() {
        String source = "UnknownSource";
        if(metainfo != null) {
            if(metainfo.containsKey("source_de")) {
                source = metainfo.get("source_de").get(0);
            } else if(metainfo.containsKey("publication")) {
                source = metainfo.get("publication").get(0);
            } else if(metainfo.containsKey("dataSource")) {
                source = metainfo.get("dataSource").get(0);
            }
        }

        return source;
    }

    public String getState() {
        String state = "UNKNOWN";
        if(metainfo != null) {
            state = metainfo.get("state").get(0);
        }

        return state;
    }


    public String getLanguage() {
        String lang = "UNKNOWN";
        if(metainfo != null) {
            lang = metainfo.get("doclang").get(0);
        }

        return lang;
    }

    public String getUrl() {
        String url = null;
        if (this.metainfo != null && this.metainfo.containsKey("url")) {
            url = metainfo.get("url").get(0);
        }

        return url;
    }


    protected void handleStartElement(String element, XMLStreamReader xmlReader) throws XMLStreamException {

        if (inLslexrefs) {
            level++;
            if (level == 1) {
                this.currentLexrefType = element;
            }
        }

        if(inMetainfo) {
            String valueRaw = xmlReader.getElementText();
            List<String> mi = new ArrayList<String>();
            for (String s :valueRaw.split("[\\t]")) {
                mi.add(s);
            }
            this.metainfo.put(element, mi);
        } else if (element.equals(DCR_LSLEXREF)) {
            inLslexrefs = true;
            level = 0;
        } else if (element.equals(DCR_TEXT)) {
            contentStart = xmlReader.getLocation().getCharacterOffset();
        } else if(element.equals(DCR_METAINFO)) {
                this.inMetainfo = true;
        } else if(element.equals(DCR_DESCRIPTOR)) {
            this.inDescriptor = true;
            this.currentKeyword = new Keyword();
            int attribute = xmlReader.getAttributeCount();
            for (int i = 0; i < attribute; i++) {
                String name = xmlReader.getAttributeLocalName(i);
                String value = xmlReader.getAttributeValue(i);
                if(name.equalsIgnoreCase(DCR_ID)) {
                    this.currentKeyword.setId(value);
                } else if(name.equalsIgnoreCase(DCR_SCORE)) {
                    this.currentKeyword.setScore(value);
                }
            }
        } else if(element.equals(DCR_ITEM) && inLslexrefs) {
            Reference r = new Reference(currentLexrefType);

            int attribute = xmlReader.getAttributeCount();
            for (int i = 0; i < attribute; i++) {
                String name = xmlReader.getAttributeLocalName(i);
                String value = xmlReader.getAttributeValue(i);
                if(name.equalsIgnoreCase(DCR_ID)) {
                    r.setId(value);
                } else if(name.equalsIgnoreCase(DCR_SCORE)) {
                    r.setScore(value);
                } else if(name.equalsIgnoreCase(DCR_NAME)) {
                    r.setName(value);
                } else if(name.equalsIgnoreCase(DCR_LINKSREF)) {
                    r.setLinksref(value);
                }
            }
            this.references.add(r);
        } else if(inDescriptor && element.equals(DCR_LANG)) {
            int attribute = xmlReader.getAttributeCount();
            String langId = null;
            for (int i = 0; i < attribute; i++) {
                String name = xmlReader.getAttributeLocalName(i);
                String value = xmlReader.getAttributeValue(i);
                if(name.equalsIgnoreCase(DCR_ID)) {
                    langId = value;
                    break;
                }
            }
            this.currentKeyword.setLang(langId, xmlReader.getElementText());
        }
    }


    protected void handleEndElement(String element, XMLStreamReader xmlReader) {
        if (inLslexrefs) {
            level--;
        }

        if(element.equals(DCR_METAINFO)) {
            this.inMetainfo = false;
        } else if (element.equals(DCR_TEXT)) {
            contentEnd = xmlReader.getLocation().getCharacterOffset()-(DCR_TEXT.length()+4);
        } else if (element.equals(DCR_LSLEXREF)) {
            inLslexrefs = false;
        } else if(element.equals(DCR_DESCRIPTOR)) {
            this.keywords.add(this.currentKeyword);
            this.currentKeyword = null;
            this.inDescriptor = false;
        }
    }


    private void initFromXML(File dcrxml) throws FileNotFoundException, XMLStreamException{
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(dcrxml));
        while (xmlReader.hasNext()) {
            String elementName;
            int event = xmlReader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    elementName = xmlReader.getName().getLocalPart();
                    handleStartElement(elementName, xmlReader);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    elementName = xmlReader.getName().getLocalPart();
                    handleEndElement(elementName, xmlReader);
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    xmlReader.close();
                    break;
                default:
                    break;
            }
        }
    }
}
