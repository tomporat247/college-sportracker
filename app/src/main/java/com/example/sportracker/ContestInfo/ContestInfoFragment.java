package com.example.sportracker.ContestInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sportracker.R;
import com.example.sportracker.Utils.DrawerLocker;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.HashMap;


public class ContestInfoFragment extends Fragment {
    private View root;
    private HashMap<Integer, String> tabPositionToText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_contest_info, container, false);
        this.tabPositionToText = new HashMap<>();
        this.tabPositionToText.put(0, "Users");
        this.tabPositionToText.put(1, "Matches");
        this.tabPositionToText.put(2, "Proofs");

        this.setupViewPager();

        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        return root;
    }

    private void setupViewPager() {
        ViewPager2 viewPager = this.root.findViewById(R.id.contestInfoViewPager);
        viewPager.setAdapter(new ContestInfoAdapter(this));
        new TabLayoutMediator(this.root.findViewById(R.id.tabLayout), viewPager,
                (tab, position) -> tab.setText(this.tabPositionToText.get(position))
        ).attach();
    }
}