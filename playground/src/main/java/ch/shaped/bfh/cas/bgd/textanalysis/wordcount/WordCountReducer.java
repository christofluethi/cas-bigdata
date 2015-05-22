package ch.shaped.bfh.cas.bgd.textanalysis.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by christof on 5/19/15.
 */
public class WordCountReducer extends Reducer {

    protected void reduce(LongWritable key, Iterable<IntWritable> values, Reducer.Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }

        context.write(key, new IntWritable(sum));
    }
}
