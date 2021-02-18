package com.example.sportracker.ContestInfo.Matches;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.Match;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;

import java.util.List;
import java.util.stream.Collectors;

public class ContestInfoMatchesViewModel extends ViewModel {
    LiveData<List<Match>> getMatches() {
        return Transformations.map(ContestService.getInstance().getContest(), Contest::getMatches);
    }

    void removeMatch(String matchId) {
        ContestService.getInstance().removeMatch(matchId);
    }

    private User getUserById(String id) {
        return ContestService.getInstance().getContest().getValue().getUsers().stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    List<User> getUsersByIds(List<String> ids) {
        return ids.stream().map(this::getUserById).collect(Collectors.toList());
    }
}
