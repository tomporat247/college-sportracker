package com.example.sportracker.EnterContestPlayers;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class EnterContestPlayersViewModel extends ViewModel {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<User>> users;

    public EnterContestPlayersViewModel() {
        this.users = new MutableLiveData<>();
        final ArrayList<User> tmpUsers = new ArrayList<>();
        tmpUsers.add(new User("1", "abc@gmail.com", "abc", "https://i.stack.imgur.com/B1vOw.jpg?s=32&g=1"));
        tmpUsers.add(new User("2", "abcd@gmail.com", "abcef", "https://i.stack.imgur.com/B1vOw.jpg?s=32&g=1"));
        this.users.setValue(tmpUsers);
    }

    public LiveData<List<User>> getUsers() {
        return this.users;
    }

    public Future<User> addUser(String email) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        this.firestore.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    completableFuture.completeExceptionally(new Resources.NotFoundException("User not found"));
                } else {
                    User user = new User(task.getResult().getDocuments().get(0).getData());
                    List<User> userList = users.getValue();
                    userList.add(user);
                    users.setValue(userList);
                }
            }
        });
        return completableFuture;
    }

    public void removeUser(User user) {
        List<User> userList = users.getValue();
        userList.remove(user);
        users.setValue(userList);
    }
}
