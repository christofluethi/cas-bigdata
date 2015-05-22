package ch.shaped.bfh.cas.bgd.textanalysis.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by christof on 5/19/15.
 */
public class WordCountMapper extends Mapper {

    private IntWritable one = new IntWritable(1);

    protected void map(LongWritable in, Text value, Mapper.Context context) throws IOException, InterruptedException {
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while(tokenizer.hasMoreElements()) {
            context.write(new Text(tokenizer.nextToken()), one);
        }
    }
}
