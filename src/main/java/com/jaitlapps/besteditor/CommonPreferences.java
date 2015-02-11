package com.jaitlapps.besteditor;

import java.util.prefs.Preferences;

public class CommonPreferences {
    private Preferences preferences;

    public static final String WORK_FOLDER = "work_folder";
    public static final String RESENT_ICON_FOLDER = "resent_icon_folder";
    public static final String RESENT_IMAGE_FOLDER = "resent_image_folder";
    public static final String RESENT_ARCHIVE_FOLDER = "resent_archive_folder";
    public static final String MARKDOWN_EDITOR_EXE = "markdown_editor_exe";

    private static final CommonPreferences INSTANCE = new CommonPreferences();

    public static CommonPreferences getInstance() {
        return INSTANCE;
    }

    private CommonPreferences() {
        preferences = Preferences.userNodeForPackage(Preferences.class);
    }

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
}