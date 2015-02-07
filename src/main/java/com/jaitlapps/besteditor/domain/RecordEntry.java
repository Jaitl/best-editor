package com.jaitlapps.besteditor.domain;

public class RecordEntry extends Entry {


    @Override
    public void update(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        setTitle(recordEntry.getTitle());
        setPathToImage(recordEntry.getPathToImage());
    }
}
