package ch.shaped.dcrxml.graphdb.neo4j;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.graphdb.utils.api.GraphDBImporter;
import ch.shaped.dcrxml.model.DCRXml;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import org.neo4j.graphdb.*;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by christof on 5/21/15.
 */
public class Neo4jImporter implements GraphDBImporter {

    public void importDirectory(File xmls) {
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(xmls, new String[]{"xml"});

        Map<String, Long> keywordsNodes = new HashMap<String, Long>();
        Map<String, Long> refNodes = new HashMap<String, Long>();

      /*
      Regular DB Creation
      GraphDatabaseService graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder("/tmp/neo4j-db")
                .setConfig(GraphDatabaseSettings.pagecache_memory, "512M")
                .setConfig(GraphDatabaseSettings.string_block_size, "60")
                .setConfig( GraphDatabaseSettings.array_block_size, "300")
                .newGraphDatabase();

        registerShutdownHook(graphDb);*/

        BatchInserter inserter = null;
        try {
            inserter = BatchInserters.inserter(new File("~/dcrxml-full.db").getAbsolutePath());

            Label documentLabel = DynamicLabel.label("Document");
            inserter.createDeferredSchemaIndex(documentLabel).on("leid").create();

            Label keywordLabel = DynamicLabel.label("Keyword");
            inserter.createDeferredSchemaIndex(keywordLabel).on("name").create();

            Label referenceLabel = DynamicLabel.label("Reference");
            inserter.createDeferredSchemaIndex(referenceLabel).on("name").create();

            RelationshipType hasKeyword = DynamicRelationshipType.withName("has_keyword");
            RelationshipType hasReference = DynamicRelationshipType.withName("has_reference");

            Map<String, Object> documentProperties = new HashMap<>();
            Map<String, Object> keywordProperties = new HashMap<>();
            Map<String, Object> referenceProperties = new HashMap<>();

            long startTime = System.nanoTime();
            int i = 0;
            System.out.println("Start processing a total of #" + files.size() + " entries.");
            for (File file : files) {
                i++;
                try {
                    DCRXml dcrdoc = new DCRXml(file);
                    documentProperties.clear();

                    /* create node */
                    documentProperties.put("leid", dcrdoc.getMetainfo().get("leid").get(0));
                    documentProperties.put("url", dcrdoc.getUrl());
                    documentProperties.put("title", dcrdoc.getTitle());
                    documentProperties.put("source", dcrdoc.getSource());
                    documentProperties.put("subjectarea", dcrdoc.getSubjectArea());

                    Long node = inserter.createNode(documentProperties, documentLabel);

                    List<Keyword> kwords = dcrdoc.getKeywords();
                    int limit = 0;
                    for (Keyword kw : kwords) {
                        keywordProperties.clear();

                        String name = kw.getKeyword("de");
                        if (name != null) {
                            Long kwNode = null;
                            if (!keywordsNodes.containsKey(name)) {
                                keywordProperties.put("name", name);
                                kwNode = inserter.createNode(keywordProperties, keywordLabel);
                                keywordsNodes.put(name, kwNode);
                            } else {
                                kwNode = keywordsNodes.get(name);
                            }

                            inserter.createRelationship(node, kwNode, hasKeyword, null);
                        }
                        limit++;

                        if (limit > 10) {
                            break;
                        }
                    }

                    List<Reference> refs = dcrdoc.getReferences();
                    for (Reference re : refs) {
                        referenceProperties.clear();

                        String name = re.getName();
                        if (name != null) {
                            Long reNode = null;
                            if (!refNodes.containsKey(name)) {
                                referenceProperties.put("name", name);
                                referenceProperties.put("type", re.getType());
                                referenceProperties.put("category", re.getCategory());
                                referenceProperties.put("subcategory", re.getSubcategory());
                                reNode = inserter.createNode(referenceProperties, referenceLabel);
                                refNodes.put(name, reNode);
                            } else {
                                reNode = refNodes.get(name);
                            }

                            inserter.createRelationship(node, reNode, hasReference, null);
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
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("\n\nDuration: " + duration + "ms");
        } finally {
            if (inserter != null) {
                inserter.shutdown();
            }
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.err.println("Usage: java -jar dcrxml-graphdb-neo4j-jar-with-dependencies.jar DCRXMLDirectory");
            System.exit(1);
        }

        File dcrxmlDirectory = new File(args[0]);

        if (!dcrxmlDirectory.exists() || !dcrxmlDirectory.canRead()) {
            System.err.println("Cannot read directory " + args[0]);
        }

        GraphDBImporter importer = new Neo4jImporter();
        importer.importDirectory(dcrxmlDirectory);
    }
}
