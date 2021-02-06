package com.example.sportracker.ContestInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.sportracker.R;

import java.util.HashMap;

public class ContestInfoAdapter extends FragmentStateAdapter {
    private final HashMap<Integer, Integer> positionToFragmentResourceId;

    public ContestInfoAdapter(Fragment fragment) {
        super(fragment);
        this.positionToFragmentResourceId = new HashMap<>();
        this.positionToFragmentResourceId.put(0, R.layout.fragment_enter_contest_players);
        this.positionToFragmentResourceId.put(1, R.layout.fragment_enter_contest_players);
        this.positionToFragmentResourceId.put(2, R.layout.fragment_enter_contest_players);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new ContestInfoInnerFragment();
        Bundle args = new Bundle();
        args.putInt(ContestInfoInnerFragment.layoutResourceId, this.positionToFragmentResourceId.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return this.positionToFragmentResourceId.size();
    }
}
