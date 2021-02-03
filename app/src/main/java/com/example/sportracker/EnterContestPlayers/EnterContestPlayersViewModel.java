package com.example.sportracker.EnterContestPlayers;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

    private boolean doesUserEmailExist(String email) {
        return this.users.getValue().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null) != null;
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

    public boolean isCurrentUserEmail(String email) {
        return this.auth.getCurrentUser().getEmail().equals(email);
    }

    public void removeUserByEmail(String email) {
        List<User> userList = users.getValue();
        userList.removeIf(user -> user.getEmail().equals(email));
        users.setValue(userList);
    }

    private void addSelfUser() {
        final ArrayList<User> currentUsers = new ArrayList<>();
        final FirebaseUser currentUser = this.auth.getCurrentUser();
        currentUsers.add(new User(currentUser.getUid(), currentUser.getEmail(), currentUser.getDisplayName(), currentUser.getPhotoUrl().toString()));
        this.users.setValue(currentUsers);
    }
}
