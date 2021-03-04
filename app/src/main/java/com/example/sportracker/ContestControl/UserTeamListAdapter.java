package com.example.sportracker.ContestControl;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.User;
import com.example.sportracker.R;
import com.example.sportracker.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class UserTeamListAdapter extends RecyclerView.Adapter<UserTeamListAdapter.UserInTeamViewHolder> {
    private List<User> users = Collections.emptyList();

    @NonNull
    @Override
    public UserInTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserInTeamViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_in_team_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserInTeamViewHolder holder, int position) {
        final User currentUser = this.users.get(position);
        holder.userName.setText(currentUser.getName());
        Picasso.get().load(currentUser.getPhotoUrl()).transform(new CircleTransform()).into(holder.userImage);
        holder.setTag(currentUser.getId());
        holder.allowDrag();
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    class UserInTeamViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final ImageView userImage;
        private final View view;

        public UserInTeamViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userInTeamName);
            this.userImage = itemView.findViewById(R.id.userInTeamPhoto);
            this.view = itemView;
        }

        private void setTag(String tag) {
            this.view.setTag(tag);
        }

        private void allowDrag() {
            this.view.setOnLongClickListener(v -> {
                String tag = v.getTag().toString();
                ClipData.Item item = new ClipData.Item(tag);
                ClipData data = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(data, dragShadowBuilder, v, 0);
                return true;
            });
        }
    }
}
