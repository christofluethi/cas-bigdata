package ch.shaped.search;

import ch.shaped.dcrxml.json.DCRDocJsonSerializer;
import ch.shaped.dcrxml.model.DCRDoc;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by christof on 6/14/15.
 */
public class DCRDocElasticImporter {
    public static void main(String[] args) throws FileNotFoundException, InstantiationException {
        // currently only a debug main
        if(args.length < 1) {
            System.out.println("Usage: java -jar search.jar Filename");
            System.exit(1);
        }

        DCRDoc d = new DCRDoc(new File(args[0]));

        DCRDocJsonSerializer serialzer = new DCRDocJsonSerializer();
        String je = serialzer.serialize(d);
        System.out.println(je);
    }
}
