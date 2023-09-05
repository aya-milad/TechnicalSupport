package com.example.technicalsupport.ui.activity;

import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_ON_PENDING;
import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_ON_PROGRESS;
import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_ORDER_DATA;
import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_USER;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.technicalsupport.R;
import com.example.technicalsupport.databinding.ActivitySendRequestBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SendRequestActivity extends AppCompatActivity {
    private static final int READ_CONTACT_REQUEST_CODE = 101;
    ActivitySendRequestBinding binding;
    private FirebaseFirestore fireStore;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageRef;
    private Uri deviceImg;
    private  ActivityResultLauncher<String> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("deviceImg");


        ActivityResultLauncher<String> ar1 = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {

                    }
                }
        );
       launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            deviceImg = result;
                            binding.deviceImg.setVisibility(View.GONE);
                            binding.deviceImg2.setVisibility(View.VISIBLE);
                            binding.deviceImg2.setImageURI(result);

                            Log.d("TAG", "onActivityResult: " + result);


                        }
                    }
                });

        binding.deviceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ar1.launch(Manifest.permission.READ_MEDIA_IMAGES);
                checkAccessToGalleryPermission();




            }
        });
        binding.deviceImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ar1.launch(Manifest.permission.READ_MEDIA_IMAGES);
                checkAccessToGalleryPermission();




            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (checkedData()) {

                sendRequest();

            }

            }
        });




    }

    boolean checkedData() {
        // loadDeviceImg();
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
        } else if (binding.deviceImg2.getDrawable() == null ){
            Toast.makeText(this, "يجب ارفاق مرفقات", Toast.LENGTH_SHORT).show();


        } else{
            return true;
        }
        return false;

    }

    private void checkAccessToGalleryPermission(){
        if(ContextCompat.checkSelfPermission(SendRequestActivity.this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SendRequestActivity.this,new String[]{Manifest.permission.READ_MEDIA_IMAGES}
                    ,READ_CONTACT_REQUEST_CODE);

        } else {
            launcher.launch("image/*");



        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACT_REQUEST_CODE && grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {

//sendRequest();
        }else
            Toast.makeText(this, "تم رفض إذن الوصول للصور، يرجى السماح بالإذن لإجراء مكالمة", Toast.LENGTH_LONG).show();
    }

private void sendRequest(){
    storageRef = storageRef.child(System.currentTimeMillis() + ".jpg");
    if (deviceImg != null) {
        storageRef.putFile(deviceImg)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        Toast  .makeText(SendRequestActivity.this, task.getException().getMessage(),  Toast.LENGTH_SHORT).show();
                        throw task.getException();

                    }
                    return storageRef.getDownloadUrl();
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        if (task.isSuccessful()) {
                            fireStore.collection(COLLECTION_USER)
                                    .document(currentUser.getUid()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            binding.progressBar.setVisibility(View.VISIBLE);

                                            long   employeeNumber = documentSnapshot.getLong("employeeNumber");
                                         // Save the download URL to Firestore

                                            Map<String, Object> orderData = new HashMap<>();
                                            String id =(UUID.randomUUID().toString());
                                            orderData.put("id", id);
                                            orderData.put("tittle", binding.tittleET.getText().toString());
                                            orderData.put("description", binding.descriptionET.getText().toString());
                                            orderData.put("location", binding.locationET.getText().toString());
                                            orderData.put("personName", binding.personET.getText().toString());
                                            orderData.put("deviceImg",  task.getResult().toString());
                                            orderData.put("employeeNumber",employeeNumber );
                                            fireStore.collection(COLLECTION_ORDER_DATA)
                                                    .document(currentUser.getUid())
                                                    .collection(COLLECTION_ON_PENDING)
                                                    .add(orderData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            binding.progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(SendRequestActivity.this, "تم ارسال طلبك بنجاح", Toast .LENGTH_SHORT).show();
                                                            binding.tittleET.setText("");
                                                            binding.locationET.setText("");
                                                            binding.personET.setText("");
                                                            binding.descriptionET.setText("");
                                                            binding.deviceImg2.setVisibility(View.GONE);
                                                            binding.deviceImg.setVisibility(View.VISIBLE);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast   .makeText(SendRequestActivity.this, "Error uploading image or saving URL", Toast .LENGTH_SHORT).show();

                                                        }
                                                    });
                                        }
                                    });

//                            Map<String, Object> data = new HashMap<>();
//                            data.put("deviceImg", task.getResult().toString());
//
//                            fireStore.collection(COLLECTION_ORDER_DATA)
//                                    .document(currentUser.getUid())
//                                    .collection(COLLECTION_ON_PENDING)
//
//                                    .add(data)
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//
//                                            Toast .makeText(SendRequestActivity.this, "Uploaded Successfully",  Toast.LENGTH_SHORT).show();}
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Toast .makeText(SendRequestActivity.this, "Error uploading image or saving URL",  Toast.LENGTH_SHORT).show();
//                                    });
                        } else {
                            Toast  .makeText(SendRequestActivity.this, "Error uploading image or getting download URL", Toast .LENGTH_SHORT).show();
                            Log.d("TAG", "onComplete: " + task.getException().getMessage());
                            //binding.progressBar.setVisibility(View.GONE);


                        }
                    }
                });
    }
}




}