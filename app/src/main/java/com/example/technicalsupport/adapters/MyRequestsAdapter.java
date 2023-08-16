package com.example.technicalsupport.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MyRequestsAdapter extends FragmentStateAdapter {
    ArrayList<Fragment >myRequests ;

    public MyRequestsAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> myRequests) {
        super(fragmentActivity);
        this.myRequests = myRequests;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return myRequests.get(position);
    }

    @Override
    public int getItemCount() {
        return myRequests.size();
    }
}
