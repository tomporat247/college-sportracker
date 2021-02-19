package com.example.sportracker.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Contest {
    private final String id;
    private List<User> users;
    private String name;
    private List<Match> matches;
    private List<Proof> proofs;

    public Contest(String name, List<User> users, List<Match> matches, List<Proof> proofs) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.users = users;
        this.matches = matches;
        this.proofs = proofs;
    }

    public String getId() {
        return id;
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

    public Map<String, ContestUserDetails> getIdToUserDetails() {
        HashMap<String, ContestUserDetails> idToUserDetails = new HashMap<>();

        for (User user : this.users) {
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

    public void setUsers(List<User> users) {
        this.users = users;
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

    public void addMatch(HashMap<Team, List<String>> teamToUserIds, Team winningTeam) {
        List<String> winningTeamUserIds = teamToUserIds.get(winningTeam);
        List<String> losingTeamUserIds = teamToUserIds.get(winningTeam == Team.A ? Team.B : Team.A);
        this.matches.add(0, new Match(winningTeamUserIds, losingTeamUserIds));
    }

    public void removeMatch(String matchId) {
        this.matches.removeIf(match -> match.getId().equals(matchId));
    }
}
