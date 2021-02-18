package com.example.sportracker.ContestInfo.Users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.ContestUserDetails;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContestInfoUsersViewModel extends ViewModel {

    public Map<String, ContestUserDetails> getIdToUserDetails() {
        return ContestService.getInstance().getContest().getValue().getIdToUserDetails();
    }

    LiveData<List<User>> getOrderedUsers() {
        return Transformations.map(ContestService.getInstance().getContest(), contest -> {
            Map<String, ContestUserDetails> idToUserDetails = this.getIdToUserDetails();
            List<User> users = new ArrayList<>(contest.getUsers());
            users.sort((User first, User second) ->
                    idToUserDetails.get(first.getId()).getWinLossRatio() >= idToUserDetails.get(second.getId()).getWinLossRatio() ? 1 : -1);
            return users;
        });
    }
}
