package com.jaitlapps.besteditor.domain;

public class RecordEntry extends Entry {
    private String groupId;
    private boolean isAuthorExist;
    private String AuthorName;
    private String AuthorURL;

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
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getAuthorURL() {
        return AuthorURL;
    }

    public void setAuthorURL(String authorURL) {
        AuthorURL = authorURL;
    }
}
