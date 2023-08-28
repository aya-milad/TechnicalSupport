package com.example.technicalsupport.ui.activity;

import static com.example.technicalsupport.javaClasses.Constant.COLLECTION_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.technicalsupport.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
   private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireStore;
    boolean isTechnicalSupportEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        fireStore =FirebaseFirestore.getInstance();
        if (currentUser!=null){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    private void login() {
        typeEmployee();
        if (currentUser==null){
            String email = binding.emilET.getText().toString();
            String password = binding.passwordET.getText().toString();
            // Use FirebaseAuth to sign in the user with the email and password values
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (isTechnicalSupportEmployee==false){
                                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                                }else {
                                    startActivity(new Intent(LoginActivity.this,HomeSupportActivity.class));

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(),  Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onFailure: "+task.getException().getMessage());

                            }
                        }
                    });

        }

    }
    private void typeEmployee(){
        fireStore.collection(COLLECTION_USER)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            isTechnicalSupportEmployee=documentSnapshot.getBoolean("isTechnicalSupportEmployee");
                        }
                        }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),  Toast.LENGTH_LONG).show();
                        Log.d("TAG", "onFailure: "+e.getMessage());
                    }
                });
    }



}