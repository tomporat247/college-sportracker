package com.example.sportracker.TypeConverters;

import androidx.room.TypeConverter;

import com.example.sportracker.Models.Proof;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ProofTypeConverter {
    private final Gson gson = new Gson();

    @TypeConverter
    public Proof stringToProof(String value) {
        return this.gson.fromJson(value, Proof.class);
    }

    @TypeConverter
    public String proofToString(Proof proof) {
        return this.gson.toJson(proof);
    }

    @TypeConverter
    public List<Proof> stringToProofList(String value) {
        return this.gson.fromJson(value, new TypeToken<List<Proof>>() {
        }.getType());
    }

    @TypeConverter
    public String proofListToString(List<Proof> proofs) {
        return this.gson.toJson(proofs);
    }
}
