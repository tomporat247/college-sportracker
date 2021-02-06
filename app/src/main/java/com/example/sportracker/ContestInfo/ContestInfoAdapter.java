package com.example.sportracker.ContestInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sportracker.ContestInfo.Matches.ContestInfoMatchesFragment;
import com.example.sportracker.ContestInfo.Proof.ContestInfoProofFragment;
import com.example.sportracker.ContestInfo.Users.ContestInfoUsersFragment;

import java.util.HashMap;

public class ContestInfoAdapter extends FragmentStateAdapter {
    private final HashMap<Integer, Fragment> positionToFragment;

    public ContestInfoAdapter(Fragment fragment) {
        super(fragment);
        this.positionToFragment = new HashMap<>();
        this.positionToFragment.put(0, new ContestInfoUsersFragment());
        this.positionToFragment.put(1, new ContestInfoMatchesFragment());
        this.positionToFragment.put(2, new ContestInfoProofFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.positionToFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return this.positionToFragment.size();
    }
}
