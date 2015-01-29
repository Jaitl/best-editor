package com.jaitlapps.besteditor;

import java.util.prefs.Preferences;

public class CommonPreferences {
    private Preferences preferences;

    private static final CommonPreferences INSTANCE = new CommonPreferences();

    public static CommonPreferences getInstance() {
        return INSTANCE;
    }

    public static final String WORK_FOLDER = "work_folder";

    private CommonPreferences() {
        preferences = Preferences.userNodeForPackage(Preferences.class);
    }

    public void putWorkFolder(String url) {
        preferences.put(WORK_FOLDER, url);
    }

    public String getWorkFolder() {
        return preferences.get(WORK_FOLDER, null);
    }
}