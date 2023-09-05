package com.example.technicalsupport.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technicalsupport.databinding.CustomDoneItemBinding;
import com.example.technicalsupport.model.interfaceClass.OnClickItemDetailsRequest;
import com.example.technicalsupport.model.javaClasses.Request;

import java.util.ArrayList;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.DoneHolder> {
    ArrayList<Request>requestArrayList;

    public DoneAdapter(ArrayList<Request> requestArrayList) {
        this.requestArrayList = requestArrayList;
    }

    @NonNull
    @Override
    public DoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomDoneItemBinding binding=CustomDoneItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DoneHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneHolder holder, int position) {
        int poss =position;
        Request request=requestArrayList.get(poss);
        holder.tittle.setText(request.getTittleRequest());


    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class DoneHolder extends RecyclerView.ViewHolder {
        TextView tittle;
       // Button detailsDevice;
        public DoneHolder(CustomDoneItemBinding binding) {
            super(binding.getRoot());
            tittle=binding.titleTV;
           // detailsDevice=binding.deatilsDeviceBtn;
        }
    }
}
