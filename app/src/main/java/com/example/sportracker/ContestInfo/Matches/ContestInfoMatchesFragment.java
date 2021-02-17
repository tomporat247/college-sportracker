package com.example.sportracker.ContestInfo.Matches;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportracker.Models.Match;
import com.example.sportracker.Models.User;
import com.example.sportracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ContestInfoMatchesFragment extends Fragment {
    private View root;
    private ContestInfoMatchesViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_contest_info_matches, container, false);
        this.viewModel = new ViewModelProvider(this).get(ContestInfoMatchesViewModel.class);

        this.setupMatchTable();

        return root;
    }

    private void setupMatchTable() {
        TableLayout tableLayout = this.root.findViewById(R.id.matchTable);
        TextView matchCountTextView = this.root.findViewById(R.id.matchCount);
        this.viewModel.getMatches().observe(getViewLifecycleOwner(), matches -> {
            tableLayout.removeAllViews();
            matchCountTextView.setText("Match count: " + matches.size());

            Map<String, List<Match>> dateToMatches = getDateToMatches(matches);

            for (Map.Entry<String, List<Match>> entry : dateToMatches.entrySet()) {
                tableLayout.addView(this.createMatchDateDivider(tableLayout, entry.getKey()));
                for (Match match : entry.getValue()) {
                    tableLayout.addView(this.createMatchTableRow(tableLayout, match));
                }
            }
        });

    }

    private SortedMap<String, List<Match>> getDateToMatches(List<Match> matches) {
        SortedMap<String, List<Match>> dateToMatches = new TreeMap<>();

        for (Match match : matches) {
            String matchDate = DateFormat.format("dd.MM.yyyy", match.getDate()).toString();
            if (dateToMatches.get(matchDate) == null) {
                dateToMatches.put(matchDate, new ArrayList<>());
            }
            dateToMatches.get(matchDate).add(match);
        }

        return dateToMatches;
    }

    private View createMatchDateDivider(ViewGroup root, String date) {
        View matchDateDivider = getLayoutInflater().inflate(R.layout.match_date_divider, root, false);
        ((TextView) matchDateDivider.findViewById(R.id.matchDate)).setText(date);
        return matchDateDivider;
    }

    private View createMatchTableRow(ViewGroup root, Match match) {
        ViewGroup matchTableRow = (ViewGroup) getLayoutInflater().inflate(R.layout.match_table_row, root, false);

        Map<LinearLayout, List<User>> layoutToUsers = new HashMap<>();
        layoutToUsers.put(matchTableRow.findViewById(R.id.winners), this.viewModel.getUsersByIds(match.getWinningTeamUserIds()));
        layoutToUsers.put(matchTableRow.findViewById(R.id.losers), this.viewModel.getUsersByIds(match.getLosingTeamUserIds()));

        for (Map.Entry<LinearLayout, List<User>> entry : layoutToUsers.entrySet()) {
            for (User user : entry.getValue()) {
                entry.getKey().addView(this.createUserInMatchRow(user, entry.getKey(), match.getId()));
            }
        }

        return matchTableRow;
    }

    private View createUserInMatchRow(User user, ViewGroup container, String matchId) {
        // TODO: Listen to remove
        View userInMatchRow = getLayoutInflater().inflate(R.layout.user_in_match_row, container, false);
        Picasso.get().load(user.getPhotoUrl()).into((ImageView) userInMatchRow.findViewById(R.id.userInMatchImage));
        ((TextView) userInMatchRow.findViewById(R.id.userInMatchName)).setText(user.getName());
        return userInMatchRow;
    }
}
