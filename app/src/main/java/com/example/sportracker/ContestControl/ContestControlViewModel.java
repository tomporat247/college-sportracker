package com.example.sportracker.ContestControl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.Team;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ContestControlViewModel extends ViewModel {
    public LiveData<Map<Team, List<User>>> getTeamToUsers() {
        return ContestService.getInstance().getTeamToUsers();
    }

    public void setUsers(List<User> users) {
        ContestService.getInstance().setContest(new Contest("", users, new ArrayList<>()));
        HashMap<Team, List<String>> teamToUsersValue = new HashMap<>();
        teamToUsersValue.put(Team.OUT, users.stream().map(User::getId).collect(Collectors.toList()));
        teamToUsersValue.put(Team.A, new ArrayList<>());
        teamToUsersValue.put(Team.B, new ArrayList<>());
        ContestService.getInstance().setTeamToUserIds(teamToUsersValue);
    }

    public void swapUserTeam(String userId, Team newTeam) {
        ContestService.getInstance().swapUserTeam(userId, newTeam);
    }

    public void addMatch(Team winningTeam) {
        ContestService.getInstance().addMatch(winningTeam);
    }
}
