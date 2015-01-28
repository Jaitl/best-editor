package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupMapper {

    private List<Group> groupList;

    public GroupMapper() {
        groupList = new ArrayList<Group>();
    }

    public void addGroup(Group group) {
        groupList.add(group);
    }
}
