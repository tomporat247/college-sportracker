package com.example.sportracker.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final ListenerRegistration contestListenerRegistration;

    private final MutableLiveData<List<Contest>> contests;

    public HomeViewModel() {
        this.contests = new MutableLiveData<>();
        this.contestListenerRegistration = this.listenToCurrentUserContestChanges();
    }

    public LiveData<List<Contest>> getContests() {
        return this.contests;
    }

    private ListenerRegistration listenToCurrentUserContestChanges() {
        return this.firestore
                .collection("contests")
                .whereArrayContains("users", this.auth.getCurrentUser().getUid())
                .addSnapshotListener((value, error) ->
                        contests.setValue(value.getDocuments().stream().map(documentSnapshot -> new Contest(documentSnapshot.getData())).collect(Collectors.toList())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.contestListenerRegistration.remove();
    }
}