package com.jaitlapps.besteditor;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFolder {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(ZipFolder.class);

    private  List<String> fileList = new ArrayList<>();

    public void zipDir(String zipFile) {
        CommonPreferences preferences = CommonPreferences.getInstance();

        Path contentDirectory = Paths.get(preferences.getWorkFolder());

        getFileList(contentDirectory.resolve("content").toFile());
        getFileList(contentDirectory.resolve("data").toFile());
        getFileList(contentDirectory.resolve("icon").toFile());

        try {
            FileOutputStream fos  = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (String filePath : fileList) {
                log.info("Compressing: " + filePath);

                // Creates a zip entry.
                String name = filePath.substring(contentDirectory.toFile().getAbsolutePath().length() + 1,
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
    private void getFileList(File directory) {
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