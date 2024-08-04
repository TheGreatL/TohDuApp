package com.example.tohdu.RecyclerClasses;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tohdu.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private final List<TodoItem> todoList;
    protected final RecyclerViewInterface recyclerViewInterface;
    private final int LAYOUT;

    public TodoAdapter(List<TodoItem> todoList, RecyclerViewInterface recyclerViewInterface, int LAYOUT) {
        this.todoList = todoList;
        this.recyclerViewInterface = recyclerViewInterface;
        this.LAYOUT = LAYOUT;

    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {

        holder.bind(todoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, time, note;
        String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            note = itemView.findViewById(R.id.note);

            itemView.setOnClickListener((view) -> {
                if (recyclerViewInterface == null) {
                    return;
                }
                int position = getAdapterPosition();
                if(position==RecyclerView.NO_POSITION){
                    return;
                }
                recyclerViewInterface.onViewClicked(position);

            });
        }


        public void bind(TodoItem todoItem, int position) {
            String year = todoItem.getDate().split("/")[0];
            String month = todoItem.getDate().split("/")[1];
            String day = todoItem.getDate().split("/")[2];
            Log.d("BINDMONTH", "bind: "+month);
            String setDate = months[Integer.parseInt(month)-1]+" "+day+", "+year;
            int hours = Integer.parseInt(todoItem.getTime().split(":")[0]);
            int minutes = Integer.parseInt(todoItem.getTime().split(":")[1]);

            String prefix =hours>=12?"PM":"AM";

            String setTime =(hours>12? hours-12 : hours==0? 12: hours) +":"+(minutes < 10 ? "0" + minutes : minutes)+prefix;
            title.setText(todoItem.getTitle());
            date.setText(setDate);
            time.setText(setTime);
            note.setText("Note: "+todoItem.getMessage());
        }
    }
}
