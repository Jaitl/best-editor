package com.jaitlapps.besteditor;

import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.GroupsListEntry;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class GroupManager {

    private static Logger log = Logger.getLogger(GroupManager.class.getName());
    private static final GroupManager INSTANCE  = new GroupManager();
    private GroupsListEntry groupsListEntry;
    private ObjectMapper mapper;

    private Path pathToDataDir;
    private Path pathToGroupData;

    private GroupManager() {
        mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY);

        CommonPreferences preferences = CommonPreferences.getInstance();

        pathToDataDir = Paths.get(preferences.getWorkFolder(), "data");
        pathToGroupData = pathToDataDir.resolve("group.json");

        loadGroupsFromFile();
    }

    public static GroupManager getInstance() {
        return INSTANCE;
    }

    public void updateGroup(GroupEntry groupEntry) {
        groupsListEntry.updateGroup(groupEntry);
        log.info("update group: " + groupEntry.getId());
    }

    public void addGroup(GroupEntry groupEntry) {
        groupsListEntry.addGroup(groupEntry);
        log.info("add group: " + groupEntry.getId());
    }

    public List<GroupEntry> getGroups() {
        return groupsListEntry.getList();
    }

    public void saveGroupsToFile() {
        try {
            mapper.writeValue(pathToGroupData.toFile(), groupsListEntry);
            log.info("save groups to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGroupsFromFile() {
        if(Files.exists(pathToGroupData)) {
            try {
                groupsListEntry = mapper.readValue(pathToGroupData.toFile(), GroupsListEntry.class);
                log.info("save groups from file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            groupsListEntry = new GroupsListEntry();
            log.info("create new group list");
        }
    }
}
