package com.example.sportracker.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sportracker.Models.BasicContest;
import com.example.sportracker.Models.Contest;

import java.util.List;

@Dao
public interface ContestDao {
    @Transaction
    @Query("SELECT * FROM contests ORDER BY name DESC")
    LiveData<List<Contest>> getAllContests();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBasicContest(BasicContest contest);

    @Transaction
    @Delete
    void deleteContest(BasicContest contest);

    @Transaction
    @Query("DELETE FROM contests WHERE id = :id")
    void deleteContest(String id);
}
