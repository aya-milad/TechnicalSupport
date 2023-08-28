package com.example.technicalsupport.ui.activity;

import static com.example.technicalsupport.javaClasses.Constant.COLLECTION_ON_PROGRESS;
import static com.example.technicalsupport.javaClasses.Constant.COLLECTION_ORDER_DATA;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.technicalsupport.databinding.ActivitySendRequestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SendRequestActivity extends AppCompatActivity {
ActivitySendRequestBinding binding;
    private FirebaseFirestore fireStore;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    private String deviceImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySendRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore=FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference().child("Images");

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedData()){
                    sendRequest();

                }

            }
        });
        ActivityResultLauncher<String> ar2=
                registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        binding.imgJobs.setImageURI(result);

                    }
                });
        binding.imgJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  ar1.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                ar2.launch("image/*");


            }
        });

    }
    private void sendRequest(){
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("tittle", binding.tittleET.getText().toString());
        orderData.put("description", binding.descriptionET.getText().toString());
        orderData.put("location", binding.locationET.getText().toString());
        orderData.put("personName", binding.personET.getText().toString());
        orderData.put("deviceImg", deviceImg);
        //dataRequest.put("imgJobs", binding.personET.getText().toString());
        fireStore.collection(COLLECTION_ORDER_DATA)
                .document(currentUser.getUid())
                .collection(COLLECTION_ON_PROGRESS)
                .add(orderData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SendRequestActivity.this, "تم ارسال طلبك بنجاح", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: "+e.getMessage());
                    }
                });
    }
    private void loadDeviceImg(){
        storage.getReference().child("deviceImages")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        deviceImg = uri.toString();
                        Log.d("TAG", "onSuccess: " + deviceImg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SendRequestActivity.this, exception.getMessage(), Toast .LENGTH_SHORT).show();                    }
                });

    }
    boolean checkedData() {
        loadDeviceImg();
     String tittle  =binding.tittleET.getText().toString();
     String description  =binding.descriptionET.getText().toString();
     String location  =binding.locationET.getText().toString();
     String  person =binding.personET.getText().toString();

        if (tittle.isEmpty()) {
            binding.tittleTextInputLayout.setError("هذا الحقل فارغ");

        } else if (description.isEmpty()) {
            binding.decorationTextInputLayout.setError("هذا الحقل فارغ");
        } else if (location.isEmpty()) {
            binding.locationTextInputLayout.setError("هذا الحقل فارغ");
        }

        else if (person.isEmpty()) {
            binding.personTextInputLayout.setError("هذا الحقل فارغ");
        } else if (binding.imgJobs.getDrawable()==null){
            Toast.makeText(this, "يجب ارفاق مرفقات", Toast.LENGTH_SHORT).show();


        } else{
            return true;
        }
        return false;
    }
//    private void uploadImage() {
//        storageRef = storageRef.child(System.currentTimeMillis() + ".jpg");
//        if (deviceImg != null) {
//            storageRef.putFile()
//                    .continueWithTask(task -> {
//                        if (!task.isSuccessful()) {
//                            Toast  .makeText(getActivity(), task.getException().getMessage(),  Toast.LENGTH_SHORT).show();
//                            throw task.getException();
//
//                        }
//                        return storageRef.getDownloadUrl();
//                    })
//                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(Task<Uri> task) {
//                            if (task.isSuccessful()) {
//
//                                // Save the download URL to Firestore
//                                Map<String, Object> data = new HashMap<>();
//                                data.put("imageUrl", task.getResult().toString());
//
//                                db.collection(COLLECTION_USER)
//                                        .document(currentUser.getUid())
//                                        .collection(COLLECTION_BUSINESS_GALLERY)
//
//                                        .add(data)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//
//                                                Toast .makeText(getActivity(), "Uploaded Successfully",  Toast.LENGTH_SHORT).show();}
//                                        })
//                                        .addOnFailureListener(e -> {
//                                            Toast .makeText(getActivity(), "Error uploading image or saving URL",  Toast.LENGTH_SHORT).show();
//                                        });
//                            } else {
//                                Toast  .makeText(getActivity(), "Error uploading image or getting download URL", Toast .LENGTH_SHORT).show();
//                                Log.d("TAG", "onComplete: " + task.getException().getMessage());
//                                //binding.progressBar.setVisibility(View.GONE);
//
//
//                            }
//                        }
//                    });
//        }
//    }


}