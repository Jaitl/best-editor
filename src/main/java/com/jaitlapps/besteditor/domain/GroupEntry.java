package com.jaitlapps.besteditor.domain;

public class GroupEntry extends Entry {

    @Override
    public void update(Entry entry) {
        GroupEntry groupEntry = (GroupEntry) entry;

        setTitle(groupEntry.getTitle());
        setPathToImage(groupEntry.getPathToImage());
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
