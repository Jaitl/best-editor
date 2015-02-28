package com.jaitlapps.besteditor;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class CommonPreferences {

    private static final FileProperties preferences = new FileProperties();

    public static final String WORK_FOLDER = "work_folder";
    public static final String RESENT_ICON_FOLDER = "resent_icon_folder";
    public static final String RESENT_IMAGE_FOLDER = "resent_image_folder";
    public static final String RESENT_ARCHIVE_FOLDER = "resent_archive_folder";
    public static final String MARKDOWN_EDITOR_EXE = "markdown_editor_exe";

    private static final CommonPreferences INSTANCE = new CommonPreferences();

    public static CommonPreferences getInstance() {
        return INSTANCE;
    }

    private CommonPreferences() { }

    public void putWorkFolder(String url) {
        preferences.put(WORK_FOLDER, url);
    }

    public String getWorkFolder() {
        return preferences.get(WORK_FOLDER, null);
    }

    public void putRecentIconFolder(String url) {
        preferences.put(RESENT_ICON_FOLDER, url);
    }

    public String getRecentIconFolder() {
        return preferences.get(RESENT_ICON_FOLDER, null);
    }

    public void putRecentImageFolder(String url) {
        preferences.put(RESENT_IMAGE_FOLDER, url);
    }

    public String getRecentImageFolder() {
        return preferences.get(RESENT_IMAGE_FOLDER, null);
    }

    public void putArchiveImageFolder(String url) {
        preferences.put(RESENT_ARCHIVE_FOLDER, url);
    }

    public String getArchiveImageFolder() {
        return preferences.get(RESENT_ARCHIVE_FOLDER, null);
    }

    public void putMarkdownEditor(String url) {
        preferences.put(MARKDOWN_EDITOR_EXE, url);
    }

    public String getMarkdownEditor() {
        return preferences.get(MARKDOWN_EDITOR_EXE, null);
    }

    private static class FileProperties {

        private static org.slf4j.Logger log = LoggerFactory.getLogger(FileProperties.class);

        private final Properties properties = new Properties();

        public FileProperties() {
            loadPropertiesFromFile();
        }

        public String get(String key, String defaultValue) {
            return properties.getProperty(key, defaultValue);
        }

        public void put(String key, String value) {
            properties.setProperty(key, value);
            storePropertiesToFile();
        }

        private void loadPropertiesFromFile() {
            try {
                InputStream is = new FileInputStream(getPathToFile());

                properties.load(is);
            } catch (FileNotFoundException e) {
                log.error("error", e);
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        private void storePropertiesToFile() {
            try {
                OutputStream os = new FileOutputStream(getPathToFile());
                properties.store(os, "");
            } catch (FileNotFoundException e) {
                log.error("error", e);
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        private String getPathToFile() {
            File jar = null;

            try {
                 jar = new File (FileProperties.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch (URISyntaxException e) {
                log.error("error", e);
            }

            Path mainPath = jar.getParentFile().toPath();
            Path pathToSettings = mainPath.resolve("settings");

            if(Files.notExists(pathToSettings)) {
                try {
                    Files.createDirectory(pathToSettings);
                } catch (IOException e) {
                    log.error("error", e);
                }
            }

            pathToSettings = pathToSettings.resolve("best.properties");

            if(Files.notExists(pathToSettings)) {
                try {
                    Files.createFile(pathToSettings);
                } catch (IOException e) {
                    log.error("error", e);
                }
            }

            return pathToSettings.toString();
        }
    }
}