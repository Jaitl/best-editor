package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.ImageEditor;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RecordSaver extends EntrySaver {

    private EntryManager<RecordEntry> recordManager = EntryManager.createRecordManager();
    private ImageEditor imageEditor;

    public void save(RecordEntry recordEntry, BufferedImage icon, String content) {
        String recordId = Generator.generateRandomId();
        String pathToImage = saveIcon(icon, "record", recordId);
        recordEntry.setPathToImage(pathToImage);
        recordEntry.setId(recordId);

        String pathToContent = createMarkdownFileWithContent(content, recordId);
        recordEntry.setPathToContent(pathToContent);

        imageEditor.deleteNoUsingImages(content);

        recordManager.add(recordEntry);
        recordManager.saveToFile();
    }

    public void update(RecordEntry recordEntry, BufferedImage icon, String content) {
        deleteIcon(recordEntry);

        String newIcon = saveIcon(icon, "record", recordEntry.getId());
        recordEntry.setPathToImage(newIcon);

        deleteMarkDownFile(recordEntry.getPathToContent());
        String pathToContent = createMarkdownFileWithContent(content, recordEntry.getId());
        recordEntry.setPathToContent(pathToContent);

        imageEditor.deleteNoUsingImages(content);

        recordManager.update(recordEntry);
        recordManager.saveToFile();
    }

    @Override
    public void delete(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        String content = loadContent(recordEntry.getPathToContent());

        ImageEditor.deleteAllImages(content);

        deleteIcon(recordEntry);
        deleteMarkDownFile(recordEntry.getPathToContent());

        recordManager.delete(recordEntry);
        recordManager.saveToFile();
    }

    public static String createMarkdownFileWithContent(String content, String contentId) {
        Path pathToContent = Paths.get(preferences.getWorkFolder(), "content", contentId + ".md");

        try {
            log.info("save content to file: " + Paths.get("content", contentId + ".md"));
            Files.deleteIfExists(pathToContent);
            Files.write(pathToContent, content.getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            log.error("save content error", e);
        }

        return "content/" + contentId + ".md";
    }

    public static void deleteMarkDownFile(String path) {
        Path pathToContent = Paths.get(preferences.getWorkFolder(), path);

        try {
            log.info("delete content: " + path);
            Files.delete(pathToContent);
        } catch (IOException e) {
            log.error("delete content error", e);
        }
    }

    public ImageEditor getImageEditor() {
        return imageEditor;
    }

    public void setImageEditor(ImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }

    public static String loadContent(String path) {
        Path pathToContent = Paths.get(preferences.getWorkFolder(), path);
        byte[] bytesContent = null;
        try {
            bytesContent = Files.readAllBytes(pathToContent);
        } catch (IOException e) {
            log.error("load content error", e);
        }


        String result = null;
        try {
            result = new String(bytesContent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("byte error", e);
        }

        return result;
    }
}
