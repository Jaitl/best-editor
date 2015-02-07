package com.jaitlapps.besteditor.saver;

import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.Generator;
import com.jaitlapps.besteditor.domain.GroupEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GroupSaver extends Saver {
    private EntryManager<GroupEntry> entryManager = EntryManager.createGroupManager();;

    public void saveGroup(GroupEntry groupEntry, File icon) {
        String pathToImage = saveImage(icon, "group");
        groupEntry.setPathToImage(pathToImage);
        groupEntry.setId(Generator.generateRandomId());

        entryManager.add(groupEntry);
        entryManager.saveToFile();
    }

    public void updateGroup(GroupEntry groupEntry, File icon) {
        // Првоерка не изменилась ли картинка по путям.
        Path pathOldImage = Paths.get(groupEntry.getPathToImage());
        pathOldImage = pathOldImage.subpath(pathOldImage.getNameCount() - 2, pathOldImage.getNameCount());

        Path pathNewImage = Paths.get(icon.getPath());
        pathNewImage = pathNewImage.subpath(pathNewImage.getNameCount() - 2, pathNewImage.getNameCount());

        if(pathOldImage.compareTo(pathNewImage) != 0) {
            Path pathToIcon = Paths.get(preferences.getWorkFolder(), groupEntry.getPathToImage());
            try {
                Files.delete(pathToIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String newIcon = saveImage(icon, "group");
            groupEntry.setPathToImage(newIcon);

        }

        entryManager.update(groupEntry);
        entryManager.saveToFile();
    }

    public void deleteGroup(GroupEntry groupEntry) {
        Path pathToIcon = Paths.get(preferences.getWorkFolder(), groupEntry.getPathToImage());
        try {
            log.info("delete icon: " + groupEntry.getPathToImage());
            Files.delete(pathToIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        entryManager.delete(groupEntry);
        entryManager.saveToFile();
    }
}
