package com.example.sportracker.EnterContestPlayers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.User;
import com.example.sportracker.R;
import com.example.sportracker.Utils.DrawableClickListener;
import com.example.sportracker.Utils.DrawerLocker;
import com.example.sportracker.Utils.EditTextWithDrawable;
import com.example.sportracker.Utils.Keyboard;
import com.example.sportracker.Utils.RecyclerViewUtils;
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

        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        return root;
    }

    private void setupUserList() {
        final RecyclerView recyclerView = this.root.findViewById(R.id.userRecyclerView);
        RecyclerViewUtils.setupRecyclerView(recyclerView, requireContext(), this.userListAdapter);

        this.viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            this.userListAdapter.setUsers(users);
            this.determineProceedButtonVisibility(users.size());
        });
    }

    private void listenToUserActions() {
        EditTextWithDrawable editTextTextUserEmail = this.root.findViewById(R.id.editTextTextUserEmail);
        editTextTextUserEmail.setDrawableClickListener(target -> {
            if (target == DrawableClickListener.DrawablePosition.RIGHT) {
                this.onAddUser(editTextTextUserEmail.getText().toString(), editTextTextUserEmail);
            }
        });

        this.root.findViewById(R.id.proceedToContestFab).setOnClickListener(v -> navigateToContestControl());
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
        this.viewModel.removeUserByEmail(email);
    }

    private void navigateToContestControl() {
        Keyboard.hideKeyboard(getActivity());
        this.viewModel.createContest();
        Navigation.findNavController(this.root).navigate(
                EnterContestPlayersFragmentDirections.actionEnterContestPlayersToContestControl(
                        this.viewModel.getUsers().getValue().toArray(new User[0])));
    }
}