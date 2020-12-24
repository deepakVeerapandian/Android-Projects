package com.example.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);

        NewsAdapter.ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
            viewHolder = new NewsAdapter.ViewHolder();
            viewHolder.iv_news_image = (ImageView) convertView.findViewById(R.id.new_image);
            viewHolder.tv_news_title = (TextView) convertView.findViewById(R.id.news_title);
            viewHolder.tv_news_author = (TextView) convertView.findViewById(R.id.news_author);
            viewHolder.tv_news_date = (TextView) convertView.findViewById(R.id.news_date);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (NewsAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv_news_title.setText(news.title);
        viewHolder.tv_news_date.setText(news.publishedAt);
        viewHolder.tv_news_author.setText(news.author);
        viewHolder.iv_news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //webView.loadUrl("http://www.google.com");

//                String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
//                webView.loadData(customHtml, "text/html", "UTF-8");
//                System.out.println("======iam working");
            }
        });
        Picasso.get().load(news.urlToImage).into(viewHolder.iv_news_image);
        return  convertView;
    }

    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<News> objects) {
        super(context, resource, objects);
    }
    private static class ViewHolder{
        ImageView iv_news_image;
        TextView tv_news_title;
        TextView tv_news_author;
        TextView tv_news_date;
    }
}
