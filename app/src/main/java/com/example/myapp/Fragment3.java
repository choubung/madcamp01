package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {
    EditText editSubject, editGreeting, editText, editConclusion;
    AutoCompleteTextView dearTextView;
    FloatingActionButton button;
    String[] situations = {"지금...", "출석을 문의한다!", "결석 인정을 문의한다!", "유고결석 증빙자료를 보낸다!", "과제 내용을 여쭤본다!", "빌넣을 한다!", "성적 관련해 문의한다!"};
    ArrayAdapter<String> nameAdapter;
    List<ContactEntity> allContacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);

        editSubject = rootView.findViewById(R.id.mailSubject);
        editGreeting = rootView.findViewById(R.id.greetingText);
        editText = rootView.findViewById(R.id.textText);
        editConclusion = rootView.findViewById(R.id.mailConclusion);

        dearTextView = rootView.findViewById(R.id.editDear);

        // 교수님 자동완성을 위한 AutoCompleteTextView 셋업
        nameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        dearTextView.setAdapter(nameAdapter);

        // Fetch all contacts from database asynchronously
        new Thread(() -> {
            ContactDatabase db = ContactDatabase.getInstance(getContext());
            allContacts = db.getContactDao().getAllContact();

            getActivity().runOnUiThread(() -> {
                List<String> names = new ArrayList<>();
                for (ContactEntity contact : allContacts) {
                    names.add(contact.getName());
                }
                nameAdapter.addAll(names);
            });
        }).start();

        Spinner spinner1 = rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, situations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    // 첫 번째 항목 ("선택하세요...")은 선택된 상태로 처리하지 않음
                    return;
                }
                parser(position - 1); // 실제 데이터 인덱스는 1부터 시작하므로 -1
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editSubject.setText("");
                editGreeting.setText("");
                editText.setText("");
                editConclusion.setText("");
            }
        });

        // Handle selection of professor from the dropdown
        dearTextView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedName = nameAdapter.getItem(position);
            for (ContactEntity contact : allContacts) {
                if (contact.getName().equals(selectedName)) {
                    dearTextView.setText(contact.getEmail());
                    break;
                }
            }
        });

        button = rootView.findViewById(R.id.send_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String professor = dearTextView.getText().toString();
                String subject = editSubject.getText().toString();
                String greeting = editGreeting.getText().toString();
                String text = editText.getText().toString();
                String conclusion = editConclusion.getText().toString();
                String textAll = greeting + "\n \n" + text + "\n \n" + conclusion;

                try {
                    Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
                    mail_intent.setData(Uri.parse("mailto:"));
                    mail_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{professor});
                    mail_intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    mail_intent.putExtra(Intent.EXTRA_TEXT, textAll);
                    startActivity(mail_intent);
                } catch (android.content.ActivityNotFoundException exception) {
                    // Handle exception
                }
            }
        });

        return rootView;
    }

    private void parser(int position) {
        Log.d("parser", "파서 실행됨");
        InputStream inputStream = getResources().openRawResource(R.raw.mail_text);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder stringBuffer = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("mail_text_list"));

            JSONObject jsonObject1 = (JSONObject) jsonArray.get(position);
            editSubject.setText(jsonObject1.getString("subject"));
            editGreeting.setText(jsonObject1.getString("greeting"));
            editText.setText(jsonObject1.getString("text"));
            editConclusion.setText(jsonObject1.getString("conclusion"));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}