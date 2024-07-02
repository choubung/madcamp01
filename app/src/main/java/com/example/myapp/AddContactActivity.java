package com.example.myapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {
    EditText name, department, number, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        name = findViewById(R.id.editTextName);
        name.setTypeface(null);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

        department =  findViewById(R.id.editTextDepartment);
        department.setTypeface(null);
        department.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

        number = findViewById(R.id.editTextPhone);
        number.setTypeface(null);
        number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

        email = findViewById(R.id.editTextEmail);
        email.setTypeface(null);
        email.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("department", department.getText().toString());
                intent.putExtra("number", number.getText().toString());
                intent.putExtra("email", email.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}