package com.example.sportracker.Models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Contest {
    @Embedded
    private BasicContest basicContest;
    @Relation(
            entity = Match.class,
            parentColumn = "id",
            entityColumn = "contestId"
    )
    private List<Match> matches;

    public Contest() {
        this.basicContest = new BasicContest();
    }

    public Contest(String name, @NonNull List<User> users, List<Match> matches, List<Proof> proofs) {
        this.basicContest = new BasicContest(name, users, matches, proofs);
        this.matches = matches;
        if (this.matches == null) {
            this.matches = new ArrayList<>();
        }
    }

    public Contest(Map<String, Object> firestoreDocument) {
        this.basicContest = new BasicContest(firestoreDocument);
        this.matches = ((List<Map<String, Object>>) firestoreDocument.get("matches")).stream().map(Match::new).collect(Collectors.toList());
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> contestMap = this.basicContest.toDoc();
        contestMap.put("matches", this.matches.stream().map(Match::toDoc).collect(Collectors.toList()));
        return contestMap;
    }

    public BasicContest getBasicContest() {
        return basicContest;
    }

    public void setBasicContest(BasicContest basicContest) {
        this.basicContest = basicContest;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String getId() {
        return this.basicContest.getId();
    }

    public List<User> getUsers() {
        return this.basicContest.getUsers();
    }

    public void setUsers(List<User> users) {
        this.basicContest.setUsers(users);
    }

    public String getName() {
        return this.basicContest.getName();
    }

    public void setName(String name) {
        this.basicContest.setName(name);
    }

    public List<Proof> getProofs() {
        return this.basicContest.getProofs();
    }

    public Map<String, ContestUserDetails> getIdToUserDetails() {
        HashMap<String, ContestUserDetails> idToUserDetails = new HashMap<>();

        for (User user : this.basicContest.getUsers()) {
            idToUserDetails.put(user.getId(), new ContestUserDetails(0, 0));
        }

        for (Match match : this.matches) {
            for (String winnerId : match.getWinningTeamUserIds()) {
                idToUserDetails.get(winnerId).incrementWinAmount();
            }
            for (String loserId : match.getLosingTeamUserIds()) {
                idToUserDetails.get(loserId).incrementLossAmount();
            }
        }

        return idToUserDetails;
    }

    public void addMatch(HashMap<Team, List<String>> teamToUserIds, Team winningTeam) {
        List<String> winningTeamUserIds = teamToUserIds.get(winningTeam);
        List<String> losingTeamUserIds = teamToUserIds.get(winningTeam == Team.A ? Team.B : Team.A);
        this.matches.add(0, new Match(this.basicContest.getId(), winningTeamUserIds, losingTeamUserIds));
    }

    public void removeMatch(String matchId) {
        this.matches.removeIf(match -> match.getId().equals(matchId));
    }

    public void addProof(Proof proof) {
        this.basicContest.getProofs().add(0, proof);
    }

    public boolean isContestNew() {
        return this.basicContest.getName() == null;
    }
}
