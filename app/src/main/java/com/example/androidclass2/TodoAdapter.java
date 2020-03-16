package com.example.androidclass2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by xing on 2020/3/14.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<Todo> list_todo;
    public TodoAdapter(List<Todo> list_todo){
        this.list_todo=list_todo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date,tv_todo;
        Button bt_finish;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_todo=itemView.findViewById(R.id.tv_todo);
            bt_finish=itemView.findViewById(R.id.bt_finish);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.bt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_todo.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo=list_todo.get(position);
        holder.tv_date.setText(todo.getDate());
        holder.tv_todo.setText(todo.getContent());
    }

    @Override
    public int getItemCount() {
        return list_todo.size();
    }
}
