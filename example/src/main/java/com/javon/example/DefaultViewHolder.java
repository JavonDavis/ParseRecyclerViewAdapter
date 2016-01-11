package com.javon.example;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.javon.parserecyclerviewadapter.annotations.Layout;
import com.javon.parserecyclerviewadapter.annotations.ParseName;
import com.parse.ParseImageView;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 05/01/16.
 */
@Layout(R.layout.list_item)
public class DefaultViewHolder extends RecyclerView.ViewHolder{

    public DefaultViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title_view);
        imageView = (ParseImageView) itemView.findViewById(R.id.imageView);

    }

    @ParseName("title")
    public TextView titleView;

    @ParseName("image")
    public ParseImageView imageView;

}
