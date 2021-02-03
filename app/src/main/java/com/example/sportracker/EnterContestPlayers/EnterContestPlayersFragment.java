package com.example.sportracker.EnterContestPlayers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.R;
import com.example.sportracker.Utils.DrawableClickListener;
import com.example.sportracker.Utils.EditTextWithDrawable;
import com.google.android.material.snackbar.Snackbar;

public class EnterContestPlayersFragment extends Fragment {
    private EnterContestPlayersViewModel viewModel;
    private final UserListAdapter userListAdapter = new UserListAdapter(this::onUserRemoved);

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

        EditTextWithDrawable editTextTextUserEmail = root.findViewById(R.id.editTextTextUserEmail);
        editTextTextUserEmail.setDrawableClickListener(target -> {
            if (target == DrawableClickListener.DrawablePosition.RIGHT) {
                this.onAddUser(editTextTextUserEmail.getText().toString(), root, editTextTextUserEmail);
            }
        });
    }

    private void onAddUser(String email, View root, EditText editText) {
        editText.setText("");
        if (email.isEmpty()) {
            Snackbar.make(root, "Insert email", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            this.viewModel.addUser(email).whenComplete((user, exception) -> {
                if (exception != null) {
                    Snackbar.make(root, "No user found for email: \"" + email + '"', Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void onUserRemoved(View view) {
        String email = ((TextView) ((View) view.getParent()).findViewById(R.id.userEmail)).getText().toString();
        if (!this.viewModel.isCurrentUserEmail(email)) {
            this.viewModel.removeUserByEmail(email);
        }
    }
}