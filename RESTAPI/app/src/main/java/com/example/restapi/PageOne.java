package com.example.restapi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageOne extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PhoneBookInfo> phoneBook_array;


    public PageOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment_one = inflater.inflate(R.layout.fragment_page_one, container, false); // 뜻 ??
        mRecyclerView = fragment_one.findViewById(R.id.phoneBook_list);
        mLayoutManager = new LinearLayoutManager(getActivity()); // this??
        mRecyclerView.setLayoutManager(mLayoutManager);

        phoneBook_array = new ArrayList<>();

        phoneBook_array.add(new PhoneBookInfo("Daniel", "010-1234-5678"));
        phoneBook_array.add(new PhoneBookInfo("Patty", "010-5245-8563"));
        phoneBook_array.add(new PhoneBookInfo("Mary", "010-7241-9721"));
        phoneBook_array.add(new PhoneBookInfo("Tom", "010-8317-1237"));
        phoneBook_array.add(new PhoneBookInfo("Hank", "010-4117-4337"));
        phoneBook_array.add(new PhoneBookInfo("James", "010-9711-9787"));
        phoneBook_array.add(new PhoneBookInfo("John", "010-6666-7777"));


        mRecyclerView.setAdapter(new RecyclerAdapter_PhoneBook(phoneBook_array, R.layout.recyclerview_phonebook));

        // Inflate the layout for this fragment
        return fragment_one;
    }

}
