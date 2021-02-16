package com.example.sportracker.ContestInfo.Proof;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sportracker.R;
import com.example.sportracker.Utils.RecyclerViewUtils;

public class ContestInfoProofsFragment extends Fragment {
    private ContestInfoProofsViewModel viewModel;
    private final ContestInfoProofsListAdapter adapter = new ContestInfoProofsListAdapter();
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_contest_info_proofs, container, false);
        this.viewModel = new ViewModelProvider(this).get(ContestInfoProofsViewModel.class);

        this.setupProofList();

        return root;
    }

    private void setupProofList() {
        RecyclerViewUtils.setupRecyclerView(this.root.findViewById(R.id.proofRecyclerView), requireContext(), adapter);
        this.viewModel.getProofs().observe(getViewLifecycleOwner(), this.adapter::setProofs);
    }
}
