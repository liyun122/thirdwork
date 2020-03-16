package com.example.androidclass2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_todo;
    private EditText et_date;
    private Button bt_add;
    private RecyclerView todo_list;
    private List<Todo> list;
    private TodoAdapter todoAdapter;
    Calendar calendar= Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        et_date=findViewById(R.id.et_date);
        et_todo=findViewById(R.id.et_todo);
        bt_add=findViewById(R.id.bt_add);
        todo_list=findViewById(R.id.todo_list);

        et_date.setOnClickListener(this);
        bt_add.setOnClickListener(this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        todo_list.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        todoAdapter=new TodoAdapter(list);
        todo_list.setAdapter(todoAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(todo_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_date:
                showDatePickerDialog(this,  0, et_date, calendar);
                break;
            case R.id.bt_add:
                String todo_date=et_date.getText().toString().trim();
                String todo_content=et_todo.getText().toString().trim();
                if(!todo_date.equals("")&&!todo_content.equals("")){
                    Todo todo=new Todo(todo_date,todo_content);
                    list.add(todo);
                    //根据日期排序
                    Collections.sort(list, new Comparator<Todo>() {
                        @Override
                        public int compare(Todo o1, Todo o2) {
                            return o1.getDate_l().compareTo(o2.getDate_l());
                        }
                    });
                    todoAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(this,"日期或事项为空",Toast.LENGTH_SHORT).show();
                }
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

    ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            list.remove(position);
            todoAdapter.notifyItemRemoved(position);
        }
    };

}
