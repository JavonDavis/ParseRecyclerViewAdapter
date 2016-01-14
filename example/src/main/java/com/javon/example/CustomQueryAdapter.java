package com.javon.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.javon.parserecyclerviewadapter.ParseRecyclerQueryAdapter;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 11/01/16.
 */
public class CustomQueryAdapter extends ParseRecyclerQueryAdapter<ParseObject, DefaultViewHolder> {

    public CustomQueryAdapter(Context context, Class clazz, String className) {
        super(context, clazz, className);
    }

    public CustomQueryAdapter(Context context, Class clazz,QueryFactory queryFactory) {
        super(context, clazz,queryFactory);
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.staticTitle.setText(object.getString("title").substring(0,2)+"...");
    }
}
