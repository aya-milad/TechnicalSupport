package com.example.technicalsupport.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.technicalsupport.R;
import com.example.technicalsupport.adapters.MyRequestsAdapter;
import com.example.technicalsupport.databinding.ActivityMyRequestsBinding;
import com.example.technicalsupport.ui.fragment.DoneFragment;
import com.example.technicalsupport.ui.fragment.InProgressFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class MyRequestsActivity extends AppCompatActivity {
    private ActivityMyRequestsBinding binding;
    private   ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<String> tb=new ArrayList<>();
        tb.add("تم الانتهاء");
        tb.add("جاري الصيانة");
        fragments=new ArrayList<>();
        fragments.add(new DoneFragment());
        fragments.add(new InProgressFragment());
        MyRequestsAdapter adapter=new MyRequestsAdapter(MyRequestsActivity.this,fragments);
        binding.VP.setAdapter(adapter);
        binding.VP.setCurrentItem(1);

        new TabLayoutMediator(binding.TL, binding.VP, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tb.get(position));
            }
        }).attach();
    }
}