package ch.shaped.search.elastic;

import ch.shaped.dcrxml.graphdb.utils.FileCrawler;
import ch.shaped.dcrxml.json.DCRDocJsonSerializer;
import ch.shaped.dcrxml.model.DCRDoc;
import ch.shaped.search.SearchImportNode;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by christof on 6/14/15.
 */
public class ElasticsearchImportNode implements SearchImportNode {
    private static Logger logger = Logger.getLogger(ElasticsearchImportNode.class.getName());

    private final String esClusterName;
    private Client esClient;

    public ElasticsearchImportNode(String clusterName) {
        this.esClusterName = clusterName;
                Settings settings = ImmutableSettings.settingsBuilder()
                .put("discovery.zen.ping.multicast.enabled", "false")
                .put("discovery.zen.ping.unicast.hosts", "[\"localhost\"]")
                .put("cluster.name", this.esClusterName)
                .put("node.data", "false")
                .put("node.master", "false")
                .put("node.name", "embedded-client")
                        .build();

//        this.esNode = nodeBuilder().client(true).data(false).node();
//        this.esClient = this.esNode.client();

        this.esClient = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    }

    public void indexDirectory(File directory) {
        FileCrawler fc = new FileCrawler();
        List<File> files = fc.getFileList(directory, new String[]{"xml"});
        DCRDocJsonSerializer serialzer = new DCRDocJsonSerializer();

        BulkProcessor bulkProcessor = BulkProcessor.builder(esClient, new ElasticBulkListener())
                .setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
               // .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .build();


       for (File file : files) {
            try {
                String je = serialzer.serialize(new DCRDoc(file));
                bulkProcessor.add(new IndexRequest("lawdocs", "dcrdoc").source(je));
            } catch(InstantiationException | FileNotFoundException e) {
            }
        }

        try {
            bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
        } catch(InterruptedException e) {
            logger.debug("Awaiting closeing of BulkProcessor interrupted");
        }

        esClient.close();
    }

    public static void main(String[] args) {
        // currently only a debug main
        if(args.length < 1) {
            System.out.println("Usage: java -jar search.jar dcrxmlDirectory");
            System.exit(1);
        }

        SearchImportNode ein = new ElasticsearchImportNode("dcrdocs");
        ein.indexDirectory(new File(args[0]));
    }
}
