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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class EnterContestPlayersFragment extends Fragment {
    public static final int MINIMUM_CONTEST_USER_AMOUNT = 2;
    private EnterContestPlayersViewModel viewModel;
    private final UserListAdapter userListAdapter = new UserListAdapter(this::onUserRemoved);
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel =
                new ViewModelProvider(this).get(EnterContestPlayersViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_enter_contest_players, container, false);

        this.setupUserList();
        this.listenToUserActions();

        return root;
    }

    private void setupUserList() {
        final RecyclerView recyclerView = this.root.findViewById(R.id.userRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(userListAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void listenToUserActions() {
        this.viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            this.userListAdapter.setUsers(users);
            this.determineProceedButtonVisibility(users.size());
        });

        EditTextWithDrawable editTextTextUserEmail = this.root.findViewById(R.id.editTextTextUserEmail);
        editTextTextUserEmail.setDrawableClickListener(target -> {
            if (target == DrawableClickListener.DrawablePosition.RIGHT) {
                this.onAddUser(editTextTextUserEmail.getText().toString(), editTextTextUserEmail);
            }
        });
    }

    private void determineProceedButtonVisibility(int userAmount) {
        final FloatingActionButton fab = this.root.findViewById(R.id.proceedToContestFab);
        fab.setVisibility(userAmount >= MINIMUM_CONTEST_USER_AMOUNT ? View.VISIBLE : View.INVISIBLE);
    }

    private void onAddUser(String email, EditText editText) {
        editText.setText("");
        this.viewModel.addUser(email).whenComplete((user, exception) -> {
            if (exception != null) {
                Snackbar.make(this.root, exception.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void onUserRemoved(View view) {
        String email = ((TextView) ((View) view.getParent()).findViewById(R.id.userEmail)).getText().toString();
        if (!this.viewModel.isCurrentUserEmail(email)) {
            this.viewModel.removeUserByEmail(email);
        }
    }
}