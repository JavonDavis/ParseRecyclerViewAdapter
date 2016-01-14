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
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 12/11/2015.
 */
public class ParseRecyclerQueryAdapter<T extends ParseObject, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected Context mContext;
    protected Class<V> clazz;
    protected QueryFactory queryFactory;
    protected List<T> objects;
    protected WeakHashMap<Field,String> fields = new WeakHashMap<>();
    protected ParseObject object;
    protected int objectsPerPage;
    private List<OnQueryLoadListener<T>> listeners = new ArrayList<>();
    protected int currentPage;
    protected boolean hasNextPage;
    private boolean paginationEnabled = false;

    public ParseRecyclerQueryAdapter(Context context, Class<V> clazz,final String className)
    {
        this(context,clazz,new ParseRecyclerQueryAdapter.QueryFactory() {
            public ParseQuery<T> create() {
                ParseQuery query = ParseQuery.getQuery(className);
                query.orderByDescending("createdAt");
                return query;
            }
        });
        if(className == null) {
            throw new RuntimeException("You need to specify a className for the ParseRecyclerQueryAdapter");
        }
    }

    public ParseRecyclerQueryAdapter(Context context, Class<V> clazz,final QueryFactory queryFactory)
    {
        this.clazz = clazz;
        this.mContext = context;
        this.objects = new ArrayList<>();
        this.queryFactory = queryFactory;
        this.objectsPerPage = 5;
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

    public void clear() {
        this.objects.clear();
        this.notifyDataSetChanged();
        this.currentPage = 0;
    }

    public T getItem(int index) {
        return index == this.getPaginationCellRow()?null: this.objects.get(index);
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public int getItemViewType(int position) {
        return position == this.getPaginationCellRow()?1:0;
    }

    private boolean shouldShowPaginationCell() {
        return this.paginationEnabled && this.objects.size() > 0 && this.hasNextPage;
    }

    private int getPaginationCellRow() {
        return this.objects.size();
    }

    public void loadNextPage() {
        this.loadParseData(this.currentPage + 1, false);
    }

    private void loadParseData(final int page, final boolean shouldClear)
    {
        final ParseQuery query = this.queryFactory.create();

        if(this.objectsPerPage > 0 && this.paginationEnabled) {
            this.setPageOnQuery(page, query);
        }

        this.notifyOnLoadingListeners();
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                if(query.getCachePolicy() != ParseQuery.CachePolicy.CACHE_ONLY || e == null || e.getCode() != 120) {
                    if(e == null || e.getCode() != 100 && e.getCode() == 120) {
                        if(list != null) {
                            ParseRecyclerQueryAdapter.this.currentPage = page;
                            ParseRecyclerQueryAdapter.this.hasNextPage = list.size() > ParseRecyclerQueryAdapter.this.objectsPerPage;
                            if(ParseRecyclerQueryAdapter.this.paginationEnabled && ParseRecyclerQueryAdapter.this.hasNextPage)
                            {
                                list.remove(ParseRecyclerQueryAdapter.this.objectsPerPage);
                            }

                            if(shouldClear)
                            {
                                ParseRecyclerQueryAdapter.this.objects.clear();
                            }
                            ParseRecyclerQueryAdapter.this.objects.addAll(list);
                            ParseRecyclerQueryAdapter.this.notifyDataSetChanged();
                        }
                    } else {
                        ParseRecyclerQueryAdapter.this.hasNextPage = true;
                    }
                    ParseRecyclerQueryAdapter.this.notifyOnLoadedListeners(list,e);
                }
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

        object = objects.get(position);

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
        loadParseData(0,true);
    }

    @Override
    public int getItemCount() {
        int count = this.objects.size();
        if(this.shouldShowPaginationCell()) {
            ++count;
        }
        return count;
    }

    public void setPaginationEnabled(boolean paginationEnabled) {
        this.paginationEnabled = paginationEnabled;
    }

    protected void setPageOnQuery(int page, ParseQuery<T> query) {
        query.setLimit(this.objectsPerPage + 1);
        query.setSkip(page * this.objectsPerPage);
    }

    public void addOnQueryLoadListener(OnQueryLoadListener<T> listener) {
        this.listeners.add(listener);
    }

    public void removeOnQueryLoadListener(OnQueryLoadListener<T> listener) {
        this.listeners.remove(listener);
    }

    private void notifyOnLoadingListeners() {
        Iterator i$ = this.listeners.iterator();

        while(i$.hasNext()) {
            OnQueryLoadListener listener = (OnQueryLoadListener)i$.next();
            listener.onLoading();
        }

    }

    private void notifyOnLoadedListeners(List<T> objects, Exception e) {
        Iterator i$ = this.listeners.iterator();

        while(i$.hasNext()) {
            OnQueryLoadListener listener = (OnQueryLoadListener)i$.next();
            listener.onLoaded(objects, e);
        }

    }

    /* ==================== Interfaces ==========================*/
    public interface OnQueryLoadListener<T> {
        void onLoading();

        void onLoaded(List<T> var1, Exception var2);
    }

    public interface QueryFactory<T extends ParseObject> {
        ParseQuery<T> create();
    }
}

