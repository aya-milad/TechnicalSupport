package com.example.technicalsupport.adapters;

import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_USER;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technicalsupport.model.interfaceClass.OnClickItemDetailsRequest;
import com.example.technicalsupport.databinding.CustomInprogressItemBinding;
import com.example.technicalsupport.model.javaClasses.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InProgressAdapter extends RecyclerView.Adapter<InProgressAdapter.OnProgressHolder> {
    ArrayList<Request>requestArrayList;
    OnClickItemDetailsRequest onClickItemDetailsRequest;


    public InProgressAdapter(ArrayList<Request> requestArrayList, OnClickItemDetailsRequest onClickItemDetailsRequest) {
        this.requestArrayList = requestArrayList;
        this.onClickItemDetailsRequest = onClickItemDetailsRequest;
    }

    @NonNull
    @Override
    public OnProgressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomInprogressItemBinding binding=CustomInprogressItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OnProgressHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnProgressHolder holder, int position) {
        int poss =position;
        Request request=requestArrayList.get(poss);
        holder.title.setText(request.getTittleRequest());
        holder.timer.setText(request.getTime()+ " "+ "يوم" );
        holder.deatilsDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItemDetailsRequest.showDetails(poss);
            }
        });
        holder.deliveryDeviceBtn.setVisibility(View.GONE);


    }


    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }


    class OnProgressHolder extends RecyclerView.ViewHolder {
        TextView title,timer;
        Button deatilsDeviceBtn,deliveryDeviceBtn;

        public OnProgressHolder(CustomInprogressItemBinding binding) {
            super(binding.getRoot());
            title=binding.titleTV ;
            timer=binding.timer;
            deatilsDeviceBtn=binding.deatilsDeviceBtn;
            deliveryDeviceBtn=binding.deliveryDeviceBtn;


        }
    }
}
