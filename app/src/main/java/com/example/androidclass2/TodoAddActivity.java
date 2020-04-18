package com.example.androidclass2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class TodoAddActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText add_et_todo,add_et_date,add_et_date_ex;
    private ImageView imageView;
    private Button add_bt_add;
    private Todo todo;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private byte[] imageData;
    private Calendar calendar= Calendar.getInstance(Locale.CHINA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);
        dbHelper=new MyDatabaseHelper(this,"TodoList.db",null,1);
        db=dbHelper.getWritableDatabase();
        initView();
    }

    private void initView() {
        add_et_todo=findViewById(R.id.add_et_todo);
        add_et_date=findViewById(R.id.add_et_date);
        add_bt_add=findViewById(R.id.add_bt_add);
        add_et_date_ex=findViewById(R.id.add_et_date_ex);
        imageView=findViewById(R.id.image);

        add_et_date.setOnClickListener(this);
        add_bt_add.setOnClickListener(this);
        add_et_date_ex.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
                int size = bitmap.getWidth() * bitmap.getHeight() * 4;

                ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos);
                    imageData = baos.toByteArray();
                }catch (Exception e){
                }finally {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_et_date:
                showDatePickerDialog(this,0,add_et_date,calendar);
                break;
            case R.id.add_et_date_ex:
                showDatePickerDialog(this,0,add_et_date_ex,calendar);
                break;
            case R.id.image:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_bt_add:
                String todo_date=add_et_date.getText().toString().trim();
                String todo_date_ex=add_et_date_ex.getText().toString().trim();
                String todo_content=add_et_todo.getText().toString().trim();
                if(!todo_date.equals("")&&!todo_content.equals("")&&!todo_date_ex.equals("")&& imageData !=null){
                     todo=new Todo(todo_date,todo_date_ex,todo_content, imageData);
                }else {
                    Toast.makeText(this,"日期、事项或图片为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values=new ContentValues();
                values.put("date",todo.getDate());
                values.put("date_ex",todo.getDate_ex());
                values.put("content",todo.getContent());
                values.put("progress",todo.getProgress());
                values.put("image",todo.getImage());
                db.insert("Todo",null,values);
                Intent intent=new Intent();
                intent.putExtra("todo",todo);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    //弹出日历
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
