package com.example.androidclass2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class TodoAddActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText add_et_todo,add_et_date;
    private Button add_bt_add;
    private Todo todo;
    private Calendar calendar= Calendar.getInstance(Locale.CHINA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);

        initView();
    }

    private void initView() {
        add_et_todo=findViewById(R.id.add_et_todo);
        add_et_date=findViewById(R.id.add_et_date);
        add_bt_add=findViewById(R.id.add_bt_add);

        add_et_date.setOnClickListener(this);
        add_bt_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_et_date:
                showDatePickerDialog(this,0,add_et_date,calendar);
                break;
            case R.id.add_bt_add:
                String todo_date=add_et_date.getText().toString().trim();
                String todo_content=add_et_todo.getText().toString().trim();
                if(!todo_date.equals("")&&!todo_content.equals("")){
                     todo=new Todo(todo_date,todo_content);
                }else {
                    Toast.makeText(this,"日期或事项为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent();
                intent.putExtra("todo",todo);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }


    public static void showDatePickerDialog(Activity activity, int themeResId, final EditText tv, Calendar calendar) {
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv.setText(year+"-"+(monthOfYear + 1)+"-"+ dayOfMonth);
            }
        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
