package com.javon.parseadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.javon.parserecyclerviewadapter.ParseRecyclerQueryAdapter;
import com.parse.ParseObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ParseRecyclerQueryAdapter<ParseObject,DefaultViewHolder> mainAdapter = new ParseRecyclerQueryAdapter<>(this, DefaultViewHolder.class
                ,"Posts"); // change this to your desired class name on parse
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.items);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mainAdapter);



    }

}
