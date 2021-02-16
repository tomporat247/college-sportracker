package com.example.sportracker.ContestControl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.Proof;
import com.example.sportracker.Models.Team;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ContestControlViewModel extends ViewModel {
    public LiveData<Map<Team, List<User>>> getTeamToUsers() {
        return ContestService.getInstance().getTeamToUsers();
    }

    public void setUsers(List<User> users) {
        // TODO: Remove
        ArrayList<Proof> tmpProofs = new ArrayList<>();
        tmpProofs.add(new Proof("https://www.gannett-cdn.com/media/USATODAY/USATODAY/2013/05/13/05-13-2013-dwyane-wade-3_4.jpg", new Date()));
        tmpProofs.add(new Proof("https://arc-anglerfish-arc2-prod-pmn.s3.amazonaws.com/public/QJGP36X3ANEQ7EARDLBRBHIDII.jpg", new Date()));
        tmpProofs.add(new Proof("https://www.sportscasting.com/wp-content/uploads/2020/01/Los-Angeles-Laker-Kobe-Bryant.jpg", new Date()));
        tmpProofs.add(new Proof("https://www.nbcsports.com/sites/rsnunited/files/styles/article_hero_image/public/article/hero/dame_0.jpg", new Date()));
        ContestService.getInstance().setContest(new Contest("", users, new ArrayList<>(), tmpProofs));
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
