package com.example.sportracker.TypeConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

public class GeneralTypeConverters {
    private final Gson gson = new Gson();

    @TypeConverter
    public List<String> stringToList(String value) {
        return this.gson.fromJson(value, new TypeToken<List<String>>() {
        }.getType());
    }

    @TypeConverter
    public String listToString(List<String> list) {
        return this.gson.toJson(list);
    }

    @TypeConverter
    public Date longToDate(long value) {
        return new Date(value);
    }

    @TypeConverter
    public long dateToLong(Date date) {
        return date.getTime();
    }
}
