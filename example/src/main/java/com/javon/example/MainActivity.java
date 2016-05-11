package com.javon.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;

import com.javon.parserecyclerviewadapter.ParseRecyclerQueryAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParseRecyclerQueryAdapter<ParseObject,DefaultViewHolder> mainAdapter = new ParseRecyclerQueryAdapter<>(this, DefaultViewHolder.class
                ,new ParseRecyclerQueryAdapter.QueryFactory() {
            public ParseQuery<ParseObject> create() {
                ParseQuery query = ParseQuery.getQuery("Posts");
                query.orderByDescending("title");
                return query;
            }
        });

//        final CustomQueryAdapter mainAdapter = new CustomQueryAdapter(this, DefaultViewHolder.class
//                ,new ParseRecyclerQueryAdapter.QueryFactory() {
//            public ParseQuery<ParseObject> create() {
//                ParseQuery query = ParseQuery.getQuery("Posts");
//                query.orderByDescending("title");
//                return query;
//            }
//        });


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.items);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mainAdapter);
    }

}
