package com.example.technicalsupport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technicalsupport.databinding.CustomDonItemBinding;
import com.example.technicalsupport.javaClasses.Request;

import java.util.ArrayList;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.DoneHolder> {
    private ArrayList<Request>requestArrayList;

    public DoneAdapter(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }

    @NonNull
    @Override
    public DoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomDonItemBinding binding=CustomDonItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DoneHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneHolder holder, int position) {
int poss =position;
Request request=requestArrayList.get(poss);

    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class DoneHolder extends RecyclerView.ViewHolder {
        public DoneHolder(CustomDonItemBinding binding) {
            super(binding.getRoot());
        }
    }
}
