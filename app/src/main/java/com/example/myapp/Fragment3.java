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
import android.widget.Button;
import android.widget.Toast;

public class Fragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_3, container, false);

        Button button = (Button) rootView.findViewById(R.id.send_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
                    mail_intent.setData(Uri.parse("mailto:"));
                    mail_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
                    startActivity(mail_intent);
                } catch (android.content.ActivityNotFoundException exception) {
                    //
                }
            }
        });

        return rootView;
    }
}