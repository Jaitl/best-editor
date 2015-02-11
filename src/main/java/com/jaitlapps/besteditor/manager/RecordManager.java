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
                String jsonData = new String(jsonDataBytes, "UTF-8");
                if(jsonData.length() > 0) {
                    listEntry = gson.fromJson(jsonData, RecordListEntry.class);
                    log.info("load records from file");
                } else {
                    listEntry = new RecordListEntry();
                    log.info("create new records list");
                }
            } catch (IOException e) {
                log.error("load error", e);
            }
        } else {
            listEntry = new RecordListEntry();
            log.info("create new records list");
        }
    }
}
