package com.jaitlapps.besteditor.manager;

import com.google.gson.Gson;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.domain.list.ListEntry;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EntryManager<T extends Entry> {

    private static Map<String, Object> entryMap = new HashMap<>();

    protected static org.slf4j.Logger log = LoggerFactory.getLogger(EntryManager.class);

    protected ListEntry<T> listEntry;
    protected Gson gson = new Gson();

    protected Path pathToDataDir;
    protected Path pathToEntryData;

    protected EntryManager(String fileName) {
        CommonPreferences preferences = CommonPreferences.getInstance();

        pathToDataDir = Paths.get(preferences.getWorkFolder(), "data");
        pathToEntryData = pathToDataDir.resolve(fileName + ".json");

        loadFromFile();
    }

    public static EntryManager<GroupEntry> createGroupManager() {
        if(!entryMap.containsKey("group"))
            entryMap.put("group", new GroupManager());

        return (GroupManager) entryMap.get("group");
    }

    public static EntryManager<RecordEntry> createRecordManager() {
        if(!entryMap.containsKey("record"))
            entryMap.put("record", new RecordManager());

        return (RecordManager) entryMap.get("record");
    }

    public void add(T entry) {
        listEntry.add(entry);
        log.info("add entry: " + entry.getId());
    }

    public void update(T entry) {
        listEntry.update(entry);
        log.info("update entry: " + entry.getId());
    }

    public void delete(T entry) {
        log.info("delete entry: " + entry.getId());

        listEntry.delete(entry);
    }

    public List<T> getList() {
        return listEntry.getList();
    }

    public void saveToFile() {
        try {
            String jsonData = gson.toJson(listEntry);
            Files.deleteIfExists(pathToEntryData);
            Files.write(pathToEntryData, jsonData.getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
            log.info("save entries to file");
        } catch (IOException e) {
            log.error("save error", e);
        }
    }

    public abstract void loadFromFile();
}
