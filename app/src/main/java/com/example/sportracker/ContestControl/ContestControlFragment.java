package com.example.sportracker.ContestControl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Dialogs.EnterContestName.EnterContestNameDialog;
import com.example.sportracker.Models.Team;
import com.example.sportracker.Models.User;
import com.example.sportracker.R;
import com.example.sportracker.Utils.DrawerLocker;
import com.example.sportracker.Utils.RecyclerViewUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ContestControlFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private ContestControlViewModel viewModel;
    private View root;
    private HashMap<Team, UserTeamListAdapter> teamToAdapter;
    private HashMap<Team, Integer> teamToViewId;
    private HashMap<Integer, Team> viewIdToTeam;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel =
                new ViewModelProvider(this).get(ContestControlViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_contest_control, container, false);

        this.handleIncomingArguments();
        this.initializeMaps();
        this.setupTeamLists();
        this.listenToActionClicks();

        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        return root;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.teamAOption:
                this.viewModel.addMatch(Team.A);
                Toast.makeText(getContext(), "Match Added", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.teamBOption:
                this.viewModel.addMatch(Team.B);
                Toast.makeText(getContext(), "Match Added", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            this.setLoadingBarVisibility(true);
            this.viewModel.uploadPhotoToStorageBucket(photo).whenComplete((uri, storageException) -> {
                if (storageException != null) {
                    Snackbar.make(this.root, storageException.getMessage(), Snackbar.LENGTH_LONG).show();
                } else {
                    this.viewModel.saveContest().whenComplete((a, firestoreException) -> {
                        if (firestoreException != null) {
                            Snackbar.make(this.root, "An error occurred during contest save", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(this.root, "Contest saved", Snackbar.LENGTH_LONG).show();
                        }
                        this.setLoadingBarVisibility(false);
                    });
                }
            });
        }
    }

    private void handleIncomingArguments() {
        ContestControlFragmentArgs args = ContestControlFragmentArgs.fromBundle(getArguments());
        User[] users = args.getUsers();
        this.viewModel.setUsers(users != null ? new ArrayList<>(Arrays.asList(users)) : null);
    }

    private void initializeMaps() {
        this.teamToAdapter = new HashMap<>();
        this.teamToAdapter.put(Team.OUT, new UserTeamListAdapter(this::onUserClick));
        this.teamToAdapter.put(Team.A, new UserTeamListAdapter(this::onUserClick));
        this.teamToAdapter.put(Team.B, new UserTeamListAdapter(this::onUserClick));
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

        this.root.findViewById(R.id.saveButton).setOnClickListener(v -> this.onSave());

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

    private void onSave() {
        if (this.viewModel.isContestNew()) {
            this.requestContestName();
        } else {
            this.dispatchTakePictureIntent();
        }
    }

    private void requestContestName() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        EnterContestNameDialog dialog = new EnterContestNameDialog(completableFuture);

        completableFuture.whenComplete((contestName, exception) -> {
            if (contestName != null) {
                this.viewModel.setContestName(contestName);
                this.onSave();
            }
        });
        dialog.show(getChildFragmentManager(), "EnterContestDialog");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void setLoadingBarVisibility(boolean isVisible) {
        this.root.findViewById(R.id.savingLoader).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        this.root.findViewById(R.id.contestInfoContainer).setAlpha((float) (isVisible ? 0.4 : 1.0));
    }

    private void onUserClick(View userView) {
        String userId = (String) userView.getTag();
        Navigation.findNavController(this.root).navigate(
                ContestControlFragmentDirections.actionContestControlToNavProfile().setId(userId));
    }
}