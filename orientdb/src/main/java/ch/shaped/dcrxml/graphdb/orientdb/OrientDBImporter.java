package ch.shaped.dcrxml.graphdb.orientdb;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.graphdb.utils.api.GraphDBImporter;
import ch.shaped.dcrxml.model.DCRDoc;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shaped on 14.05.15.
 */
public class OrientDBImporter implements GraphDBImporter {

    public void importDirectory(File xmls) {
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(xmls, new String[]{"xml"});

        Map<String, Vertex> keywordsVertex = new HashMap<String, Vertex>();
        Map<String, Vertex> refVertex = new HashMap<String, Vertex>();

        //OrientGraphFactory factory = new OrientGraphFactory("remote:localhost/docstore").setupPool(1,10);

        // EVERY TIME YOU NEED A GRAPH INSTANCE
        //OrientGraphNoTx graph = factory.getNoTx();
        TransactionalGraph graph = new OrientGraph("remote:localhost/docstore", "admin", "admin");
        try {
            long startTime = System.nanoTime();
            int i = 0;
            System.out.println("Start processing a total of #" + files.size() + " entries.");
            for (File file : files) {
                i++;
                try {
                    DCRDoc dcrdoc = new DCRDoc(file);
                    Vertex doc = graph.addVertex("class:Document");
                    doc.setProperty("leid", dcrdoc.getMetainfo().get("leid").get(0));
                    doc.setProperty("url", dcrdoc.getUrl());
                    doc.setProperty("title", dcrdoc.getTitle());
                    doc.setProperty("source", dcrdoc.getSource());
                    doc.setProperty("subjectarea", dcrdoc.getSubjectArea());

                    List<Keyword> kwords = dcrdoc.getKeywords();
                    int limit = 0;
                    for (Keyword kw : kwords) {
                        String name = kw.getKeyword("de");
                        if (name != null) {
                            Vertex kwVertex = null;
                            if (!keywordsVertex.containsKey(name)) {
                                kwVertex = graph.addVertex("class:Keyword");
                                kwVertex.setProperty("name", name);
                                keywordsVertex.put(name, kwVertex);
                            } else {
                                kwVertex = keywordsVertex.get(name);
                            }

                            graph.addEdge(null, doc, kwVertex, "has_keyword");
                        }
                        limit++;

                        if(limit > 10) {
                            break;
                        }
                    }

                    List<Reference> refs = dcrdoc.getReferences();
                    for (Reference re : refs) {
                        String name = re.getName();
                        if (name != null) {
                            Vertex ref = null;
                            if (!refVertex.containsKey(name)) {
                                ref = graph.addVertex("class:Reference");
                                ref.setProperty("name", name);
                                ref.setProperty("type", re.getType());
                                ref.setProperty("category", re.getCategory());
                                ref.setProperty("subcategory", re.getSubcategory());
                                refVertex.put(name, ref);
                            } else {
                                ref = refVertex.get(name);
                            }

                           graph.addEdge(null, doc, ref, "has_reference");
                        }
                    }

                } catch (FileNotFoundException | InstantiationException e) {
                /* ignore */
                }
                if (i % 100 == 0) {
                    System.out.print(".");
                    graph.commit();
                }
                if (i % 10000 == 0) {
                    System.out.println(" | " + i);
                    //  graph.commit();
                }
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("\n\nDuration: " + duration + "ms");
        } finally {
            graph.shutdown();
        }
    }

    public static void main(String[] args) {
        if(args == null || args.length < 1) {
            System.err.println("Usage: java -jar dcrxml-graphdb-orientdb-jar-with-dependencies.jar DCRXMLDirectory");
            System.exit(1);
        }

        File dcrxmlDirectory = new File(args[0]);
        String odbString = args[1];

        if(!dcrxmlDirectory.exists() || !dcrxmlDirectory.canRead()) {
            System.err.println("Cannot read directory "+args[0]);
        }

        GraphDBImporter importer = new OrientDBImporter();
        importer.importDirectory(dcrxmlDirectory);
    }
}
