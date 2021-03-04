package com.example.sportracker.ContestInfo.Proof;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportracker.Models.Proof;
import com.example.sportracker.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class ContestInfoProofsListAdapter extends RecyclerView.Adapter<ContestInfoProofsListAdapter.ProofViewHolder> {
    private List<Proof> proofs = Collections.emptyList();

    @NonNull
    @Override
    public ContestInfoProofsListAdapter.ProofViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContestInfoProofsListAdapter.ProofViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.proof_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContestInfoProofsListAdapter.ProofViewHolder holder, int position) {
        final Proof currentProof = this.proofs.get(position);
        holder.proofDate.setText(DateFormat.format("dd.MM.yyyy HH:mm:ss", currentProof.getDate()));
        Picasso.get().load(currentProof.getPhotoUrl()).into(holder.proofImage);
    }

    @Override
    public int getItemCount() {
        return this.proofs.size();
    }

    public void setProofs(List<Proof> proofs) {
        this.proofs = proofs;
        this.notifyDataSetChanged();
    }

    class ProofViewHolder extends RecyclerView.ViewHolder {
        private final TextView proofDate;
        private final ImageView proofImage;

        public ProofViewHolder(@NonNull View itemView) {
            super(itemView);
            this.proofDate = itemView.findViewById(R.id.proofDate);
            this.proofImage = itemView.findViewById(R.id.proofImage);
        }
    }
}