package com.example.sportracker.Services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.Team;
import com.example.sportracker.Models.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContestService {
    private static final ContestService instance = new ContestService();
    private final MutableLiveData<Contest> contest;
    private final MutableLiveData<HashMap<Team, List<String>>> teamToUserIds;

    public static ContestService getInstance() {
        return instance;
    }

    private ContestService() {
        this.contest = new MutableLiveData<>();
        this.teamToUserIds = new MutableLiveData<>();
    }

    public void setContest(Contest contest) {
        this.contest.setValue(contest);
    }

    public LiveData<Contest> getContest() {
        return this.contest;
    }

    public void setTeamToUserIds(HashMap<Team, List<String>> teamToUserIds) {
        this.teamToUserIds.setValue(teamToUserIds);
    }

    public LiveData<HashMap<Team, List<String>>> getTeamToUserIds() {
        return this.teamToUserIds;
    }

    public LiveData<Map<Team, List<User>>> getTeamToUsers() {
        Map<String, User> userIdToUser = this.getUserIdToUser();

        return Transformations.map(this.teamToUserIds, teamToUserIds -> teamToUserIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(userIdToUser::get).collect(Collectors.toList())
                )));
    }

    public void swapUserTeam(String userId, Team newTeam) {
        HashMap<Team, List<String>> teamToUsersValue = this.teamToUserIds.getValue();
        User user = this.contest.getValue().getUsers().stream().filter(currentUser -> currentUser.getId().equals(userId)).findFirst().orElse(null);
        if (user != null) {
            Arrays.stream(Team.values()).forEach(team -> teamToUsersValue.get(team).removeIf(currentUserId -> currentUserId.equals(userId)));
            teamToUsersValue.get(newTeam).add(user.getId());
            this.teamToUserIds.setValue(teamToUsersValue);
        }
    }

    private HashMap<String, User> getUserIdToUser() {
        HashMap<String, User> userIdToUser = new HashMap<>();
        for (User user : this.contest.getValue().getUsers()) {
            userIdToUser.put(user.getId(), user);
        }
        return userIdToUser;
    }
}