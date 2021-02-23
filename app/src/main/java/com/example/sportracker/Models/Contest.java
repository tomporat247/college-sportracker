package com.example.sportracker.Models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public Contest(Map<String, Object> firestoreDocument) {
        this.id = (String) firestoreDocument.get("id");
        this.name = (String) firestoreDocument.get("name");
        this.users = null;
        this.matches = ((List<Map<String, Object>>) firestoreDocument.get("matches")).stream().map(Match::new).collect(Collectors.toList());
        this.proofs = ((List<Map<String, Object>>) firestoreDocument.get("proofs")).stream().map(Proof::new).collect(Collectors.toList());
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> contestMap = new HashMap<>();

        contestMap.put("id", this.id);
        contestMap.put("name", this.name);
        contestMap.put("users", this.users.stream().map(User::getId).collect(Collectors.toList()));
        contestMap.put("matches", this.matches.stream().map(Match::toDoc).collect(Collectors.toList()));
        contestMap.put("proofs", this.proofs.stream().map(Proof::toDoc).collect(Collectors.toList()));

        return contestMap;
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

    public void addProof(Proof proof) {
        this.getProofs().add(0, proof);
    }

    public boolean isContestNew() {
        return this.getName() == null;
    }
}
