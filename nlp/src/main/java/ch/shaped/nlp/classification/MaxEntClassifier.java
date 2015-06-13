package ch.shaped.nlp.classification;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRDoc;
import ch.shaped.nlp.util.OpenNLPFactory;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by christof on 6/3/15.
 */
public class MaxEntClassifier {
    private static Logger logger = Logger.getLogger(MaxEntClassifier.class.getName());

    public static void main(String[] args) throws InstantiationException, IOException {
        if (args == null || args.length < 1) {
            System.err.println("Usage: java -jar nlp.jar DCRXMLDirectory");
            System.exit(1);
        }

        File xmls = new File(args[0]);
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(xmls, new String[]{"xml"});
        DocumentCategorizerME cat = OpenNLPFactory.createCategorizer(new File("nlp/src/main/resources/sentiments-gen/de-drsk.bin"));

        int i = 0;
        System.out.println("Start processing a total of #" + files.size() + " entries.");

        int total = 0;
        int correct = 0;
        for (File file : files) {
            i++;
            try {
                DCRDoc dcrdoc = new DCRDoc(file);

                String doclang = null;
                String category = null;
                if (dcrdoc.getMetainfo().containsKey("doclang")) {
                    doclang = dcrdoc.getMetainfo().get("doclang").get(0);
                }

                if (dcrdoc.getMetainfo().containsKey("category")) {
                    category = dcrdoc.getMetainfo().get("category").get(0);
                }


                if (doclang != null && category != null && doclang.equalsIgnoreCase("de") && !category.isEmpty()) {
                    total++;
                    double[] outcome = cat.categorize(dcrdoc.getContent().getPlainText());
                    String bestcat = cat.getBestCategory(outcome).toLowerCase();
                    if (bestcat.equals(category.toLowerCase())) {
                        correct++;
                    }
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

        System.out.println("analyzed " + total + " positive sentences of which " + correct + " were categorized correctly: " + (100.0 / total * correct) + "%");
    }
}
