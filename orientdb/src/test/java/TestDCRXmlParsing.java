import ch.shaped.dcrxml.model.DCRXml;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by christof on 5/15/15.
 */
public class TestDCRXmlParsing {

    @Test
    public void testParsing() throws FileNotFoundException, InstantiationException {
        ClassLoader classLoader = getClass().getClassLoader();
        File dcr = new File(classLoader.getResource("134890.xml").getFile());
        System.out.println("Using DCRXml file: "+dcr.getAbsolutePath());

        DCRXml dcrdoc = new DCRXml(dcr);
        Map<String, List<String>> mi = dcrdoc.getMetainfo();
        for (String s : mi.keySet()) {
            System.out.print(String.format("%20s:", s));
            for (String s1 : mi.get(s)) {
                System.out.print(" " + s1);
            }
            System.out.println();
        }
        
        assertTrue(mi.containsKey("dataSource"));
        assertTrue(mi.get("refine_date").get(0).equals("28.10.2014 18:06:47"));

        List<Keyword> keywords = dcrdoc.getKeywords();
        for (Keyword d : keywords) {
            System.out.println("Keyword with score '" + d.getScore() + "': " + d.getKeyword("de"));
        }

        assertTrue(keywords.get(0).getKeyword("de").equals("bundesstrafgericht"));

        List<Reference> references = dcrdoc.getReferences();
        for (Reference r : references) {
            System.out.println("Reference '" + r.getName() + "': " + r.getLinksref());
        }

        assertTrue(references.get(0).getName().equals("129-II-462"));
    }
}
