package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.Group;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class GroupSaver {

    private static CommonPreferences preferences = new CommonPreferences();

    public final static int IMAGE_HEIGHT = 350;

    public void saveGroup(String title, File image) throws IOException {

        Group group = new Group(title);

        String workFolder = preferences.getWorkFolder();

        if(workFolder == null) {
            throw new IOException("Не выбрана рабочая папка");
        }

        String pathToImage = saveImage(image);
        group.setPathToImage(pathToImage);
        group.setId(generateRandomId());


        GroupManager groupManager = GroupManager.getInstance();

        groupManager.addGroup(group);

        groupManager.saveGroupsToFile();
    }

    private String saveImage(File image) {

        BufferedImage originalImage = loadImage(image);
        BufferedImage resizeImage = resizeImage(originalImage);
        String imageName = generateRandomId();

        Path pathToIconDir = Paths.get(preferences.getWorkFolder() + "\\icon");

        if(Files.notExists(pathToIconDir)) {
            try {
                Files.createDirectory(pathToIconDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path pathToGroupIconDir = pathToIconDir.resolve("group");

        if(Files.notExists(pathToGroupIconDir)) {
            try {
                Files.createDirectory(pathToGroupIconDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path pathToImage = pathToGroupIconDir.resolve(imageName + ".png");

        try {
            ImageIO.write(resizeImage, "png", pathToImage.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "group\\icon\\" + imageName + ".png";
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

    private String generateRandomId() {

        byte[] dateByte = null;

        try {
            dateByte = new Date().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] randomByte = messageDigest.digest(dateByte);

        return byteArrayToHex(randomByte);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }
}
