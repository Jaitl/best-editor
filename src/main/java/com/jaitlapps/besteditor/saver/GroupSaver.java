package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.manager.EntryManager;

import java.awt.image.BufferedImage;

public class GroupSaver extends EntrySaver {
    private EntryManager<GroupEntry> groupManager = EntryManager.createGroupManager();

    public void save(GroupEntry groupEntry, BufferedImage icon) {
        String pathToImage = saveIcon(icon, "group");
        groupEntry.setPathToImage(pathToImage);
        groupEntry.setId(Generator.generateRandomId());

        groupManager.add(groupEntry);
        groupManager.saveToFile();
    }

    public void update(GroupEntry groupEntry, BufferedImage icon) {

        deleteIcon(groupEntry);

        String newIcon = saveIcon(icon, "group");
        groupEntry.setPathToImage(newIcon);

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
