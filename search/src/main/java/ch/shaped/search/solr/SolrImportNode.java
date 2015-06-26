package ch.shaped.search.solr;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.model.DCRDoc;
import ch.shaped.search.SearchImportNode;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.elasticsearch.client.Client;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by christof on 6/22/15.
 */
public class SolrImportNode implements SearchImportNode {

    private static Logger logger = Logger.getLogger(SolrImportNode.class.getName());

    private final String collectionName;
    private Client esClient;

    public SolrImportNode(String collection) {
        this.collectionName = collection;
    }


    public void indexDirectory(File directory) {
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(directory, new String[]{"xml"});

        CloudSolrClient client = new CloudSolrClient("localhost:9983");
        client.setDefaultCollection("dcrdocs");

        int i = 0;
        for (File file : files) {
            i++;
            try {
                DCRDoc d = new DCRDoc(file);
                SolrInputDocument solrDoc = new SolrInputDocument();
                Map<String, List<String>> mi = d.getMetainfo();
                for (String s : mi.keySet()) {
                    solrDoc.addField(s, mi.get(s));
                }

                solrDoc.addField("keywords", d.getKeywords());
                solrDoc.addField("reference", d.getReferences());

                client.add(solrDoc);
                if(i % 1000 == 0) {
                    client.commit();
                }
            } catch(InstantiationException| SolrException | SolrServerException |IOException e ) {
                logger.debug("Exception (document: "+file.getAbsolutePath()+": "+e);
            }
        }

        try {
            client.commit();
        } catch(SolrException | SolrServerException |IOException e) {
            /* der */
        }
    }

    public static void main(String[] args) {
        // currently only a debug main
        if(args.length < 1) {
            System.out.println("Usage: java -jar search.jar dcrxmlDirectory");
            System.exit(1);
        }

        SearchImportNode ein = new SolrImportNode("dcrdocs");
        ein.indexDirectory(new File(args[0]));
    }
}
