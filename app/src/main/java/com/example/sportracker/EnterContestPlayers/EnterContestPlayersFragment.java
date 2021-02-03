package com.example.sportracker.EnterContestPlayers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.R;

public class EnterContestPlayersFragment extends Fragment {
    private EnterContestPlayersViewModel viewModel;
    private final UserListAdapter userListAdapter = new UserListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel =
                new ViewModelProvider(this).get(EnterContestPlayersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_enter_contest_players, container, false);

        this.setupUserList(root);
        this.listenToUserActions(root);

        return root;
    }

    private void setupUserList(View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.userRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(userListAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void listenToUserActions(View root) {
        this.viewModel.getUsers().observe(getViewLifecycleOwner(), userListAdapter::setUsers);
    }
}