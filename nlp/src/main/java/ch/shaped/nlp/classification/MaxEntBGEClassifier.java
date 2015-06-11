package ch.shaped.nlp.classification;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRXml;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import ch.shaped.nlp.classification.train.NLPJusClassificationTrainGenerator;
import ch.shaped.nlp.util.OpenNLPFactory;
import ch.shaped.nlp.util.StopWordsFilter;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
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
public class MaxEntBGEClassifier {
    private static Logger logger = Logger.getLogger(MaxEntBGEClassifier.class.getName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws InstantiationException, IOException {
        if (args == null || args.length < 1) {
            System.err.println("Usage: java -jar nlp.jar DCRXMLDirectory");
            System.exit(1);
        }

        StopWordsFilter ignorewords = new StopWordsFilter("nlp/src/main/resources/thes-words-ignore.txt");

        List<String> files = Files.readLines(new File("nlp/src/main/resources/sentiments-gen/de-bge-metadata.test"), Charsets.UTF_8);

       // File xmls = new File(args[0]);
       // FileCrawler fc = new FileCrawler();
       // List<File> files = fc.getFileList(xmls, new String[]{"xml"});
        DocumentCategorizerME cat = OpenNLPFactory.createCategorizer(new File("nlp/src/main/resources/sentiments-gen/de-bge-metadata.bin"));

        int i = 0;
        System.out.println("Start processing testset with a total of #" + files.size() + " entries.");

        int total = 0;
        int correct = 0;
        int usable = 0;
        for (String f : files) {
            File file = new File(f);
            i++;
            try {
                DCRXml dcrdoc = new DCRXml(file);

                String doclang = null;
                String category = null;
 /*               if (dcrdoc.getMetainfo().containsKey("doclang")) {
                    doclang = dcrdoc.getMetainfo().get("doclang").get(0);
                }*/

                if (dcrdoc.getMetainfo().containsKey("label_de")) {
                    category = dcrdoc.getMetainfo().get("label_de").get(0);
                }
/*
                if (dcrdoc.getMetainfo().containsKey("source_de")) {
                    if("bger".equalsIgnoreCase(dcrdoc.getMetainfo().get("source_de").get(0))) {
                        continue;
                    }
                }

                if(category == null || category.toLowerCase().contains("beschwerdekammer") || category.toLowerCase().startsWith("bge")) {
                    continue;
                }*/


                StringBuffer sb = new StringBuffer();
                if (category != null && !category.isEmpty()) {
                    for (Keyword kw : dcrdoc.getKeywords()) {
                        if(kw.getKeyword("de") != null) {
                            String word = NLPJusClassificationTrainGenerator.normalizeString(kw.getKeyword("de"));
                            if (!ignorewords.isStopWord(word)) {
                                sb.append(" " + word);
                            }
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
                            sb.append(" " + NLPJusClassificationTrainGenerator.normalizeString(r));
                        }
                    }
                }
                String out = sb.toString().trim();

                if (category != null && !category.isEmpty()) {
                    total++;
                    double[] outcome = cat.categorize(out);
                    String bestcat = cat.getBestCategory(outcome).toLowerCase();
                    int cats = cat.getNumberOfCategories();
                    System.out.print("bestcat: "+bestcat);

                    System.out.print(", is: " + NLPJusClassificationTrainGenerator.normalizeString(category));
                    if (bestcat.equalsIgnoreCase(NLPJusClassificationTrainGenerator.normalizeString(category))) {
                        correct++;
                        System.out.print("       --> OK("+String.format("%.2f",100.0 / total * correct) + "%"+")");
                    }

                    System.out.print("            |");

                    for(int x = 0; x < cats; x++) {
                        System.out.print(", "+cat.getCategory(x)+"("+outcome[x]+")");
                    }

                    System.out.println();

                }
            } catch (FileNotFoundException | InstantiationException e) {
                /* ignore */
            }
        }

        System.out.println("\nanalyzed " + total + " documents of which " + correct + " were categorized correctly: " + (100.0 / total * correct) + "%");
    }
}
