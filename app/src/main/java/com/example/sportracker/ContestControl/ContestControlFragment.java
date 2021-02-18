package com.example.sportracker.ContestControl;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.Team;
import com.example.sportracker.R;
import com.example.sportracker.Utils.RecyclerViewUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ContestControlFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private ContestControlViewModel viewModel;
    private View root;
    private HashMap<Team, UserTeamListAdapter> teamToAdapter;
    private HashMap<Team, Integer> teamToViewId;
    private HashMap<Integer, Team> viewIdToTeam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel =
                new ViewModelProvider(this).get(ContestControlViewModel.class);
        this.viewModel.setUsers(new ArrayList<>(Arrays.asList(ContestControlFragmentArgs.fromBundle(getArguments()).getUsers())));
        this.root = inflater.inflate(R.layout.fragment_contest_control, container, false);

        this.initializeMaps();
        this.setupTeamLists();
        this.listenToActionClicks();

        return root;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.teamAOption:
                this.viewModel.addMatch(Team.A);
                Snackbar.make(this.root, "Match Added", 500).show();
                return true;
            case R.id.teamBOption:
                this.viewModel.addMatch(Team.B);
                Snackbar.make(this.root, "Match Added", 500).show();
                return true;
            default:
                return false;
        }
    }

    private void initializeMaps() {
        this.teamToAdapter = new HashMap<>();
        this.teamToAdapter.put(Team.OUT, new UserTeamListAdapter());
        this.teamToAdapter.put(Team.A, new UserTeamListAdapter());
        this.teamToAdapter.put(Team.B, new UserTeamListAdapter());
        this.teamToViewId = new HashMap<>();
        this.viewIdToTeam = new HashMap<>();
        this.teamToViewId.put(Team.OUT, R.id.teamOutRecyclerView);
        this.teamToViewId.put(Team.A, R.id.teamARecyclerView);
        this.teamToViewId.put(Team.B, R.id.teamBRecyclerView);
        this.viewIdToTeam.put(R.id.teamOutRecyclerView, Team.OUT);
        this.viewIdToTeam.put(R.id.teamARecyclerView, Team.A);
        this.viewIdToTeam.put(R.id.teamBRecyclerView, Team.B);
    }

    private void setupTeamLists() {
        Arrays.stream(Team.values()).forEach(this::setupTeamList);

        this.viewModel.getTeamToUsers().observe(getViewLifecycleOwner(), teamToUsers -> {
            teamToUsers.keySet().forEach(team -> this.teamToAdapter.get(team).setUsers(teamToUsers.get(team)));
            this.root.findViewById(R.id.newMatchButton).setEnabled(teamToUsers.get(Team.A).size() > 0 && teamToUsers.get(Team.B).size() > 0);
        });
    }

    private void setupTeamList(Team team) {
        final RecyclerView recyclerView = this.root.findViewById(this.teamToViewId.get(team));
        RecyclerViewUtils.setupRecyclerView(recyclerView, requireContext(), this.teamToAdapter.get(team));
        this.listenToUserDragDrop((View) recyclerView.getParent());
    }

    private void listenToActionClicks() {
        this.root.findViewById(R.id.newMatchButton).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), v);
            popup.setOnMenuItemClickListener(ContestControlFragment.this);
            popup.inflate(R.menu.popup_menu_pick_team);
            popup.show();
        });

        this.root.findViewById(R.id.moreInfoButton).setOnClickListener(v ->
                Navigation.findNavController(this.root).navigate(
                        ContestControlFragmentDirections.actionContestControlToContestInfo()));
    }

    private void listenToUserDragDrop(View target) {
        target.setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                case DragEvent.ACTION_DRAG_ENTERED:
                case DragEvent.ACTION_DRAG_EXITED:
                case DragEvent.ACTION_DRAG_ENDED: {
                    view.invalidate();
                    return true;
                }
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DROP: {
                    view.invalidate();
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String userId = item.getText().toString();
                    Team team = viewIdToTeam.get(((ViewGroup) view).getChildAt(1).getId());
                    viewModel.swapUserTeam(userId, team);

                    return true;
                }
                default:
                    return false;
            }
        });
    }
}