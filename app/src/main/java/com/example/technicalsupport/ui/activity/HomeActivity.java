package com.example.technicalsupport.ui.activity;

import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.technicalsupport.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
ActivityHomeBinding binding;
    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore =FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
            }
        });
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });
        Log.d("TAG", "onCreate: "+currentUser.getEmail());
        fireStore.collection(COLLECTION_USER).get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
               String employeeName=documentSnapshot.getString("employeeName");
               String employeeImage=documentSnapshot.getString("employeeImage");
               String employeeEmail=documentSnapshot.getString("employeeEmail");
                    Log.d("TAG", "onSuccess: "+employeeEmail);
               if (currentUser.getEmail().equals(employeeEmail)){
                   Picasso.get().load(employeeImage)
                           .into(binding.profileImg);
                   binding.nameTV.setText("اهلا بك"+ " " +employeeName);
               }

                }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+"no data");
            }
        });


binding.myRequestLout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(HomeActivity.this, MyRequestsActivity.class));
    }
});
        binding.sendRequestLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SendRequestActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}