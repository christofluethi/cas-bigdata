package ch.shaped.bfh.cas.bgd.textanalysis.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * Created by christof on 5/19/15.
 */
public class WordCounter {


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if(args.length != 2) {
            System.out.println("Argument expected: inputfile outputfile");
        }

        String fileIn = args[0];
        String fileOut = args[1];

        Configuration conf = new Configuration();
        Job job = new Job(conf, "wordcount");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(fileIn));
        FileOutputFormat.setOutputPath(job, new Path(fileOut));

        job.waitForCompletion(true);
    }
}
