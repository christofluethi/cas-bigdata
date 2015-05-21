package ch.shaped.dcrxml.graphdb.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christof on 5/15/15.
 */
public class FileCrawler {

    public List<File> getFileList(File path, final String[] filter) {
        List<File> files = new ArrayList<File>();

        FilenameFilter fnfilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                boolean isOk = false;
                for (String s1 : filter) {
                    if(s.endsWith("."+s1)) {
                        isOk = true;
                        break;
                    }
                }

                return isOk;
            }
        };

        this.walkDir(path, files, fnfilter);

        return files;
    }


    /**
     * Scan directory
     *
     * @param f Start Folder for the recursive scan
     */
    private void walkDir(File f, List<File> files, FilenameFilter filter) {
        File[] children = f.listFiles();
        if(children != null) {
            for (File child : children) {
                if(child.isFile()) {
                    if(child.canRead() && child.length() > 0) {
                        files.add(child);
                    }
                } else {
                    this.walkDir(child, files, filter);
                }
            }
        }
    }
}
