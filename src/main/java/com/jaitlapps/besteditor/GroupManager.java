package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.Group;
import com.jaitlapps.besteditor.domain.GroupsListJSON;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GroupManager {

    private static final GroupManager INSTANCE  = new GroupManager();
    private GroupsListJSON groupsListJSON;
    private ObjectMapper mapper;

    private Path pathToDataDir;
    private Path pathToGroupData;

    private GroupManager() {
        mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);

        CommonPreferences preferences = new CommonPreferences();

        pathToDataDir = Paths.get(preferences.getWorkFolder(), "data");
        pathToGroupData = pathToDataDir.resolve("group.json");

        loadGroupsFromFile();
    }

    public static GroupManager getInstance() {
        return INSTANCE;
    }

    public void addGroup(Group group) {
        groupsListJSON.addGroup(group);
    }
    public List<Group> getGroups() {
        return groupsListJSON.getList();
    }

    public void saveGroupsToFile() {
        if(Files.notExists(pathToDataDir)) {
            try {
                Files.createDirectory(pathToDataDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            mapper.writeValue(pathToGroupData.toFile(), groupsListJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGroupsFromFile() {
        if(Files.notExists(pathToDataDir)) {
            try {
                Files.createDirectory(pathToDataDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Files.exists(pathToGroupData)) {
            try {
                groupsListJSON = mapper.readValue(pathToGroupData.toFile(), GroupsListJSON.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            groupsListJSON = new GroupsListJSON();
        }
    }
}
