package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactInfoActivity extends AppCompatActivity {
    TextView name, department, phoneNumber, email;
    Button callBtn, messageBtn, emailBtn;
    ContactItem item;
    String address, numberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        name = findViewById(R.id.name);
        name.setTypeface(null);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 33);

        department = findViewById(R.id.department);
        department.setTypeface(null);
        department.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

        phoneNumber = findViewById(R.id.phoneNumber);
        phoneNumber.setTypeface(null);
        phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);

        email = findViewById(R.id.email);
        email.setTypeface(null);
        email.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);

        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        callBtn = findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+numberStr)));
            }
        });

        messageBtn = findViewById(R.id.messageBtn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:"+numberStr)));
            }
        });

        emailBtn = findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
                    mail_intent.setData(Uri.parse("mailto:"));
                    mail_intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                    startActivity(mail_intent);
                } catch (android.content.ActivityNotFoundException exception) {
                    Toast.makeText(ContactInfoActivity.this, "이메일 앱을 찾을 수 없음", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void processIntent(Intent passedIntent) {
        if (passedIntent != null){
            item = (ContactItem) passedIntent.getSerializableExtra("contact");
            if (item != null) {
                //Toast.makeText(this, "아이템을 무사히 전달받음: 이름" + item.getName(), Toast.LENGTH_SHORT).show();
                numberStr = item.getPhoneNumber();
                address = item.getEmail();

                name.setText(item.getName() + " 교수님");
                department.setText(item.getDepartment());
                phoneNumber.setText(numberStr);
                email.setText(address);
            }
        }
    }
}