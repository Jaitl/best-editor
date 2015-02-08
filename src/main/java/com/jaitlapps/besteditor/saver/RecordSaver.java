package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class RecordSaver extends EntrySaver {

    private EntryManager<RecordEntry> recordManager = EntryManager.createRecordManager();

    public void save(RecordEntry recordEntry, BufferedImage icon, String content) {
        String pathToImage = saveImage(icon, "record");
        recordEntry.setPathToImage(pathToImage);
        recordEntry.setId(Generator.generateRandomId());

        String pathToContent = saveContent(content);
        recordEntry.setPathToContent(pathToContent);

        recordManager.add(recordEntry);
        recordManager.saveToFile();
    }

    public void update(RecordEntry recordEntry, BufferedImage icon, String content) {
        deleteIcon(recordEntry);

        String newIcon = saveImage(icon, "record");
        recordEntry.setPathToImage(newIcon);

        deleteContent(recordEntry);
        String pathToContent = saveContent(content);
        recordEntry.setPathToContent(pathToContent);

        recordManager.update(recordEntry);
        recordManager.saveToFile();
    }

    @Override
    public void delete(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        deleteIcon(recordEntry);
        deleteContent(recordEntry);

        recordManager.delete(recordEntry);
        recordManager.saveToFile();
    }

    private String saveContent(String content) {
        String fileName = Generator.generateRandomId() + ".md";

        Path pathToContent = Paths.get(preferences.getWorkFolder(), "content", fileName);

        try {
            log.info("save content to file: " + Paths.get("content", fileName));
            Files.deleteIfExists(pathToContent);
            Files.write(pathToContent, content.getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "content\\" + fileName;
    }

    private void deleteContent(RecordEntry recordEntry) {
        Path pathToContent = Paths.get(preferences.getWorkFolder(), recordEntry.getPathToContent());

        try {
            log.info("delete content: " + recordEntry.getPathToContent());
            Files.delete(pathToContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
