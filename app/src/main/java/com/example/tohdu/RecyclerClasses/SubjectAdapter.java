package com.example.tohdu.RecyclerClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tohdu.R;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private final ArrayList<SubjectClass> schedulePages;

    public SubjectAdapter(ArrayList<SubjectClass> schedulePages,RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.schedulePages = schedulePages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subject, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(schedulePages.get(position));
    }

    @Override
    public int getItemCount() {
        return schedulePages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,time,room,instructor;
        CardView cardView;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            name = itemView.findViewById(R.id.nameView);
            time = itemView.findViewById(R.id.timeView);
            room = itemView.findViewById(R.id.roomView);
            instructor = itemView.findViewById(R.id.instructorView);
            layout = itemView.findViewById(R.id.layout);
            cardView.setOnClickListener(view ->{

                if(recyclerViewInterface == null){
                    return;
                }
                int position = getAdapterPosition();

                if(position == RecyclerView.NO_POSITION){
                    return;
                }

                recyclerViewInterface.onViewClicked(position);

            });


            layout.setOnClickListener(view ->{

                if(recyclerViewInterface == null){
                    return;
                }
                int position = getAdapterPosition();

                if(position == RecyclerView.NO_POSITION){
                    return;
                }

                recyclerViewInterface.onViewClicked(position);

            });
        }

        public String formatTime(String time){
            int hours = Integer.parseInt(time.split(":")[0]);
//                    int minutes = Integer.parseInt(schedulePage.getTimeIn().split(":")[1]);
            String prefix = hours>=12?"PM":"AM";
            return  (hours>12? hours-12: (hours==0?"12": hours)) + ":"+time.split(":")[1]+prefix;

        }
        public void bind(SubjectClass schedulePage) {

            StringBuilder stringBuilder = new StringBuilder(formatTime(schedulePage.getTimeIn())+"-"+formatTime(schedulePage.getTimeOut()));

            name.setText(schedulePage.getName().toUpperCase());
            instructor.setText(schedulePage.getInstructor());
            room.setText(schedulePage.getRoom());
            time.setText(stringBuilder);
        }
    }
}
