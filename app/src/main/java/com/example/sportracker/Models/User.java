package com.example.sportracker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {
    private String id;
    private String email;
    private String name;
    private String photoUrl;
    private Date lastLoginDate;
    private ArrayList<String> contestIds;

    public User(String id, String email, String name, String photoUrl, @Nullable ArrayList<String> contestIds) {
        this.setId(id);
        this.setEmail(email);
        this.setName(name);
        this.setPhotoUrl(photoUrl);
        this.setContestIds(contestIds);
    }

    public User(String id, Map<String, Object> firestoreDocument) {
        this.setId(id);
        this.setEmail((String) firestoreDocument.get("email"));
        this.setName((String) firestoreDocument.get("name"));
        this.setPhotoUrl((String) firestoreDocument.get("photoUrl"));
        this.setLastLoginDate(new Date((Long) firestoreDocument.get("lastLoginDate")));
        this.setContestIds(firestoreDocument.containsKey("contestIds") ? new ArrayList<>(Arrays.asList((String[]) firestoreDocument.get("contestIds"))) : null);
    }

    public Map<String, Object> toDoc() {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("id", this.id);
        userMap.put("email", this.email);
        userMap.put("name", this.name);
        userMap.put("photoUrl", this.photoUrl);
        userMap.put("lastLoginDate", this.lastLoginDate.getTime());
        userMap.put("contestIds", contestIds);

        return userMap;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        name = in.readString();
        photoUrl = in.readString();
        contestIds = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(photoUrl);
        dest.writeStringList(contestIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public List<String> getContestIds() {
        return contestIds;
    }

    public void setContestIds(ArrayList<String> contestIds) {
        if (contestIds == null) {
            this.contestIds = new ArrayList<>();
        } else {
            this.contestIds = contestIds;
        }
    }
}
