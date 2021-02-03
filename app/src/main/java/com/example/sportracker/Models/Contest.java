package com.example.sportracker.Models;

import java.util.List;

public class Contest {
    private List<User> users;
    private String name;

    public Contest(String name, List<User> users) {
        this.setName(name);
        this.setUsers(users);
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
