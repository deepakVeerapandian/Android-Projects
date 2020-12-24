package com.example.a801100869_midterm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SourceAdapter extends ArrayAdapter<Cities> {

    public SourceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Cities> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Cities source = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_main_listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_source_name = (TextView) convertView.findViewById(R.id.txtMainListName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_source_name.setText(source.city_name+" , "+source.country_name);
        return  convertView;
    }

    private static class ViewHolder{
        TextView tv_source_name;
    }
}