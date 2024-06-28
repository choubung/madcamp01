package com.example.myapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    ArrayList<ContactItem> contactItems = new ArrayList<>();
    FloatingActionButton fab;

    private ActivityResultLauncher<Intent> addContactLauncher;

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

                //Toast.makeText(getActivity(), "아이템 선택됨" + item.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
                intent.putExtra("contact", item);
                startActivity(intent);
            }
        });

        addContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String name = result.getData().getStringExtra("name");
                        String department = result.getData().getStringExtra("department");
                        String number = result.getData().getStringExtra("number");
                        String email = result.getData().getStringExtra("email");

                        contactItems.add(new ContactItem(name, department, number, email));
                        adapter.notifyItemInserted(contactItems.size() - 1);  // 변경된 위치가 마지막 인덱스이므로 -1을 해준다.
                    }
                }
        );

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                addContactLauncher.launch(intent);

                //startActivity(intent);
                //contactItems.add(new ContactItem("name", "department", "number", "email"));
                //adapter.notifyItemInserted(contactItems.size());
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

                ContactItem item = new ContactItem(name, department, phoneNumber, email);
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