package com.example.sportracker.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.R;
import com.example.sportracker.Utils.RecyclerViewUtils;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {
    private View root;
    private HomeViewModel homeViewModel;
    private final ContestListAdapter contestListAdapter = new ContestListAdapter(this::onContestClick);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_home, container, false);

        this.setupContestList(this.root);
        this.listenToUserActions(this.root);

        return this.root;
    }

    private void setupContestList(View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.contests_recycler_view);
        RecyclerViewUtils.setupRecyclerView(recyclerView, requireContext(), this.contestListAdapter);
    }

    private void listenToUserActions(View root) {
        this.homeViewModel.getContests().observe(getViewLifecycleOwner(), contestListAdapter::setContests);

        root.findViewById(R.id.addContestFab).setOnClickListener(view ->
                Navigation.findNavController(this.root).navigate(R.id.action_nav_home_to_enterContestPlayers));
    }

    private void onContestClick(View contestView) {
        // TODO: Show loader until contest is loaded (or fails)
        String contestId = (String) contestView.getTag();
        this.homeViewModel.selectContest(contestId).whenComplete((x, exception) -> {
            if (exception != null) {
                Snackbar.make(this.root, "Failed to load contest", Snackbar.LENGTH_LONG).show();
            } else {
                Navigation.findNavController(this.root).navigate(HomeFragmentDirections.actionNavHomeToContestControl(null));
            }
        });
    }
}