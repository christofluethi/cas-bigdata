package ch.shaped.nlp.classification;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRXml;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import ch.shaped.nlp.classification.train.NLPJusClassificationTrainGenerator;
import ch.shaped.nlp.util.OpenNLPFactory;
import ch.shaped.nlp.util.StopWordsFilter;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by christof on 6/3/15.
 */
public class MaxEntJusClassifier {
    private static Logger logger = Logger.getLogger(MaxEntJusClassifier.class.getName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws InstantiationException, IOException {
        if (args == null || args.length < 1) {
            System.err.println("Usage: java -jar nlp.jar DCRXMLDirectory");
            System.exit(1);
        }

        StopWordsFilter ignorewords = new StopWordsFilter("nlp/src/main/resources/thes-words-ignore.txt");

        File xmls = new File(args[0]);
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(xmls, new String[]{"xml"});
        DocumentCategorizerME cat = OpenNLPFactory.createCategorizer(new File("nlp/src/main/resources/sentiments-gen/de-jusl-metadata.bin"));

        int i = 0;
        System.out.println("Start processing a total of #" + files.size() + " entries.");

        int total = 0;
        int correct = 0;
        for (File file : files) {
            i++;
            try {
                DCRXml dcrdoc = new DCRXml(file);

                String doclang = null;
                String category = null;
                if (dcrdoc.getMetainfo().containsKey("doclang")) {
                    doclang = dcrdoc.getMetainfo().get("doclang").get(0);
                }

                if (dcrdoc.getMetainfo().containsKey("category")) {
                    category = dcrdoc.getMetainfo().get("category").get(0);
                }


                StringBuffer sb = new StringBuffer();
                if (doclang != null && category != null && doclang.equalsIgnoreCase("de") && !category.isEmpty()) {
                    for (Keyword kw : dcrdoc.getKeywords()) {
                        String word = NLPJusClassificationTrainGenerator.normalizeString(kw.getKeyword("de"));
                        if(!ignorewords.isStopWord(word)) {
                            sb.append(" " + word);
                        }
                    }


                    for (Reference ref : dcrdoc.getReferences()) {
                        String r = ref.getName();
                        try {
                            // use regexp
                            sdf.parse(r);
                            sdf2.parse(r);
                            sdf3.parse(r);
                        } catch(ParseException e) {
//                                String refStr = r.replaceAll("\\s*\\([^\\)]*\\)\\s*", "").replaceAll("[\\.,_: \\/\\\\-]", "");
//                                //System.out.println(r + " => " + refStr);
                            sb.append(" " + NLPJusClassificationTrainGenerator.normalizeString(r));
                        }
                    }
                }
                String out = sb.toString().trim();

                if (doclang != null && category != null && doclang.equalsIgnoreCase("de") && !category.isEmpty()) {
                    total++;
                    double[] outcome = cat.categorize(out);
                    String bestcat = cat.getBestCategory(outcome).toLowerCase();
                    System.out.print("bestcat: " + bestcat + ", is: " + NLPJusClassificationTrainGenerator.normalizeString(category));
                    if (bestcat.equalsIgnoreCase(NLPJusClassificationTrainGenerator.normalizeString(category))) {
                        correct++;
                        System.out.println("       --> OK");
                    } else {
                        System.out.println();
                    }
                    System.out.println("Features ["+out+"]\n");
                }

                if (i % 100 == 0) {
                    System.out.print(".");
                }

                if (i % 10000 == 0) {
                    System.out.println(" | " + i);
                }
            } catch (FileNotFoundException | InstantiationException e) {
                /* ignore */
            }
        }

        System.out.println("\nanalyzed " + total + " documents of which " + correct + " were categorized correctly: " + (100.0 / total * correct) + "%");
    }
}
