package ch.shaped.nlp.classification.train;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRXml;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import ch.shaped.nlp.trainer.OpenNLPTrainer;
import ch.shaped.nlp.util.OpenNLPFactory;
import ch.shaped.nlp.util.StopWordsFilter;
import opennlp.tools.tokenize.Tokenizer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by christof on 5/31/15.
 */
public class NLPJusClassificationTrainGenerator extends OpenNLPTrainer {
    private static Logger logger = Logger.getLogger(NLPJusClassificationTrainGenerator.class.getName());
    private Tokenizer tokenizer = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    private StopWordsFilter ignorewords = null;
    private Map<String, AtomicInteger> descCount = new HashMap<>();

    public NLPJusClassificationTrainGenerator(Tokenizer tokenizer) throws IOException {
        this.tokenizer = tokenizer;
        this.ignorewords = new StopWordsFilter("nlp/src/main/resources/thes-words-ignore.txt");
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
                        sb.append(normalizeString(category).toUpperCase());
                        for (Keyword kw : dcrdoc.getKeywords()) {
                            String word = normalizeString(kw.getKeyword("de"));
                            if(!ignorewords.isStopWord(word)) {
                                sb.append(" " + word);
                                if(!descCount.containsKey(word)) {
                                    descCount.put(word, new AtomicInteger(0));
                                }
                                descCount.get(word).incrementAndGet();
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
                                sb.append(" " + normalizeString(r));
                            }
                        }
                    }
                    String out = sb.toString().trim();
                    if(!out.isEmpty() && ! out.equalsIgnoreCase(normalizeString(category))) {
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

            for (String s : descCount.keySet()) {
                int c = descCount.get(s).intValue();
                if(c >  500) {
                    System.out.println(s + " " + descCount.get(s).intValue());
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

    public static void main(String[] args) throws InstantiationException, IOException {
        if (args == null || args.length < 1) {
            System.err.println("Usage: java -jar nlp.jar DCRXMLDirectory");
            System.exit(1);
        }

        File dcrxmlDirectory = new File(args[0]);

        if (!dcrxmlDirectory.exists() || !dcrxmlDirectory.canRead()) {
            System.err.println("Cannot read directory " + args[0]);
        }

        Tokenizer tokenizer = OpenNLPFactory.createTokenizer(new File("nlp/src/main/resources/de-token.bin"));

        NLPJusClassificationTrainGenerator generator = new NLPJusClassificationTrainGenerator(tokenizer);
        generator.trainToModel(dcrxmlDirectory, new File("nlp/src/main/resources/sentiments-gen/de-jusl-metadata.train"), new File("nlp/src/main/resources/sentiments-gen/de-jusl-metadata.bin"));
    }

    public static String normalizeString(String s) {
        return s.replaceAll("\\s*\\([^\\)]*\\)\\s*", "").replaceAll("[\\.,_: \\/\\\\-]", "");
    }
}
