package com.jaitlapps.besteditor;

import java.util.prefs.Preferences;

public class CommonPreferences {
    private Preferences preferences;

    public static final String WORK_FOLDER = "work_folder";

    public CommonPreferences() {
        preferences = Preferences.userNodeForPackage(Preferences.class);
    }

    public void putWorkFolder(String url) {
        preferences.put(WORK_FOLDER, url);
    }

    public String getWorkFolder() {
        return preferences.get(WORK_FOLDER, null);
    }
}