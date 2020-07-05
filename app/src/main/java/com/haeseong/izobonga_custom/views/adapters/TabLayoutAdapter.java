package com.haeseong.izobonga_custom.views.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.haeseong.izobonga_custom.models.Customer;
import com.haeseong.izobonga_custom.views.fragments.CustomerFragment;
import com.haeseong.izobonga_custom.views.fragments.GraphFragment;

import java.util.ArrayList;

import static com.haeseong.izobonga_custom.views.fragments.CustomerFragment.CUSTOMER_FRAGMENT;
import static com.haeseong.izobonga_custom.views.fragments.GraphFragment.GRAPH_FRAGMENT;


public class TabLayoutAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private ArrayList<Customer> customers;

    public TabLayoutAdapter(FragmentManager fm, int tabCount, ArrayList<Customer> customers) {
        super(fm);
        this.tabCount = tabCount;
        this.customers = customers;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                CustomerFragment customerFragment = new CustomerFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(CUSTOMER_FRAGMENT, customers); // Key, Value
                customerFragment.setArguments(bundle);
                return customerFragment;
            case 1:
                GraphFragment graphFragment = new GraphFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList(GRAPH_FRAGMENT, customers); // Key, Value
                graphFragment.setArguments(bundle2);
                return graphFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}


