package ch.shaped.nlp.classification.train;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRDoc;
import ch.shaped.nlp.trainer.OpenNLPTrainer;
import ch.shaped.nlp.util.OpenNLPFactory;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by christof on 5/31/15.
 */
public class NLPClassificationTrainGenerator extends OpenNLPTrainer {
    private static Logger logger = Logger.getLogger(NLPClassificationTrainGenerator.class.getName());
    private Tokenizer tokenizer = null;

    public NLPClassificationTrainGenerator(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void trainToModel(File xmls, File trainDataOut, File modelOut) {
        this.writeModelTrainFile(xmls, trainDataOut);
        this.writeModelFile(trainDataOut, modelOut);
    }

    public void writeModelTrainFile(File xmls, File outputFile)  {
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
                    DCRDoc dcrdoc = new DCRDoc(file);

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
        if (args == null || args.length < 1) {
            System.err.println("Usage: java -jar nlp.jar DCRXMLDirectory");
            System.exit(1);
        }

        File dcrxmlDirectory = new File(args[0]);

        if (!dcrxmlDirectory.exists() || !dcrxmlDirectory.canRead()) {
            System.err.println("Cannot read directory " + args[0]);
        }

        Tokenizer tokenizer = OpenNLPFactory.createTokenizer(new File("nlp/src/main/resources/de-token.bin"));

        NLPClassificationTrainGenerator generator = new NLPClassificationTrainGenerator(tokenizer);
        generator.trainToModel(dcrxmlDirectory, new File("nlp/src/main/resources/sentiments-gen/de-drsk.train"), new File("nlp/src/main/resources/sentiments-gen/de-drsk.bin"));
    }
}
