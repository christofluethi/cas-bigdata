package ch.shaped.nlp.classification;

import ch.shaped.bfh.cas.bgd.textanalysis.nlp.OpenNLPFactory;
import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRXml;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by christof on 5/31/15.
 */
public class NLPClassificationTrainGenerator {
    private static Logger logger = Logger.getLogger(NLPClassificationTrainGenerator.class.getName());

    public void importDirectory(File xmls, File outputFile, Tokenizer tokenizer)  {
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(xmls, new String[]{"xml"});
        PrintWriter pw = null;

        try {
            pw = new PrintWriter(outputFile);

            int i = 0;
            System.out.println("Start processing a total of #" + files.size() + " entries.");


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
                        sb.append(category.toUpperCase().replace(" ", "_"));
                        String document = dcrdoc.getContent().getPlainText();
                        String[] tokens = tokenizer.tokenize(document);
                        for (String token : tokens) {
                            if (!token.equals("****") && !token.equals("***") && !token.equals("**") && !token.equals("*") ) {
                                sb.append(" " + token);
                            }
                        }
                    }
                    String out = sb.toString().trim();
                    if(!out.isEmpty()) {
                        pw.write(out + "\n");
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
        } catch(FileNotFoundException e) {
            logger.error("File "+outputFile+" not found. stop");
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public static void main(String[] args) throws InstantiationException {
        if (args == null || args.length < 2) {
            System.err.println("Usage: java -jar nlp.jar DCRXMLDirectory outputFile");
            System.exit(1);
        }

        File dcrxmlDirectory = new File(args[0]);
        File output = new File(args[1]);

        if (!dcrxmlDirectory.exists() || !dcrxmlDirectory.canRead()) {
            System.err.println("Cannot read directory " + args[0]);
        }

        Tokenizer tokenizer = OpenNLPFactory.createTokenizer(new File("playground/src/main/resources/de-token.bin"));

        NLPClassificationTrainGenerator generator = new NLPClassificationTrainGenerator();
        generator.importDirectory(dcrxmlDirectory, output, tokenizer);
    }
}
