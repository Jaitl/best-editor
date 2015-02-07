package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.io.File;

public class RecordSaver extends Saver {

    private EntryManager<RecordEntry> recordManager = EntryManager.createRecordManager();

    public void saveRecord(RecordEntry recordEntry, File icon) {
        String pathToImage = saveImage(icon, "record");
        recordEntry.setPathToImage(pathToImage);
        recordEntry.setId(Generator.generateRandomId());

        recordManager.add(recordEntry);
        recordManager.saveToFile();
    }
}
