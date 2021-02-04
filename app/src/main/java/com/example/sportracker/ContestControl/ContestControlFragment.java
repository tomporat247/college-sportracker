package com.example.sportracker.ContestControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportracker.R;

import java.util.Arrays;

public class ContestControlFragment extends Fragment {
    private ContestControlViewModel viewModel;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel =
                new ViewModelProvider(this).get(ContestControlViewModel.class);
        this.viewModel.setUsers(Arrays.asList(ContestControlFragmentArgs.fromBundle(getArguments()).getUsers()));
        this.root = inflater.inflate(R.layout.fragment_contest_control, container, false);

        return root;
    }
}