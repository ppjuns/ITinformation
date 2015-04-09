package com.rabbit.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rabbit.application.R;
import com.rabbit.application.bean.Information;
import com.rabbit.application.bean.MyVolley;

import java.util.List;

/**
 * Created by Rabbit on 2015/3/18.
 */
public class InformationAdapter extends BaseAdapter {
    ImageLoader imageLoader;
    List<Information> list;
    Context context;


    public InformationAdapter(Context content, List<Information> list) {
        this.context = content;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Information getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Information information = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.information_list_item, null);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_android_title);
            viewHolder.img_pic = (NetworkImageView) convertView.findViewById(R.id.img_android_pic);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_android_datetime);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }


        viewHolder.tv_title.setText(information.getTitle());
        viewHolder.img_pic.setDefaultImageResId(R.mipmap.ic_launcher);
        viewHolder.img_pic.setErrorImageResId(R.mipmap.ic_launcher);
        viewHolder.img_pic.setImageUrl(information.getImg_url(), MyVolley.getImageLoader());
        viewHolder.tv_date.setText(information.getDatetime());

        return convertView;
    }


    class ViewHolder {
        TextView tv_title;
        NetworkImageView img_pic;
        TextView tv_date;

    }
}
