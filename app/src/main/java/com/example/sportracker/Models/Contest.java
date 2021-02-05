package com.example.sportracker.Models;

import java.util.List;

public class Contest {
    private List<User> users;
    private String name;
    private List<Match> matches;

    public Contest(String name, List<User> users, List<Match> matches) {
        this.name = name;
        this.users = users;
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
