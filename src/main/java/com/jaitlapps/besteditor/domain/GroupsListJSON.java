package com.jaitlapps.besteditor.domain;

import java.util.ArrayList;
import java.util.List;

public class GroupsListJSON {
    private List<Group> list;

    public GroupsListJSON() {
        list = new ArrayList<>();
    }

    public void addGroup(Group group) {
        list.add(group);
    }

    public List<Group> getList() {
        return list;
    }

    public void setList(List<Group> list) {
        this.list = list;
    }
}
