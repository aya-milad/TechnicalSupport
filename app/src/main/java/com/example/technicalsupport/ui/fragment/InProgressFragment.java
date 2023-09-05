package com.example.technicalsupport.ui.fragment;

import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_ON_PENDING;
import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_ON_PROGRESS;
import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_ORDER_DATA;
import static com.example.technicalsupport.model.javaClasses.Constant.COLLECTION_USER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.technicalsupport.model.interfaceClass.OnClickItemDetailsRequest;
import com.example.technicalsupport.adapters.InProgressAdapter;
import com.example.technicalsupport.databinding.FragmentInProgressBinding;
import com.example.technicalsupport.model.interfaceClass.SendData;
import com.example.technicalsupport.model.javaClasses.Request;
import com.example.technicalsupport.ui.activity.DetailsActivity;
import com.example.technicalsupport.ui.activity.HomeActivity;
import com.example.technicalsupport.ui.activity.HomeSupportActivity;
import com.example.technicalsupport.ui.activity.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InProgressFragment extends Fragment implements OnClickItemDetailsRequest {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentInProgressBinding binding;
     ArrayList<Request> requestArrayList;
    private FirebaseFirestore fireStore;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    private InProgressAdapter inProgressAdapter;
    private String documentId;


    SendData sendData;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InProgressFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InProgressFragment newInstance(String param1, String param2) {
        InProgressFragment fragment = new InProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentInProgressBinding.inflate(inflater,container,false);
        fireStore=FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        binding.progressBar.setVisibility(View.VISIBLE);
        getAllOrders();
        Log.d("TAG", "onSuccess: " +currentUser.getUid());

        return binding.getRoot();
    }
    private  void  getAllOrders() {
        fireStore.collection(COLLECTION_USER)
                .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    documentId = documentSnapshot.getId();
                                    binding.progressBar.setVisibility(View.VISIBLE);

                                    requestArrayList = new ArrayList<>();
                                    fireStore.collection(COLLECTION_ORDER_DATA)
                                            .document(currentUser.getUid())
                                            .collection(COLLECTION_ON_PROGRESS)
                                            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    binding.progressBar.setVisibility(View.GONE);
                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                        if (documentSnapshot.exists()) {
                                                            String  tittle = documentSnapshot.getString("tittle");

                                                            String time = documentSnapshot.getString("time");
                                                            String id = documentSnapshot.getString("id");
                                                            Request request = new Request(id,tittle,time);
                                                            requestArrayList.add(request);
                                                            Log.d("TAG", "onSuccess: "+requestArrayList.size());
                                                            Log.d("TAG", "onSuccess: "+tittle);
                                                            if (inProgressAdapter == null) {
                                                                inProgressAdapter = new InProgressAdapter(requestArrayList,InProgressFragment.this);
                                                                Log.d("TAG", "onSuccess: "+inProgressAdapter.getItemCount());

                                                                binding.rv.setAdapter(inProgressAdapter);
                                                                binding.rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                                            } else {
                                                                inProgressAdapter.notifyDataSetChanged();
                                                            }



                                                        } else   {
                                                            Log.d("TAG", "onSuccess: "+ "non exists");
                                                        }


                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), e.getMessage(), Toast .LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });


    }


    @Override
    public void showDetails(int position) {
        Request request= requestArrayList.get(position);
//        sendData.setData(currentUser.getUid(),request.getId());
//        getActivity().startActivity(new Intent(getActivity(), DetailsActivity.class));
        Intent intent=new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("documentId",currentUser.getUid());
        intent.putExtra("id",request.getId());
        intent.putExtra("from","inProgress");
        intent.putExtra("fromInProgress","true");


        startActivity(intent);

    }

    @Override
    public void deliveryDevice(int position) {
        Request request= requestArrayList.get(position);

        DeliveryDeviceFragment fragment=DeliveryDeviceFragment.newInstance(documentId,request.getId());
        fragment.show(getActivity().getSupportFragmentManager(),"");

    }

    @Override
    public void deniedRequest(int position) {

    }
}