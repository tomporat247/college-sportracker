package com.example.sportracker.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.R;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private final ContestListAdapter contestListAdapter = new ContestListAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        this.setupList(root);
        this.listenToContestsChanges();

        return root;
    }

    private void setupList(View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.contests_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(contestListAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void listenToContestsChanges() {
        this.homeViewModel.getContests().observe(getViewLifecycleOwner(), contestListAdapter::setContests);
    }
}