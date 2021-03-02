package com.example.sportracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sportracker.Models.Match;

import java.util.List;

@Dao
public interface MatchesDao {
    @Transaction
    @Query("SELECT * FROM matches WHERE contestId = (:contestId)")
    LiveData<List<Match>> getContestMatches(String contestId);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMatches(List<Match> matches);

    @Transaction
    @Delete
    void deleteMatches(List<Match> match);
}
