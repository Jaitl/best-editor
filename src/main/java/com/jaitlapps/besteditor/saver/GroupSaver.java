package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.io.File;

public class GroupSaver extends EntrySaver {
    private EntryManager<GroupEntry> groupManager = EntryManager.createGroupManager();

    @Override
    public void save(Entry entry, File icon) {
        GroupEntry groupEntry = (GroupEntry) entry;

        String pathToImage = saveImage(icon, "group");
        groupEntry.setPathToImage(pathToImage);
        groupEntry.setId(Generator.generateRandomId());

        groupManager.add(groupEntry);
        groupManager.saveToFile();
    }

    @Override
    public void update(Entry entry, File icon) {
        GroupEntry groupEntry = (GroupEntry) entry;

        if(isChangeIcon(groupEntry, icon)) {
            deleteIcon(groupEntry);

            String newIcon = saveImage(icon, "group");
            groupEntry.setPathToImage(newIcon);
        }

        groupManager.update(groupEntry);
        groupManager.saveToFile();
    }

    @Override
    public void delete(Entry entry) {
        GroupEntry groupEntry = (GroupEntry) entry;

        deleteIcon(groupEntry);

        groupManager.delete(groupEntry);
        groupManager.saveToFile();
    }
}
