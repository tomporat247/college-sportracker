package com.example.sportracker.ContestControl;

import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.User;

import java.util.List;

public class ContestControlViewModel extends ViewModel {
    private List<User> users;

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
