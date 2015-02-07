package com.jaitlapps.besteditor.saver;


import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.EntryManager;
import com.jaitlapps.besteditor.Generator;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public abstract class Saver {
    public final static int IMAGE_HEIGHT = 350;

    protected static Logger log = Logger.getLogger(EntryManager.class.getName());
    protected static CommonPreferences preferences = CommonPreferences.getInstance();

    protected String saveImage(File image, String folder) {

        BufferedImage originalImage = loadImage(image);
        BufferedImage resizeImage = resizeImage(originalImage);
        String imageName = Generator.generateRandomId();

        Path pathToImage = Paths.get(preferences.getWorkFolder(), "icon", folder, imageName + ".png");

        try {
            ImageIO.write(resizeImage, "png", pathToImage.toFile());
            log.info("save icon to file: " + Paths.get("icon", folder, imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "icon\\" + folder + "\\" + imageName + ".png";
    }

    private BufferedImage resizeImage(BufferedImage buffImage) {
        return Scalr.resize(buffImage, Saver.IMAGE_HEIGHT);
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
