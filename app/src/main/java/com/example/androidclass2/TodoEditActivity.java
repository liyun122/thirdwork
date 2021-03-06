package com.example.androidclass2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TodoEditActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        editText=findViewById(R.id.progressBar_edit);
        button=findViewById(R.id.progressBar_bt);

        Intent intent=getIntent();
        editText.setText(String.valueOf(intent.getIntExtra("progress",0)));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("new_pro",editText.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
