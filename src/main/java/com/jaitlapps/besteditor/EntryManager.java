package com.jaitlapps.besteditor;

import com.google.gson.Gson;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.ListEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public abstract class EntryManager<T extends Entry> {

    private static Map<String, Object> entryMap = new HashMap<>();

    protected static Logger log = Logger.getLogger(EntryManager.class.getName());
    protected ListEntry<T> listEntry;
    protected Gson gson = new Gson();

    protected Path pathToDataDir;
    protected Path pathToEntryData;

    protected Class<?> typeClass;

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
            //Type collectionType = new TypeToken<ListEntry<T>>(){}.getType();
            String jsonData = gson.toJson(listEntry);
            Files.write(pathToEntryData, jsonData.getBytes("UTF-8"), StandardOpenOption.CREATE);
            //mapper.writeValue(pathToEntryData.toFile(), listEntry);
            log.info("save entries to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void loadFromFile();
}
