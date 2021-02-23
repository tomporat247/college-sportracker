package com.example.sportracker.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final ListenerRegistration contestListenerRegistration;
    private final HashMap<String, List<String>> contestIdToUserIds;
    private final MutableLiveData<List<Contest>> contests;

    public HomeViewModel() {
        this.contests = new MutableLiveData<>();
        this.contestIdToUserIds = new HashMap<>();
        this.contestListenerRegistration = this.listenToCurrentUserContestChanges();
    }

    public LiveData<List<Contest>> getContests() {
        return this.contests;
    }

    public CompletableFuture<Object> selectContest(String contestId) {
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();
        Contest selectedContest = this.contests.getValue().stream().filter(contest ->
                contest.getId().equals(contestId)).findFirst().orElse(null);

        this.findContestUsers(contestId).whenComplete((users, exception) -> {
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

    private CompletableFuture<List<User>> findContestUsers(String contestId) {
        CompletableFuture<List<User>> completableFuture = new CompletableFuture<>();
        this.firestore
                .collection("users")
                .whereIn(FieldPath.documentId(), this.contestIdToUserIds.get(contestId)).get()
                .addOnFailureListener(completableFuture::completeExceptionally)
                .addOnSuccessListener(snapshots ->
                        completableFuture.complete(snapshots.getDocuments().stream().map(userDoc ->
                                new User(userDoc.getId(), userDoc.getData())).collect(Collectors.toList())));

        return completableFuture;
    }

    private ListenerRegistration listenToCurrentUserContestChanges() {
        this.contestIdToUserIds.clear();
        return this.firestore
                .collection("contests")
                .whereArrayContains("users", this.auth.getCurrentUser().getUid())
                .addSnapshotListener((value, error) ->
                        contests.setValue(value.getDocuments().stream().map(documentSnapshot -> {
                            this.contestIdToUserIds.put((String) documentSnapshot.get("id"), (List<String>) documentSnapshot.get("users"));
                            return new Contest(documentSnapshot.getData());
                        }).collect(Collectors.toList())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.contestListenerRegistration.remove();
    }
}