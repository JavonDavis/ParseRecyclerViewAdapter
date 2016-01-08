package com.javon.parseadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 12/11/2015.
 */
public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.TempViewHolder> {

    private Context context;
    private ArrayList<String> items;

    public NormalAdapter(Context context, ArrayList<String> stuff)
    {
        this.context = context;
        items = stuff;

    }

    @Override
     public TempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TempViewHolder( new TextView(context));
    }

    @Override
    public void onBindViewHolder(TempViewHolder holder, int position) {
        holder.text.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class TempViewHolder extends RecyclerView.ViewHolder
    {
        public TextView text;
        public TempViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView;
        }
    }
}
