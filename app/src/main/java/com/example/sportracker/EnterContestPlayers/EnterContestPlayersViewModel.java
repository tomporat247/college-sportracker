package com.example.sportracker.EnterContestPlayers;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.Match;
import com.example.sportracker.Models.Proof;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnterContestPlayersViewModel extends ViewModel {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<User>> users;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public EnterContestPlayersViewModel() {
        this.users = new MutableLiveData<>();
        this.addSelfUser();
    }

    public LiveData<List<User>> getUsers() {
        return this.users;
    }

    public CompletableFuture<User> addUser(String email) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();

        if (email.isEmpty()) {
            completableFuture.completeExceptionally(new Resources.NotFoundException("Insert email"));
        } else if (this.doesUserEmailExist(email)) {
            completableFuture.completeExceptionally(new Resources.NotFoundException("User already exists"));
        } else {
            this.firestore.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        completableFuture.completeExceptionally(new Resources.NotFoundException("User not found"));
                    } else {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        User user = new User(document.getId(), document.getData());
                        List<User> userList = users.getValue();
                        userList.add(user);
                        users.setValue(userList);
                    }
                }
            });
        }
        return completableFuture;
    }

    public void removeUserByEmail(String email) {
        if (!this.isCurrentUserEmail(email)) {
            List<User> userList = users.getValue();
            userList.removeIf(user -> user.getEmail().equals(email));
            users.setValue(userList);
        }
    }

    public void createContest() {
        ContestService.getInstance().setContest(this.getTempContest(this.users.getValue()));
    }

    private boolean doesUserEmailExist(String email) {
        return this.users.getValue().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null) != null;
    }

    private boolean isCurrentUserEmail(String email) {
        return this.auth.getCurrentUser().getEmail().equals(email);
    }

    private void addSelfUser() {
        final ArrayList<User> currentUsers = new ArrayList<>();
        final FirebaseUser currentUser = this.auth.getCurrentUser();
        currentUsers.add(new User(currentUser.getUid(), currentUser.getEmail(), currentUser.getDisplayName(), currentUser.getPhotoUrl().toString()));
        this.users.setValue(currentUsers);
    }

    private Contest getTempContest(List<User> users) {
        // TODO: Remove
        ArrayList<Proof> tmpProofs = new ArrayList<>();
        tmpProofs.add(new Proof("https://www.gannett-cdn.com/media/USATODAY/USATODAY/2013/05/13/05-13-2013-dwyane-wade-3_4.jpg", new Date()));
        tmpProofs.add(new Proof("https://arc-anglerfish-arc2-prod-pmn.s3.amazonaws.com/public/QJGP36X3ANEQ7EARDLBRBHIDII.jpg", new Date()));
        tmpProofs.add(new Proof("https://www.sportscasting.com/wp-content/uploads/2020/01/Los-Angeles-Laker-Kobe-Bryant.jpg", new Date()));
        tmpProofs.add(new Proof("https://www.nbcsports.com/sites/rsnunited/files/styles/article_hero_image/public/article/hero/dame_0.jpg", new Date()));
        ArrayList<Match> tmpMatches = new ArrayList<>();
        ArrayList<String> tmpWinnerIds = new ArrayList<>();
        tmpWinnerIds.add(users.get(0).getId());
        tmpWinnerIds.add(users.get(1).getId());
        ArrayList<String> tmpLoserIds = new ArrayList<>();
        tmpLoserIds.add(users.get(1).getId());
        tmpMatches.add(new Match(tmpWinnerIds, tmpLoserIds));
        tmpMatches.add(new Match(tmpWinnerIds, tmpLoserIds));
        Match differentDateMatch = new Match(tmpWinnerIds, tmpLoserIds);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        differentDateMatch.setDate(c.getTime());
        tmpMatches.add(differentDateMatch);
        return new Contest("", users, tmpMatches, tmpProofs);
    }
}
