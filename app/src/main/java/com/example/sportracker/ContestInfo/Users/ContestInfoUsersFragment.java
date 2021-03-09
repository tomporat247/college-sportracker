package com.example.sportracker.ContestInfo.Users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sportracker.ContestInfo.ContestInfoFragmentDirections;
import com.example.sportracker.Models.ContestUserDetails;
import com.example.sportracker.Models.User;
import com.example.sportracker.R;
import com.example.sportracker.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Map;

public class ContestInfoUsersFragment extends Fragment {
    private View root;
    private ContestInfoUsersViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_contest_info_users, container, false);
        this.viewModel = new ViewModelProvider(this).get(ContestInfoUsersViewModel.class);

        this.setupUserTable();

        return root;
    }

    private void setupUserTable() {
        TableLayout tableLayout = this.root.findViewById(R.id.userTable);

        this.viewModel.getOrderedUsers().observe(getViewLifecycleOwner(), users -> {
            tableLayout.removeAllViews();
            Map<String, ContestUserDetails> idToUserDetails = this.viewModel.getIdToUserDetails();

            for (User user : users) {
                tableLayout.addView(this.createUserRow(tableLayout, user, idToUserDetails.get(user.getId())));
            }
        });
    }

    private View createUserRow(TableLayout tableLayout, User user, ContestUserDetails userDetails) {
        ViewGroup userTableRow = (ViewGroup) getLayoutInflater().inflate(R.layout.user_in_table_row, tableLayout, false);

        ((TextView) userTableRow.findViewById(R.id.userWins)).setText(String.valueOf(userDetails.getWinAmount()));
        ((TextView) userTableRow.findViewById(R.id.userLoses)).setText(String.valueOf(userDetails.getLossAmount()));
        ((TextView) userTableRow.findViewById(R.id.userWinLossRatio)).setText(String.format(Locale.US, "%.2f", userDetails.getWinLossRatio()));
        ((TextView) userTableRow.findViewById(R.id.userNameInUserTable)).setText(user.getName());
        Picasso.get().load(user.getPhotoUrl()).transform(new CircleTransform()).into((ImageView) userTableRow.findViewById(R.id.userImageInUserTable));

        userTableRow.setOnClickListener(view -> Navigation.findNavController(this.root).navigate(
                ContestInfoFragmentDirections.actionContestInfoToNavProfile().setId(user.getId())));

        return userTableRow;
    }
}
