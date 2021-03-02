package com.example.sportracker.Database;

import com.example.sportracker.Models.Contest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FirestoreCacheHandler {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final AppDatabase appDatabase = AppDatabase.getInstance();
    private static HashMap<String, Contest> idToContest;

    public static void cacheServerContests() {
        idToContest = new HashMap<>();
        listenToUserContests();
    }

    private static void listenToUserContests() {
        firestore
                .collection("contests")
                .whereArrayContains("users", auth.getCurrentUser().getUid())
                .addSnapshotListener((value, error) -> {
                    List<Contest> contests =
                            value.getDocuments().stream().map(documentSnapshot ->
                                    new Contest(documentSnapshot.getData())).collect(Collectors.toList());
                    updateContests(contests);
                });
    }

    private static void updateContests(List<Contest> contestsInServer) {
        Set<String> irrelevantContestIds = idToContest.keySet();
        contestsInServer.forEach(contest -> irrelevantContestIds.remove(contest.getId()));
        irrelevantContestIds.forEach(contestId -> deleteContest(idToContest.get(contestId)));
        contestsInServer.forEach(FirestoreCacheHandler::cacheContest);
        idToContest = (HashMap<String, Contest>) contestsInServer.stream().collect(Collectors.toMap(Contest::getId, contest -> contest));
    }

    private static void deleteContest(Contest contest) {
        Executors.newSingleThreadExecutor().execute(() -> {
            appDatabase.contestDao().deleteContest(contest.getBasicContest());
            appDatabase.matchesDao().deleteContestMatches(contest.getId());
        });
    }

    private static void cacheContest(Contest contest) {
        Executors.newSingleThreadExecutor().execute(() -> {
            appDatabase.contestDao().addBasicContest(contest.getBasicContest());
            appDatabase.matchesDao().deleteContestMatches(contest.getId());
            appDatabase.matchesDao().addMatches(contest.getMatches());
        });
    }
}
