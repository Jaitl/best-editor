package com.jaitlapps.besteditor.manager;

import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.list.GroupListEntry;

import java.io.IOException;
import java.nio.file.Files;

public class GroupManager extends EntryManager<GroupEntry> {

    protected GroupManager() {
        super("group");
    }

    @Override
    public void loadFromFile() {
        if(Files.exists(pathToEntryData)) {
            try {
                byte[] jsonDataBytes = Files.readAllBytes(pathToEntryData);
                String jsonData = new String(jsonDataBytes, "UTF-8");
                if(jsonData.length() > 0) {
                    listEntry = gson.fromJson(jsonData, GroupListEntry.class);
                    log.info("load groups from file");
                } else {
                    listEntry = new GroupListEntry();
                    log.info("create new groups list");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            listEntry = new GroupListEntry();
            log.info("create new groups list");
        }
    }
}
