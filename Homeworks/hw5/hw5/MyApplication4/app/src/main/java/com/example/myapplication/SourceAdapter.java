package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SourceAdapter extends ArrayAdapter<Source> {
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Source source = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.source_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_source_name = (TextView) convertView.findViewById(R.id.source_name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_source_name.setText(source.name);
        return  convertView;
    }

    public SourceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Source> objects) {
        super(context, resource, objects);
    }
    private static class ViewHolder{
        TextView tv_source_name;

    }
}
