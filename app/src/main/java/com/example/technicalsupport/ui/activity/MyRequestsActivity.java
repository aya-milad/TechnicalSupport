package com.example.technicalsupport.ui.activity;

import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.technicalsupport.adapters.DoneSupportAdapter;
import com.example.technicalsupport.adapters.MyRequestsAdapter;
import com.example.technicalsupport.databinding.ActivityMyRequestsBinding;
import com.example.technicalsupport.ui.fragment.DoneFragment;
import com.example.technicalsupport.ui.fragment.DoneSupportFragment;
import com.example.technicalsupport.ui.fragment.InPindingFragment;
import com.example.technicalsupport.ui.fragment.InProgressFragment;
import com.example.technicalsupport.ui.fragment.InProgressSupportFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity {
    private ActivityMyRequestsBinding binding;
    private   ArrayList<Fragment> fragments;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireStore;
    String  isTechnicalSupportEmployee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        fireStore =FirebaseFirestore.getInstance();

        fireStore.collection(COLLECTION_USER)
                .whereEqualTo("employeeEmail",currentUser.getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                           String  isTechnicalSupportEmployee=documentSnapshot.getString("isTechnicalSupportEmployee");
                            Log.d("TAG", "onCreate: "+isTechnicalSupportEmployee);
                            ArrayList<String> tb=new ArrayList<>();
                            tb.add("تم الانتهاء");
                            tb.add("جاري الصيانة");
                            tb.add("في الانتظار ");
                            fragments=new ArrayList<>();
                            if (isTechnicalSupportEmployee.equals("true")){
                                fragments.add(new DoneFragment());
                                fragments.add(new InProgressSupportFragment());
                            }
                            else  if (isTechnicalSupportEmployee.equals("false")) {
                                fragments.add(new DoneSupportFragment());
                                fragments.add(new InProgressFragment());
                                fragments.add(new InPindingFragment());
                            }
                            MyRequestsAdapter adapter=new MyRequestsAdapter(MyRequestsActivity.this,fragments);
                            binding.VP.setAdapter(adapter);
                            binding.VP.setCurrentItem(3);

                            new TabLayoutMediator(binding.TL, binding.VP, new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                    tab.setText(tb.get(position));
                                }
                            }).attach();
                        }}
                });


    }
    private void typeEmployee(){

        fireStore.collection(COLLECTION_USER)
                .whereEqualTo("employeeEmail",currentUser.getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                isTechnicalSupportEmployee=documentSnapshot.getString("isTechnicalSupportEmployee");
                            Log.d("TAG", "onSuccess: "+isTechnicalSupportEmployee);
                        }}
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),  Toast.LENGTH_LONG).show();
                        Log.d("TAG", "onFailure: "+e.getMessage());
                    }
                });
    }

}