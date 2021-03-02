package com.example.sportracker.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.sportracker.DAO.ContestDao;
import com.example.sportracker.DAO.MatchesDao;
import com.example.sportracker.Models.BasicContest;
import com.example.sportracker.Models.Match;
import com.example.sportracker.TypeConverters.GeneralTypeConverters;
import com.example.sportracker.TypeConverters.ProofTypeConverter;
import com.example.sportracker.TypeConverters.UserTypeConverter;

import java.util.concurrent.Executors;

@Database(entities = {BasicContest.class, Match.class}, version = 1, exportSchema = false)
@TypeConverters({GeneralTypeConverters.class, UserTypeConverter.class, ProofTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract ContestDao contestDao();
    public abstract MatchesDao matchesDao();

    public static void setupDatabase(Context context) {
        instance = Room.databaseBuilder(context, AppDatabase.class, "sportracker").build();
        Executors.newSingleThreadExecutor().execute(instance::clearAllTables);
    }

    public static AppDatabase getInstance() {
        return instance;
    }
}
