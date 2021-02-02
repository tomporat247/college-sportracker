package com.example.sportracker.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Contest>> contests;

    public HomeViewModel() {
        this.contests = new MutableLiveData<>();
        final ArrayList<User> tmpUsers = new ArrayList<>();
        final ArrayList<Contest> tmpContests = new ArrayList<>();
        tmpUsers.add(new User("1", "abc@gmail.com", "abc", "photo"));
        tmpContests.add(new Contest("ContestA", tmpUsers));
        tmpContests.add(new Contest("ContestB", tmpUsers));
        tmpContests.add(new Contest("Lilach lilch lilchush", tmpUsers));
        this.contests.setValue(tmpContests);
    }

    public LiveData<List<Contest>> getContests() {
        return this.contests;
    }
}