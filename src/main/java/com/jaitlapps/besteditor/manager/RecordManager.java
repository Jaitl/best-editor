package com.jaitlapps.besteditor.manager;

import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.domain.list.RecordListEntry;

import java.io.IOException;
import java.nio.file.Files;

public class RecordManager extends EntryManager<RecordEntry> {
    protected RecordManager() {
        super("record");
    }

    @Override
    public void loadFromFile() {
        if(Files.exists(pathToEntryData)) {
            try {
                byte[] jsonDataBytes = Files.readAllBytes(pathToEntryData);
                String jsonData = new String(jsonDataBytes);
                listEntry = gson.fromJson(jsonData, RecordListEntry.class);
                log.info("save records from file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            listEntry = new RecordListEntry();
            log.info("create new records list");
        }
    }
}
