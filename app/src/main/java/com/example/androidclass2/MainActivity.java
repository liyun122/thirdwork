package com.example.androidclass2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_date;
    private Button bt_add;
    private RecyclerView todo_recyclerView;
    private List<Todo> list;
    private TodoAdapter todoAdapter;
    private int position_current;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private Calendar calendar= Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper =new MyDatabaseHelper(this,"TodoList.db",null,1);
        dbHelper.getWritableDatabase();
        db=dbHelper.getWritableDatabase();

        initView();
        getData();
    }

    //读取数据库
    private void getData() {
        Cursor cursor=db.query("Todo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String date=cursor.getString(cursor.getColumnIndex("date"));
                String date_ex=cursor.getString(cursor.getColumnIndex("date_ex"));
                String content=cursor.getString(cursor.getColumnIndex("content"));
                int progress=cursor.getInt(cursor.getColumnIndex("progress"));
                byte[] image=cursor.getBlob(cursor.getColumnIndex("image"));
                Todo todo=new Todo(date,date_ex,content,progress,image);
                list.add(todo);
            }while (cursor.moveToNext());
        }
        Collections.sort(list, new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                return o1.getDate_l().compareTo(o2.getDate_l());
            }
        });
        todoAdapter.notifyDataSetChanged();
        cursor.close();
    }

    private void initView() {
        et_date=findViewById(R.id.add_et_date);
        bt_add=findViewById(R.id.add_bt_add);
        todo_recyclerView =findViewById(R.id.todo_list);

        et_date.setOnClickListener(this);
        bt_add.setOnClickListener(this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        todo_recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        todoAdapter=new TodoAdapter(list);
        todo_recyclerView.setAdapter(todoAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(todo_recyclerView);

        todoAdapter.setOnMyItemClickListener(new TodoAdapter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int position) {
                position_current=position;
                Intent intent=new Intent(MainActivity.this,TodoEditActivity.class);
                intent.putExtra("progress",list.get(position).getProgress());
                startActivityForResult(intent,200);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_et_date:
                showDatePickerDialog(this,  0, et_date, calendar);
                break;
            case R.id.add_bt_add:
                startActivityForResult(new Intent(MainActivity.this,TodoAddActivity.class),100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==100&&resultCode==RESULT_OK){
            list.add((Todo) data.getSerializableExtra("todo"));
            Collections.sort(list, new Comparator<Todo>() {
                @Override
                public int compare(Todo o1, Todo o2) {
                    return o1.getDate_l().compareTo(o2.getDate_l());
                }
            });
            todoAdapter.notifyDataSetChanged();
        }
        if(requestCode==200&&resultCode==RESULT_OK){
            try {
                list.get(position_current).setProgress(Integer.parseInt(data.getStringExtra("new_pro")));
                ContentValues values=new ContentValues();
                values.put("progress",Integer.parseInt(data.getStringExtra("new_pro")));
                db.update("Todo",values,"content=?",new String[]{list.get(position_current).getContent()});
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this,"输入进度有误,请重新输入",Toast.LENGTH_SHORT).show();
            }
            todoAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //日历弹框
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

    //简易侧滑移除
    ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            db.delete("Todo","content=?",new String[]{list.get(position).getContent()});
            list.remove(position);
            todoAdapter.notifyItemRemoved(position);
        }
    };

}
