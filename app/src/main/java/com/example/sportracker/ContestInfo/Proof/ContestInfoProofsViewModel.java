package com.example.sportracker.ContestInfo.Proof;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.Models.Proof;
import com.example.sportracker.Services.ContestService;

import java.util.List;

public class ContestInfoProofsViewModel extends ViewModel {
    LiveData<List<Proof>> getProofs() {
        return Transformations.map(ContestService.getInstance().getContest(), Contest::getProofs);
    }
}
