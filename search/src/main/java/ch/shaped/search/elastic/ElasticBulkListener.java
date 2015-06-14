package ch.shaped.search.elastic;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

/**
 * Created by christof on 6/14/15.
 */
public class ElasticBulkListener implements BulkProcessor.Listener {
    private static Logger logger = Logger.getLogger(ElasticBulkListener.class.getName());

    @Override
    public void beforeBulk(long l, BulkRequest bulkRequest) {
        int actions = bulkRequest.numberOfActions();
        if (actions % 100 == 0) {
            logger.debug("Actions performed: " + actions);
        }
    }

    @Override
    public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
        if (bulkResponse.hasFailures()) {
            logger.debug("Bulk insertation flagged an error.");
            for (BulkItemResponse resp : bulkResponse) {
                if (resp.isFailed()) {
                    logger.debug("Request " + resp.getId() + " failed with: " + resp.getFailureMessage());
                }
            }
        }
    }

    @Override
    public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
        logger.debug("Bulk insertation failed with : "+throwable.getMessage());
    }
}
