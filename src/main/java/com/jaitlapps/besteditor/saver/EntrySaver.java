package com.jaitlapps.besteditor.saver;


import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.ImageEditor;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.manager.EntryManager;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class EntrySaver {
    protected static org.slf4j.Logger log = LoggerFactory.getLogger(EntryManager.class);

    protected static CommonPreferences preferences = CommonPreferences.getInstance();

    protected String saveIcon(BufferedImage originalImage, String folder, String imageId) {
        BufferedImage resizeImage = ImageEditor.resizeImage(originalImage, Integer.parseInt(preferences.getIconSize()));

        Path pathToImage = Paths.get(preferences.getWorkFolder(), "icon", folder, imageId + ".png");

        try {
            ImageIO.write(resizeImage, "png", pathToImage.toFile());
            log.info("save icon to file: " + Paths.get("icon", folder, imageId + ".png"));
        } catch (IOException e) {
            log.error("icon save error", e);
        }

        return "icon/" + folder + "/" + imageId + ".png";
    }

    protected void deleteIcon(Entry entry) {
        Path pathToIcon = Paths.get(preferences.getWorkFolder(), entry.getPathToImage());
        try {
            log.info("delete icon: " + entry.getPathToImage());
            Files.delete(pathToIcon);
        } catch (IOException e) {
            log.error("icon delete error", e);
        }
    }

    public abstract void delete(Entry entry) throws Exception;
}
