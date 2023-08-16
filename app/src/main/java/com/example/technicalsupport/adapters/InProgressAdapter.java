package com.example.technicalsupport.adapters;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.technicalsupport.databinding.CustomInprogressItemBinding;

public class InProgressAdapter extends RecyclerView.Adapter<InProgressAdapter.OnProgressHolder> {
//private  ArrayList<JobOffer>jobOfferList;
////private FinishedJobListener listener;
//    private CountDownTimer timer;





    @NonNull
    @Override
    public OnProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomInprogressItemBinding binding=CustomInprogressItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OnProgressHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnProgressHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    class OnProgressHolder extends RecyclerView.ViewHolder {
        TextView titleJob,timer;
        Button finishedJobBtn,deatilsJobBtn;

        public OnProgressHolder(CustomInprogressItemBinding binding) {
            super(binding.getRoot());
            titleJob=binding.titleTV ;
        timer=binding.timer;
            deatilsJobBtn=binding.deatilsJobBtn;


        }
    }
}
