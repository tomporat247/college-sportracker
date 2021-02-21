package com.example.sportracker.Login;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void signInToAccount(GoogleSignInAccount account, Activity activity, OnCompleteListener<AuthResult> listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(activity, listener);
    }

    public void upsertUserFireStoreDocument() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            firestore.collection("users").document(user.getUid()).set(getUserDataMap(), SetOptions.merge());
        }
    }

    private Map<String, Object> getUserDataMap() {
        FirebaseUser user = auth.getCurrentUser();
        Map<String, Object> userMap = new HashMap<>();
        if (user != null) {
            userMap.put("name", user.getDisplayName());
            userMap.put("email", user.getEmail());
            userMap.put("photoUrl", Objects.requireNonNull(user.getPhotoUrl()).toString());
            userMap.put("lastLoginDate", new Date().getTime());
        }

        return userMap;
    }
}
