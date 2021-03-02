package com.example.sportracker.Database;

import com.example.sportracker.Models.Contest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executors;

public class FirestoreCacheHandler {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final AppDatabase appDatabase = AppDatabase.getInstance();
    private static ListenerRegistration listenerRegistration;

    public static void cacheServerContests() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        listenToUserContests((value, error) ->
                value.getDocuments().forEach(documentSnapshot ->
                        cacheContest(new Contest(documentSnapshot.getData()))));
    }

    private static void listenToUserContests(EventListener<QuerySnapshot> listener) {
        listenerRegistration = firestore
                .collection("contests")
                .whereArrayContains("users", auth.getCurrentUser().getUid())
                .addSnapshotListener(listener);
    }

    private static void cacheContest(Contest contest) {
        Executors.newSingleThreadExecutor().execute(() -> {
            appDatabase.contestDao().addBasicContest(contest.getBasicContest());
            appDatabase.matchesDao().deleteMatches(contest.getMatches());
            appDatabase.matchesDao().addMatches(contest.getMatches());
        });
    }
}
