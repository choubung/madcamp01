package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Fragment3 extends Fragment {
    EditText editSubject, editGreeting, editText, editConclusion;
    FloatingActionButton button;
    String[] situations = {"출결을 문의하자!", "인정 결석을 받자!", "과제 내용을 여쭤보자!", "빌넣을 해보자!", "수업 내용을 물어보자!", "성적 기준을 여쭤보자!"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);

        editSubject = rootView.findViewById(R.id.mailSubject);
        editGreeting = rootView.findViewById(R.id.greetingText);
        editText = rootView.findViewById(R.id.textText);
        editConclusion = rootView.findViewById(R.id.mailConclusion);

        Spinner spinner1 = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, situations);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button = rootView.findViewById(R.id.send_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = editSubject.getText().toString();
                String greeting = editGreeting.getText().toString();
                String text = editText.getText().toString();
                String conclusion = editConclusion.getText().toString();
                String textAll = greeting + "\n" + text + "\n" + conclusion;

                try {
                    Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
                    mail_intent.setData(Uri.parse("mailto:"));
                    mail_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
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
}
