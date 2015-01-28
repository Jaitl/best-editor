package com.jaitlapps.besteditor.domain;

import java.io.File;

public class Group {
    private String id;
    private String title;
    private String pathToImage;

    public Group(String title) {

        this.title = title;
    }

    public Group() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
