package com.example.tohdu.RecyclerClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tohdu.R;

import java.util.ArrayList;

import kotlin.jvm.Volatile;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private ArrayList<FileClass> fileList;
    private RecyclerViewInterface recyclerViewInterface;

    public FileAdapter(ArrayList<FileClass> fileList, RecyclerViewInterface recyclerViewInterface) {
        this.fileList = fileList;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subject,parent,false),recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(fileList.get(position));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
        }

        public void bind(FileClass fileClass) {

        }
    }
}
