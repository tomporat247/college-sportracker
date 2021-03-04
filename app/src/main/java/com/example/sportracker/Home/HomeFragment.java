package com.example.sportracker.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.R;
import com.example.sportracker.Utils.DrawerLocker;
import com.example.sportracker.Utils.RecyclerViewUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private View root;
    private HomeViewModel homeViewModel;
    private final ContestListAdapter contestListAdapter = new ContestListAdapter(this::onContestClick, this::onContestDeleteClick);
    private HashMap<String, Contest> idToContest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_home, container, false);

        this.setupContestList(this.root);
        this.listenToUserActions(this.root);

        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        return this.root;
    }

    private void setupContestList(View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.contests_recycler_view);
        RecyclerViewUtils.setupRecyclerView(recyclerView, requireContext(), this.contestListAdapter);
        this.homeViewModel.getContests().observe(getViewLifecycleOwner(), contests -> {
            idToContest = (HashMap<String, Contest>) contests.stream().collect(Collectors.toMap(Contest::getId, contest -> contest));
            contestListAdapter.setContests(contests);
        });
    }

    private void listenToUserActions(View root) {
        root.findViewById(R.id.addContestFab).setOnClickListener(view ->
                Navigation.findNavController(this.root).navigate(R.id.action_nav_home_to_enterContestPlayers));
    }

    private void onContestClick(View contestView) {
        this.setLoadingBarVisibility(true);
        String contestId = (String) contestView.getTag();
        this.homeViewModel.selectContest(idToContest.get(contestId)).whenComplete((x, exception) -> {
            this.setLoadingBarVisibility(false);
            if (exception != null) {
                Snackbar.make(this.root, "Failed to load contest", Snackbar.LENGTH_LONG).show();
            } else {
                Navigation.findNavController(this.root).navigate(HomeFragmentDirections.actionNavHomeToContestControl(null));
            }
        });
    }

    private void onContestDeleteClick(View contestView) {
        String contestId = (String) contestView.getTag();
        String contestName = this.idToContest.get(contestId).getName();

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Contest deletion")
                .setMessage(String.format(Locale.US, "Are you sure you want to delete contest \"%s\"?", contestName))
                .setPositiveButton("Delete", (dialog, which) -> {
                    setLoadingBarVisibility(true);
                    homeViewModel.deleteContest(contestId).whenComplete((x, exception) -> {
                        setLoadingBarVisibility(false);
                        Snackbar.make(root, exception != null ? "Failed to delete contest" : "Deleted contest successfully", Snackbar.LENGTH_LONG).show();
                    });
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();

        alertDialog.setOnShowListener(arg0 -> {
            Button[] buttons = {
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE),
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            };
            for (Button button : buttons) {
                button.setTextColor(0xFFFFFFFF);
                button.setBackgroundColor(0x00FFFFFF);
            }
        });

        alertDialog.show();
    }

    private void setLoadingBarVisibility(boolean isVisible) {
        this.root.findViewById(R.id.contestLoader).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        this.root.findViewById(R.id.contests_recycler_view).setAlpha((float) (isVisible ? 0.4 : 1.0));
        this.root.findViewById(R.id.addContestFab).setAlpha((float) (isVisible ? 0.4 : 1.0));
    }
}