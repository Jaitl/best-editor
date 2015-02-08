package com.jaitlapps.besteditor.domain;

public class RecordEntry extends Entry {
    private String groupId;
    private boolean isAuthorExist;
    private String authorName;
    private String authorURL;
    private String pathToContent;

    @Override
    public void update(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        setTitle(recordEntry.getTitle());
        setPathToImage(recordEntry.getPathToImage());
        setGroupId(recordEntry.getGroupId());
        setAuthorExist(recordEntry.isAuthorExist());
        setAuthorName(recordEntry.getAuthorName());
        setAuthorURL(recordEntry.getAuthorURL());
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean isAuthorExist() {
        return isAuthorExist;
    }

    public void setAuthorExist(boolean isAuthorExist) {
        this.isAuthorExist = isAuthorExist;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public String getPathToContent() {
        return pathToContent;
    }

    public void setPathToContent(String pathToContent) {
        this.pathToContent = pathToContent;
    }
}
