package com.jaitlapps.besteditor.domain;

import java.util.ArrayList;
import java.util.List;

public class GroupsListEntry {
    private List<GroupEntry> list;

    public GroupsListEntry() {
        list = new ArrayList<>();
    }

    public void addGroup(GroupEntry groupEntry) {
        list.add(groupEntry);
    }

    public List<GroupEntry> getList() {
        return list;
    }

    public void setList(List<GroupEntry> list) {
        this.list = list;
    }

    public void updateGroup(GroupEntry groupEntry) {
        GroupEntry group = list.stream().filter(g -> g.getId() == groupEntry.getId()).findFirst().get();

        group.setTitle(groupEntry.getTitle());
        group.setPathToImage(groupEntry.getPathToImage());
    }
}
