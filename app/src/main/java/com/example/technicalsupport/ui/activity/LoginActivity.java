package com.example.technicalsupport.ui.activity;

import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.technicalsupport.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireStore;
//    private String checkbox;
private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("remembered", false)) {
            binding.emilET.setText(sharedPreferences.getString("email", ""));
            binding.passwordET.setText(sharedPreferences.getString("password", ""));
            binding.checkBox.setChecked(false);
        }

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.checkBox.isChecked()) {
                    // Save user's credentials
                    sharedPreferences.edit()
                            .putBoolean("remembered", true)
                            .putString("email", binding.emilET.getText().toString())
                            .putString("password", binding.passwordET.getText().toString())
                            .apply();
                } else {
                    // Clear saved credentials
                    sharedPreferences.edit()
                            .putBoolean("remembered", false)
                            .remove("email")
                            .remove("password")
                            .apply();
                }
                firebaseAuth.signInWithEmailAndPassword( binding.emilET.getText().toString(),binding.passwordET.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                typeEmployee();
                            } else {
                           Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
        private void typeEmployee() {
        fireStore.collection(COLLECTION_USER)
                .whereEqualTo("employeeEmail", binding.emilET.getText().toString())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String isTechnicalSupportEmployee = documentSnapshot.getString("isTechnicalSupportEmployee");
                            Log.d("TAG", "onSuccess: " + isTechnicalSupportEmployee);
                            if (isTechnicalSupportEmployee.equals("false")) {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else if (isTechnicalSupportEmployee.equals("true")) {
                                startActivity(new Intent(LoginActivity.this, HomeSupportActivity.class));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("TAG", "onFailure: " + e.getMessage());
                    }
                });
    }
        @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
            Log.d("TAG", "onStart: " + currentUser);
        }
    }
}

