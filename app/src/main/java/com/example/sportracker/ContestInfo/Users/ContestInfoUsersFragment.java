package com.example.sportracker.ContestInfo.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sportracker.R;

public class ContestInfoUsersFragment extends Fragment {
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_contest_info_users, container, false);

        return root;
    }
}
