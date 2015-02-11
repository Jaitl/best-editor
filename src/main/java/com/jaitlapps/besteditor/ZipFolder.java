package com.jaitlapps.besteditor;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFolder {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(ZipFolder.class);

    private static List<String> fileList = new ArrayList<>();

    public static void zipDir(String dir, String zipFile) {
        File directory = new File(dir);
        getFileList(directory);

        try {
            FileOutputStream fos  = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String filePath : fileList) {
                log.info("Compressing: " + filePath);

                // Creates a zip entry.
                String name = filePath.substring(directory.getAbsolutePath().length() + 1,
                        filePath.length());
                ZipEntry zipEntry = new ZipEntry(name);
                zos.putNextEntry(zipEntry);

                // Read file content and write to zip output stream.
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                // Close the zip entry and the file input stream.
                zos.closeEntry();
                fis.close();
            }

            // Close zip output stream and file output stream. This will
            // complete the compression process.
            zos.close();
            fos.close();
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    /**
     * Get files list from the directory recursive to the sub directory.
     */
    private static void getFileList(File directory) {
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if(file.getName().compareTo("Thumbs.db")  != 0)
                        fileList.add(file.getAbsolutePath());
                } else {
                    getFileList(file);
                }
            }
        }
    }
}