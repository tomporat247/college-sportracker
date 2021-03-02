package com.example.sportracker.TypeConverters;

import androidx.room.TypeConverter;

import com.example.sportracker.Models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class UserTypeConverter {
    private final Gson gson = new Gson();

    @TypeConverter
    public User stringToUser(String value) {
        return this.gson.fromJson(value, User.class);
    }

    @TypeConverter
    public String userToString(User user) {
        return this.gson.toJson(user);
    }

    @TypeConverter
    public List<User> stringToUserList(String value) {
        return this.gson.fromJson(value, new TypeToken<List<User>>() {
        }.getType());
    }

    @TypeConverter
    public String userListToString(List<User> users) {
        return this.gson.toJson(users);
    }
}
