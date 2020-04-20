package com.example.androidclass2;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<Todo> list_todo;
    private OnMyItemClickListener listener;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public TodoAdapter(List<Todo> list_todo){
        this.list_todo=list_todo;
        dbHelper =new MyDatabaseHelper(MyApplication.getContext(),"TodoList.db",null,1);
        dbHelper.getWritableDatabase();
        db=dbHelper.getWritableDatabase();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date,tv_todo,tv_date_ex;
        Button bt_finish;
        ProgressBar progressBar;
        TextView progressBar_text;
        ImageView imageView;

         ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_todo=itemView.findViewById(R.id.tv_todo);
            tv_date_ex=itemView.findViewById(R.id.tv_date_ex);
            bt_finish=itemView.findViewById(R.id.bt_finish);
            progressBar=itemView.findViewById(R.id.progressBar);
            progressBar_text=itemView.findViewById(R.id.progressBar_text);
            imageView=itemView.findViewById(R.id.image_todo);
        }
    }


    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;
    }

    public interface OnMyItemClickListener{
        void myClick(View v, int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("Todo","content=?",new String[]{list_todo.get(holder.getAdapterPosition()).getContent()});
                list_todo.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Todo todo=list_todo.get(position);
        holder.tv_date.setText(todo.getDate());
        holder.tv_todo.setText(todo.getContent());
        holder.tv_todo.setTextColor(Color.GRAY);
        holder.tv_date_ex.setText(todo.getDate_ex());
        holder.progressBar.setProgress(todo.getProgress());
        holder.progressBar_text.setText(todo.getProgress()+"%");

        byte[] imgData=todo.getImage();
        if (imgData!=null) {
            //将字节数组转化为位图
            Bitmap imagebitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
            //将位图显示为图片
            holder.imageView.setImageBitmap(imagebitmap);
        }else {
            holder.imageView.setBackgroundResource(android.R.drawable.menuitem_background);
        }

        //将日期转为毫秒数
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(todo.getDate_ex());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date.getTime()<System.currentTimeMillis()){
            holder.tv_todo.setTextColor(Color.RED);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.myClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_todo.size();
    }
}
