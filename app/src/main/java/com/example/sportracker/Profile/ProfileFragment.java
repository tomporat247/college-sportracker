package com.example.sportracker.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportracker.R;
import com.example.sportracker.Utils.DrawerLocker;

// TODO: Add a profile page
public class ProfileFragment extends Fragment {
    private View root;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        this.root = inflater.inflate(R.layout.fragment_profile, container, false);

        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        return this.root;
    }
}