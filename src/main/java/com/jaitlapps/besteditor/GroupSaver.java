package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.Group;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
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
        group.setId(generateIdForGroup());

        ObjectMapper mapper = new ObjectMapper();

        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);


        Path pathToDataDir = Paths.get(preferences.getWorkFolder() + "\\data");

        if(Files.notExists(pathToDataDir)) {
            try {
                Files.createDirectory(pathToDataDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path pathToGroupData = pathToDataDir.resolve("group.json");

        GroupMapper groupMapper = null;
        if(Files.exists(pathToGroupData)) {
            groupMapper = mapper.readValue(pathToGroupData.toFile(), GroupMapper.class);
        } else {
            groupMapper = new GroupMapper();
        }

        groupMapper.addGroup(group);

        mapper.writeValue(pathToGroupData.toFile(), groupMapper);
    }

    private String saveImage(File image) {

        BufferedImage originalImage = loadImage(image);
        BufferedImage resizeImage = resizeImage(originalImage);
        String imageName = executeImageMD5(resizeImage);

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

    private String executeImageMD5(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] imageInByte = null;

        try {
            ImageIO.write( image, "png", baos );
            baos.flush();

            imageInByte = baos.toByteArray();

            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] randomByte = messageDigest.digest(imageInByte);

        return byteArrayToHex(randomByte);
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

    private String generateIdForGroup() {

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
