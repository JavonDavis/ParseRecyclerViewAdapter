package com.javon.parserecyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javon.parserecyclerviewadapter.annotations.Layout;
import com.javon.parserecyclerviewadapter.annotations.ParseName;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 12/11/2015.
 */
public class ParseRecyclerQueryAdapter<T extends ParseObject, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private String className;
    private Context mContext;
    private View mView;
    private Class<V> clazz;
    private QueryFactory queryFactory;
    private List<T> objects;
    private WeakHashMap<Field,String> fields = new WeakHashMap<>();

    public ParseRecyclerQueryAdapter(Context context, Class<V> clazz,final String className)
    {
        this.clazz = clazz;
        this.mContext = context;
        this.className = className;
        this.objects = new ArrayList<>();
        this.queryFactory = new ParseRecyclerQueryAdapter.QueryFactory() {
            public ParseQuery<T> create() {
                ParseQuery query = ParseQuery.getQuery(ParseRecyclerQueryAdapter.this.className);
                query.orderByDescending("createdAt");
                return query;
            }
        };
        if(className == null) {
            throw new RuntimeException("You need to specify a className for the ParseRecyclerQueryAdapter");
        }
    }

    public ParseRecyclerQueryAdapter(Context context, Class<V> clazz,final String className,QueryFactory queryFactory)
    {
        this.clazz = clazz;
        this.mContext = context;
        this.className = className;
        this.objects = new ArrayList<>();
        this.queryFactory = queryFactory;

        if(className == null) {
            throw new RuntimeException("You need to specify a className for the ParseRecyclerQueryAdapter");
        }
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            Layout annotation = clazz.getAnnotation(Layout.class);
            View view = LayoutInflater.from(mContext).inflate(annotation.value(),parent,false);
            return clazz.getConstructor(View.class).newInstance(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadParseData()
    {
        final ParseQuery query = this.queryFactory.create();
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                objects.addAll(list);
                notifyDataSetChanged();
            }
        });

        for(Field field  : clazz.getDeclaredFields())
        {
            if (field.isAnnotationPresent(ParseName.class))
            {
                ParseName annotation = field.getAnnotation(ParseName.class);
                field.setAccessible(true);
                fields.put(field,annotation.value());

            }
        }
    }

    @Override
    public void onBindViewHolder(V holder, int position) {

        ParseObject object = objects.get(position);

        for(Field field: fields.keySet())
        {

            try {
                if(field.get(holder) instanceof TextView)
                {
                    TextView textView = ((TextView) field.get(holder));
                    textView.setText((CharSequence) object.get(fields.get(field)));
                }
                else if(field.get(holder) instanceof ParseImageView)
                {
                    final ParseImageView imageView = ((ParseImageView) field.get(holder));
                    imageView.setParseFile((ParseFile) object.get(fields.get(field)));
                    imageView.loadInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                        }
                    });
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        loadParseData();

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public interface QueryFactory<T extends ParseObject> {
        ParseQuery<T> create();
    }
}

