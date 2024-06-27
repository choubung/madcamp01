package com.example.myapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Fragment1 extends Fragment {
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ContactAdapter adapter = new ContactAdapter(getActivity().getApplicationContext());

        // test
        adapter.addItem(new ContactItem("김주균", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("김주균", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("김주균", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("창병모", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("김주균", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("최영우", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("김주균", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("유석종", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));
        adapter.addItem(new ContactItem("김주균", "소프트웨어학부", "02)710-9430", "jgkim@sm.ac.kr"));

        recyclerView.setAdapter(adapter);
        return rootView;
    }
}