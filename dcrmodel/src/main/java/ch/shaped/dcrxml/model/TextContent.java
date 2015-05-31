package ch.shaped.dcrxml.model;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;

/**
 * Created by christof on 5/31/15.
 */
public class TextContent {

    private static Logger logger = Logger.getLogger(TextContent.class.getName());

    private File inputFile;
    private int startOffset = -1;
    private int endOffset = -1;
    private String originalContent;

    public TextContent(File input, int startOffset, int endOffset) throws InstantiationException {
        this.inputFile = input;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        this.assertContent();
    }

    public String getOriginalContent() {
        return this.originalContent;
    }

    private void assertContent() throws InstantiationException {
        try {
            String s = Files.toString(this.inputFile, Charsets.UTF_8);
            this.originalContent = s.substring(startOffset, endOffset);
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("Invalid offsets: " + startOffset + ", " + endOffset + " File: " + this.inputFile.getAbsoluteFile());
            throw new InstantiationException(e.getMessage());
        } catch (IOException e) {
            throw new InstantiationException(e.getMessage());
        }
    }

    public String getPlainText() {
        return this.removeLEAnnotations(Jsoup.parse(this.originalContent).text());
    }

    public String removeLEAnnotations(String in) {
        return in.replaceAll("\\<ls_s.*?>", "").replaceAll("\\<ls_e.*?>", "").replaceAll("\\<lsannot.*?>", "").replaceAll("\\<\\/lsannot.*?>", "").replaceAll("^[^>]*>", "");
    }
}
