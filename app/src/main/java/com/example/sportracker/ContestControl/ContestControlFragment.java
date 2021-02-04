package com.example.sportracker.ContestControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.Team;
import com.example.sportracker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ContestControlFragment extends Fragment {
    private ContestControlViewModel viewModel;
    private View root;
    private HashMap<Team, UserTeamListAdapter> teamToAdapter;
    private HashMap<Team, Integer> teamToViewId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel =
                new ViewModelProvider(this).get(ContestControlViewModel.class);
        this.viewModel.setUsers(new ArrayList<>(Arrays.asList(ContestControlFragmentArgs.fromBundle(getArguments()).getUsers())));
        this.root = inflater.inflate(R.layout.fragment_contest_control, container, false);

        this.initializeMaps();
        this.setupTeamLists();

        return root;
    }

    private void initializeMaps() {
        this.teamToAdapter = new HashMap<>();
        this.teamToAdapter.put(Team.OUT, new UserTeamListAdapter());
        this.teamToAdapter.put(Team.A, new UserTeamListAdapter());
        this.teamToAdapter.put(Team.B, new UserTeamListAdapter());
        this.teamToViewId = new HashMap<>();
        this.teamToViewId.put(Team.OUT, R.id.teamOutRecyclerView);
        this.teamToViewId.put(Team.A, R.id.teamARecyclerView);
        this.teamToViewId.put(Team.B, R.id.teamBRecyclerView);
    }

    private void setupTeamLists() {
        Arrays.stream(Team.values()).forEach(this::setupTeamList);

        this.viewModel.getTeamToUsers().observe(getViewLifecycleOwner(), teamToUsers ->
                teamToUsers.keySet().forEach(team -> this.teamToAdapter.get(team).setUsers(teamToUsers.get(team))));
    }

    private void setupTeamList(Team team) {
        final RecyclerView recyclerView = this.root.findViewById(this.teamToViewId.get(team));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(this.teamToAdapter.get(team));
        recyclerView.setLayoutManager(layoutManager);
    }
}