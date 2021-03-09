package com.example.sportracker.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.UserStatistics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<UserStatistics> userStatistics;

    public ProfileViewModel() {
        this.userStatistics = new MutableLiveData<>();
    }

    public CompletableFuture<Object> loadUserData(String id) {
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();

        String userId = id != null ? id : FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId).get()
                .addOnFailureListener(completableFuture::completeExceptionally)
                .addOnSuccessListener(userDocumentSnapshot ->
                        FirebaseFirestore.getInstance()
                                .collection("contests")
                                .whereArrayContains("users", userId)
                                .addSnapshotListener((value, error) -> {
                                    List<Contest> contests =
                                            value.getDocuments().stream().map(contestDocumentSnapshot ->
                                                    new Contest(contestDocumentSnapshot.getData())).collect(Collectors.toList());
                                    this.userStatistics.setValue(new UserStatistics(userId, userDocumentSnapshot.getData(), contests));
                                    if (!completableFuture.isDone()) {
                                        completableFuture.complete("");
                                    }
                                }));
        return completableFuture;
    }

    public LiveData<UserStatistics> getUserStatistics() {
        return this.userStatistics;
    }
}