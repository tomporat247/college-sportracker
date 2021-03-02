package com.example.sportracker.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(tableName = "contests")
public class BasicContest {
    @PrimaryKey
    @NonNull
    private String id;
    private List<User> users;
    private List<String> userIds;
    private String name;
    private List<Proof> proofs;

    public BasicContest() {

    }

    public BasicContest(String name, @NonNull List<User> users, List<Match> matches, List<Proof> proofs) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.users = users;
        this.userIds = users.stream().map(User::getId).collect(Collectors.toList());
        this.proofs = proofs;
        if (this.proofs == null) {
            this.proofs = new ArrayList<>();
        }
    }

    public BasicContest(Map<String, Object> firestoreDocument) {
        this.id = (String) firestoreDocument.get("id");
        this.name = (String) firestoreDocument.get("name");
        this.users = null;
        this.userIds = (List<String>) firestoreDocument.get("users");
        this.proofs = ((List<Map<String, Object>>) firestoreDocument.get("proofs")).stream().map(Proof::new).collect(Collectors.toList());
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> contestMap = new HashMap<>();

        contestMap.put("id", this.id);
        contestMap.put("name", this.name);
        contestMap.put("users", this.users.stream().map(User::getId).collect(Collectors.toList()));
        contestMap.put("proofs", this.proofs.stream().map(Proof::toDoc).collect(Collectors.toList()));

        return contestMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.userIds = users.stream().map(User::getId).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Proof> getProofs() {
        return proofs;
    }

    public void setProofs(List<Proof> proofs) {
        this.proofs = proofs;
    }
}