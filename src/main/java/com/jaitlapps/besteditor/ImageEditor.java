package com.jaitlapps.besteditor;

import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ImageEditor {
    private int counter;

    private List<String> imagesList = new ArrayList<>();

    public final static int IMAGE_SIZE = 1024;

    protected static CommonPreferences preferences = CommonPreferences.getInstance();
    protected static Logger log = Logger.getLogger(ImageEditor.class.getName());

    public String selectImage() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выбор картинки");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        File selectedImage = fileChooser.showOpenDialog(null);
        if(selectedImage != null) {
            log.info("selected image: " + selectedImage.getPath());
            BufferedImage image = loadImage(selectedImage);

            if(!validateImageSize(image)) {
                image = resizeImage(image);
            }

            String pathToImage = saveImage(image);
            imagesList.add(pathToImage);

            return urlToMarkDownImageLink(pathToImage);
        }

        return null;
    }

    public void deleteNoUsingImages(String text) {
        List<String> findImages = findImagesInText(text);
        imagesList.removeAll(findImages);

        imagesList.forEach(i -> deleteImages(i));
    }

    public void deleteAllImages(String text) {
        List<String> findImages = findImagesInText(text);
        findImages.forEach(i -> deleteImages(i));
    }

    public void findAndLoadImages(String text) {
        imagesList.addAll(findImagesInText(text));
    }

    private void deleteImages(String url) {
        Path pathToImage = Paths.get(preferences.getWorkFolder(), "content", url);
        log.info("delete image: " + url);
        try {
            Files.delete(pathToImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> findImagesInText(String text) {
        return findInText(text, "\\(.*?jpg\\)").stream()
                .map(u -> u.substring(1, u.length() - 1)).collect(Collectors.toList());
    }

    public List<String> findMarkUrlInText(String text) {
        return findInText(text, "!\\[[^\\[\\]]*?\\]\\(.*?jpg\\)");
    }

    private List<String> findInText(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        List<String> findImages = new ArrayList<>();

        while(matcher.find()) {
            String url = matcher.group();
            findImages.add(url);
        }

        return findImages;
    }

    protected BufferedImage loadImage(File image) {
        BufferedImage buffImage = null;

        try {
            buffImage = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffImage;
    }

    private boolean validateImageSize(BufferedImage image) {
        if (image != null && (image.getHeight() < IMAGE_SIZE
                || image.getWidth() < IMAGE_SIZE)) {
            return true;
        }

        return false;
    }

    private BufferedImage resizeImage(BufferedImage buffImage) {
        log.info("resize image");
        return Scalr.resize(buffImage, Scalr.Method.QUALITY, IMAGE_SIZE);
    }

    private String saveImage(BufferedImage image) {

        String imageName = Generator.generateRandomId();

        Path pathToImage = Paths.get(preferences.getWorkFolder(), "content", "images", imageName + ".jpg");

        try {
            ImageIO.write(image, "jpg", pathToImage.toFile());
            log.info("save image to file: " + Paths.get("content", "images", imageName + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/images" + "/" + imageName + ".jpg";
    }

    private String urlToMarkDownImageLink(String url) {
        counter++;
        return "![image" + counter + "](" + url + ")";
    }

}
