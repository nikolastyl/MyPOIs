package com.unipi.nikolastyl.mypois;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    Context c;
    ArrayList<String> list;
    ArrayList<String> listFull;


    public MyAdapter(Context c,ArrayList<String> list){
        this.c=c;
        this.list=list;
        listFull=new ArrayList<>(list);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textview.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filterList = new ArrayList<>();
            if(charSequence==null||charSequence.length()==0){
                filterList.addAll(listFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase(Locale.ROOT).trim();

                for(String item:listFull){
                    if(item.toLowerCase(Locale.ROOT).contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };
}
