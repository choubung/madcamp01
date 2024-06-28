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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Fragment1 extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ContactAdapter adapter;

    ContactItem item;
    ArrayList<ContactItem> contactItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_1, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        parser();

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactAdapter(getActivity().getApplicationContext(), contactItems);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ContactAdapter.ViewHolder holder, View view, int position) {
                ContactItem item = adapter.getItem(position);

                Toast.makeText(getActivity().getApplicationContext(), "아이템 선택됨" + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void parser() {
        InputStream inputStream = getResources().openRawResource(R.raw.contactlist);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuffer stringBuffer = new StringBuffer();
        String line;

        try {
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("contact_list"));

            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                String name = jsonObject1.getString("name");
                String department = jsonObject1.getString("department");
                String phoneNumber = jsonObject1.getString("phoneNumber");
                String email = jsonObject1.getString("email");

                item = new ContactItem(name, department, phoneNumber, email);
                contactItems.add(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}