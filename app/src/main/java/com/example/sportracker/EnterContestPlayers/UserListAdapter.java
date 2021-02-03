package com.example.sportracker.EnterContestPlayers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.User;
import com.example.sportracker.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private List<User> users = Collections.emptyList();

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User currentUser = this.users.get(position);
        holder.userEmail.setText(currentUser.getEmail());
        holder.userName.setText(currentUser.getName());
        Picasso.get().load(currentUser.getPhotoUrl()).into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView userEmail;
        private final ImageView userImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName = itemView.findViewById(R.id.userName);
            this.userEmail = itemView.findViewById(R.id.userEmail);
            this.userImage = itemView.findViewById(R.id.userImage);
        }
    }
}
