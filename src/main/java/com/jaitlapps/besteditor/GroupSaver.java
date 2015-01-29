package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.GroupEntry;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.acl.Group;
import java.util.Date;
import java.util.logging.Logger;

public class GroupSaver {

    private static Logger log = Logger.getLogger(GroupManager.class.getName());
    private static CommonPreferences preferences = CommonPreferences.getInstance();
    private GroupManager groupManager = GroupManager.getInstance();

    public final static int IMAGE_HEIGHT = 350;

    public void saveGroup(GroupEntry groupEntry, File icon) throws IOException {
        String workFolder = preferences.getWorkFolder();

        String pathToImage = saveImage(icon);
        groupEntry.setPathToImage(pathToImage);
        groupEntry.setId(Generator.generateRandomId());

        groupManager.addGroup(groupEntry);

        groupManager.saveGroupsToFile();
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

            String newIcon = saveImage(icon);
            groupEntry.setPathToImage(newIcon);

        }

        groupManager.updateGroup(groupEntry);
        groupManager.saveGroupsToFile();
    }

    public void deleteGroup(GroupEntry groupEntry) {

        Path pathToIcon = Paths.get(preferences.getWorkFolder(), groupEntry.getPathToImage());
        try {
            log.info("delete icon: " + groupEntry.getPathToImage());
            Files.delete(pathToIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        groupManager.deleteGroup(groupEntry);
        groupManager.saveGroupsToFile();
    }

    private String saveImage(File image) {

        BufferedImage originalImage = loadImage(image);
        BufferedImage resizeImage = resizeImage(originalImage);
        String imageName = Generator.generateRandomId();

        Path pathToImage = Paths.get(preferences.getWorkFolder(), "icon", "group", imageName + ".png");

        try {
            ImageIO.write(resizeImage, "png", pathToImage.toFile());
            log.info("save group icon to file: " + Paths.get("icon", "group", imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "icon\\group\\" + imageName + ".png";
    }

    private BufferedImage resizeImage(BufferedImage buffImage) {
        return Scalr.resize(buffImage, IMAGE_HEIGHT);
    }

    private BufferedImage loadImage(File image) {
        BufferedImage buffImage = null;

        try {
            buffImage = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffImage;
    }
}
