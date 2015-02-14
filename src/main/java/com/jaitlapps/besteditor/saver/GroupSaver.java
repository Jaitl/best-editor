package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

public class GroupSaver extends EntrySaver {
    private EntryManager<GroupEntry> groupManager = EntryManager.createGroupManager();

    public void save(GroupEntry groupEntry, BufferedImage icon) {
        String recordId = Generator.generateRandomId();
        String pathToImage = saveIcon(icon, "group", recordId);
        groupEntry.setPathToImage(pathToImage);
        groupEntry.setId(recordId);

        groupManager.add(groupEntry);
        groupManager.saveToFile();
    }

    public void update(GroupEntry groupEntry, BufferedImage icon) {
        deleteIcon(groupEntry);

        String newIcon = saveIcon(icon, "group", groupEntry.getId());
        groupEntry.setPathToImage(newIcon);

        groupManager.update(groupEntry);
        groupManager.saveToFile();
    }

    @Override
    public void delete(Entry entry) throws Exception {
        GroupEntry groupEntry = (GroupEntry) entry;

        if(!isGroupUsingInRecord(groupEntry.getId()))
            throw new Exception("В этой группе есть статьи. Что бы удалить группу, " +
                    "надо сначала удалить все статьи, входящие в эту группу");

        deleteIcon(groupEntry);

        groupManager.delete(groupEntry);
        groupManager.saveToFile();
    }

    private boolean isGroupUsingInRecord(String groupId) {
        boolean result = false;
        EntryManager<RecordEntry> entryManager = EntryManager.createRecordManager();

        try {
            entryManager.getList().stream().filter(r -> r.getGroupId().compareTo(groupId) == 0).findAny().get();
        } catch (NoSuchElementException e) {
            result = true;
        }

        return result;

    }
}
