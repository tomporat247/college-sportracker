package com.example.sportracker.ContestControl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.format.DateFormat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Proof;
import com.example.sportracker.Models.Team;
import com.example.sportracker.Models.User;
import com.example.sportracker.Services.ContestService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public class ContestControlViewModel extends ViewModel {
    public LiveData<Map<Team, List<User>>> getTeamToUsers() {
        return ContestService.getInstance().getTeamToUsers();
    }

    public String getContestId() {
        return ContestService.getInstance().getContest().getValue().getId();
    }

    public void setUsers(List<User> users) {
        List<User> validatedUsers = users != null ? users : ContestService.getInstance().getContest().getValue().getUsers();
        HashMap<Team, List<String>> teamToUsersValue = new HashMap<>();
        teamToUsersValue.put(Team.OUT, validatedUsers.stream().map(User::getId).collect(Collectors.toList()));
        teamToUsersValue.put(Team.A, new ArrayList<>());
        teamToUsersValue.put(Team.B, new ArrayList<>());
        ContestService.getInstance().setTeamToUserIds(teamToUsersValue);
    }

    public void swapUserTeam(String userId, Team newTeam) {
        ContestService.getInstance().swapUserTeam(userId, newTeam);
    }

    public void addMatch(Team winningTeam) {
        ContestService.getInstance().addMatch(winningTeam);
    }

    public boolean isContestNew() {
        return ContestService.getInstance().getContest().getValue().isContestNew();
    }

    public void setContestName(String name) {
        ContestService.getInstance().setContestName(name);
    }

    public CompletableFuture<Object> saveContest() {
        return ContestService.getInstance().saveToFirestore();
    }

    public CompletableFuture<String> uploadPhotoToStorageBucket(Bitmap photo) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Date now = new Date();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("proofs")
                .child(this.getContestId())
                .child(DateFormat.format("dd-MM-yyyy HH:mm:ss", now) + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask
                .addOnFailureListener(exception -> completableFuture.completeExceptionally(new Exception("Could not upload image to storage bucket")))
                .addOnSuccessListener(taskSnapshot ->
                        uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                            if (!task.isSuccessful()) {
                                completableFuture.completeExceptionally(task.getException());
                            }

                            return storageRef.getDownloadUrl();
                        }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                            if (task.isSuccessful()) {
                                String downloadUri = task.getResult().toString();
                                this.addProofPhotoUrl(downloadUri, now);
                                completableFuture.complete(downloadUri);
                            } else {
                                completableFuture.completeExceptionally(new Exception("Could not upload image to storage bucket"));
                            }
                        }));


        return completableFuture;
    }

    private void addProofPhotoUrl(String url, Date date) {
        ContestService.getInstance().addProofPhoto(new Proof(this.getContestId(), url, date));
    }
}
