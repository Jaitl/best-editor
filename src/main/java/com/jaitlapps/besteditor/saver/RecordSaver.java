package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.io.File;

public class RecordSaver extends EntrySaver {

    private EntryManager<RecordEntry> recordManager = EntryManager.createRecordManager();

    @Override
    public void save(Entry entry, File icon) {
        RecordEntry recordEntry = (RecordEntry) entry;

        String pathToImage = saveImage(icon, "record");
        recordEntry.setPathToImage(pathToImage);
        recordEntry.setId(Generator.generateRandomId());

        recordManager.add(recordEntry);
        recordManager.saveToFile();
    }

    @Override
    public void update(Entry entry, File icon) {
        RecordEntry recordEntry = (RecordEntry) entry;

        if(isChangeIcon(recordEntry, icon)) {
            deleteIcon(recordEntry);

            String newIcon = saveImage(icon, "record");
            recordEntry.setPathToImage(newIcon);
        }

        recordManager.update(recordEntry);
        recordManager.saveToFile();
    }

    @Override
    public void delete(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        deleteIcon(recordEntry);

        recordManager.delete(recordEntry);
        recordManager.saveToFile();
    }
}
