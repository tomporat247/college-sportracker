package com.example.sportracker.ContestControl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Team;
import com.example.sportracker.Models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ContestControlViewModel extends ViewModel {
    private final MutableLiveData<HashMap<Team, List<User>>> teamToUsers;

    public ContestControlViewModel() {
        this.teamToUsers = new MutableLiveData<>();
        this.teamToUsers.setValue(new HashMap<>());
    }

    public LiveData<HashMap<Team, List<User>>> getTeamToUsers() {
        return this.teamToUsers;
    }

    public void setUsers(List<User> users) {
        HashMap<Team, List<User>> teamToUsersValue = new HashMap<>();
        teamToUsersValue.put(Team.OUT, users);
        teamToUsersValue.put(Team.A, new ArrayList<>());
        teamToUsersValue.put(Team.B, new ArrayList<>());
        this.teamToUsers.setValue(teamToUsersValue);
    }

    public void swapUserTeam(String userId, Team newTeam) {
        HashMap<Team, List<User>> teamToUsersValue = this.teamToUsers.getValue();
        User user = this.getAllUsers().stream().filter(currentUser -> currentUser.getId().equals(userId)).findFirst().orElse(null);
        if (user != null) {
            Arrays.stream(Team.values()).forEach(team -> teamToUsersValue.get(team).removeIf(currentUser -> currentUser.getId().equals(userId)));
            teamToUsersValue.get(newTeam).add(user);
            this.teamToUsers.setValue(teamToUsersValue);
        }
    }

    private List<User> getAllUsers() {
        return this.teamToUsers.getValue().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
