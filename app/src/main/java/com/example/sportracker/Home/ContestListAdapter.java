package com.example.sportracker.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.Contest;
import com.example.sportracker.R;

import java.util.Collections;
import java.util.List;

public class ContestListAdapter extends RecyclerView.Adapter<ContestListAdapter.ContestViewHolder> {
    private List<Contest> contests = Collections.emptyList();
    private final View.OnClickListener onContestClick;
    private final View.OnClickListener onDeleteContestClick;

    public ContestListAdapter(View.OnClickListener onContestClick, View.OnClickListener onDeleteContestClick) {
        this.onContestClick = onContestClick;
        this.onDeleteContestClick = onDeleteContestClick;
    }

    @NonNull
    @Override
    public ContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContestViewHolder holder, int position) {
        final Contest currentContest = this.contests.get(position);
        holder.contestName.setText(currentContest.getName());
        holder.setViewTag(currentContest.getId());
    }

    @Override
    public int getItemCount() {
        return this.contests.size();
    }

    public void setContests(List<Contest> contests) {
        this.contests = contests;
        this.notifyDataSetChanged();
    }

    class ContestViewHolder extends RecyclerView.ViewHolder {
        private final View root;
        private final TextView contestName;
        private final ImageView deleteContestButton;

        public ContestViewHolder(@NonNull View itemView) {
            super(itemView);
            this.root = itemView;
            this.contestName = this.root.findViewById(R.id.contestName);
            this.deleteContestButton = this.root.findViewById(R.id.deleteContest);
            this.root.setOnClickListener(onContestClick);
            this.deleteContestButton.setOnClickListener(onDeleteContestClick);
        }

        public void setViewTag(String tag) {
            this.root.setTag(tag);
            this.deleteContestButton.setTag(tag);
        }
    }
}
