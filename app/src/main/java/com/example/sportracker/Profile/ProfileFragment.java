package com.example.sportracker.Profile;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportracker.R;
import com.example.sportracker.Utils.CircleTransform;
import com.example.sportracker.Utils.DrawerLocker;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class ProfileFragment extends Fragment {
    private View root;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        this.root = inflater.inflate(R.layout.fragment_profile, container, false);

        this.handleIncomingArguments();
        this.listenToUserStatistics();

        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        return this.root;
    }

    private void handleIncomingArguments() {
        this.profileViewModel.loadUserData(ProfileFragmentArgs.fromBundle(getArguments()).getId());
    }

    private void listenToUserStatistics() {
        this.profileViewModel.getUserStatistics().observe(getViewLifecycleOwner(), userStatistics -> {
            Picasso.get().load(userStatistics.getPhotoUrl().replace("=s96-c", "=s400")).transform(new CircleTransform()).into((ImageView) this.root.findViewById(R.id.profileImage));
            ((TextView) this.root.findViewById(R.id.profileName)).setText(userStatistics.getName());
            ((TextView) this.root.findViewById(R.id.profileEmail)).setText(userStatistics.getEmail());
            ((TextView) this.root.findViewById(R.id.profileTotalWins)).setText(String.format(Locale.US, "%d", userStatistics.getTotalWins()));
            ((TextView) this.root.findViewById(R.id.profileTotalLosses)).setText(String.format(Locale.US, "%d", userStatistics.getTotalLosses()));
            ((TextView) this.root.findViewById(R.id.profileWinLossRatio)).setText(String.format(Locale.US, "%.2f", userStatistics.getWinLossRatio()));
            ((TextView) this.root.findViewById(R.id.profilePlusMinus)).setText(String.format(Locale.US, "%d", userStatistics.getPlusMinus()));
            ((TextView) this.root.findViewById(R.id.profileLastSeenAt)).setText(String.format(Locale.US, "%s %s", "Last seen on: ", DateFormat.format("dd.MM.yyyy HH:mm:ss", userStatistics.getLastLoginDate())));
        });
    }
}