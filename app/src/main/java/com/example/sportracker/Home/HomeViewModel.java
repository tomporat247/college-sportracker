package com.example.sportracker.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Database.AppDatabase;
import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public LiveData<List<Contest>> getContests() {
        return AppDatabase.getInstance().contestDao().getAllContests();
    }

    public CompletableFuture<Object> deleteContest(String contestId) {
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance().contestDao().deleteContest(contestId);
            AppDatabase.getInstance().matchesDao().deleteContestMatches(contestId);
            this.firestore.collection("contests").document(contestId)
                    .delete()
                    .addOnFailureListener(completableFuture::completeExceptionally)
                    .addOnSuccessListener(x -> completableFuture.complete("done"));
        });
        return completableFuture;
    }

    public CompletableFuture<Object> selectContest(Contest selectedContest) {
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();

        this.findContestUsers(selectedContest).whenComplete((users, exception) -> {
            if (exception != null) {
                completableFuture.completeExceptionally(exception);
            } else {
                selectedContest.setUsers(users);
                ContestService.getInstance().setContest(selectedContest);
                completableFuture.complete(selectedContest);
            }
        });

        return completableFuture;
    }

    private CompletableFuture<List<User>> findContestUsers(Contest contest) {
        CompletableFuture<List<User>> completableFuture = new CompletableFuture<>();
        this.firestore
                .collection("users")
                .whereIn(FieldPath.documentId(), contest.getBasicContest().getUserIds()).get()
                .addOnFailureListener(completableFuture::completeExceptionally)
                .addOnSuccessListener(snapshots ->
                        completableFuture.complete(snapshots.getDocuments().stream().map(userDoc ->
                                new User(userDoc.getId(), userDoc.getData())).collect(Collectors.toList())));

        return completableFuture;
    }
}