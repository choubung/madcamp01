package com.example.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ContactInfoActivity extends AppCompatActivity {
    TextView name, department, phoneNumber, email;
    Button callBtn, messageBtn, emailBtn;
    ContactItem item;
    String address, numberStr;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        name = findViewById(R.id.name);
        department = findViewById(R.id.department);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);

        Intent passedIntent = getIntent();
        processIntent(passedIntent);

        callBtn = (Button) findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+numberStr)));
            }
        });

        messageBtn = (Button) findViewById(R.id.messageBtn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:"+numberStr)));
            }
        });

        emailBtn = (Button) findViewById(R.id.emailBtn);
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

        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.modify){
            Toast.makeText(this, "수정 선택됨", Toast.LENGTH_SHORT).show();
            // 구현 필요
            return true;
        } else if (menuItem.getItemId() == R.id.delete) {
            Toast.makeText(this, "삭제 선택됨", Toast.LENGTH_SHORT).show();
            // 구현 필요
            return true;
        } else{
            return super.onOptionsItemSelected(menuItem);
        }
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