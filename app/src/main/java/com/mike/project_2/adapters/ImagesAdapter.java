package com.mike.project_2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mike.project_2.R;
import com.mike.project_2.models.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mng on 2/25/15.
 */
public class ImagesAdapter extends ArrayAdapter<Image>{

    private static class ViewHolder {
        ImageView ivImage;
    };

    public ImagesAdapter(Context context, List<Image> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        Image image = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // set image using Picasso
        Picasso.with(getContext())
                .load(image.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(viewHolder.ivImage);

        return  convertView;
    }
}
