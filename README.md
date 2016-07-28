# ParseRecyclerViewAdapter

A version of ParseQueryAdapter for RecyclerView.
Load data directly from a Parse backend into the RecyclerView using the convenience methods provided similar to those in ParseQueryAdapter.

## Installing

There are two ways to install ParseRecyclerViewAdapter:

#### As a Gradle dependency

Simply add:

```groovy
compile 'com.javon.parserecyclerviewadapter:parserecyclerviewadapter:1.0.7'
```

to your project dependencies and run `gradle build` or `gradle assemble`.

#### As a library project

Download the source code and import it as a library project in Eclipse. The project is available in the folder **parserecyclerviewadapter**. For more information on how to do this, read [here](http://developer.android.com/tools/projects/index.html#LibraryProjects).

## Usage

First you have to create a ViewHolder class that extends RecyclerView.ViewHolder. This class must specify its layout by attaching the @Layout annotation to the class definition and specify the name of field of the Parse class you want the attached to the specific View of the holder using the @ParseName annotation. See an example below.

```Java
package com.javon.example;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.javon.parserecyclerviewadapter.annotations.Layout;
import com.javon.parserecyclerviewadapter.annotations.ParseName;
import com.parse.ParseImageView;

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
```

Then simply specify that ViewHolder along with ParseObject or a child of this class as Type Parameter to the ParseRecyclerQueryAdapter. Within the constructor you will need to pass in a context, the class of the ViewHolder and the name of the Class on Parse you wish to use.

```Java
ParseRecyclerQueryAdapter<ParseObject,DefaultViewHolder> adapter = new ParseRecyclerQueryAdapter<>(this, DefaultViewHolder.class,"ParseClass");
```

Then set the adapter to the RecyclerView and that's it! The data will be loaded from parse into the RecyclerView.

##Customizations

By default the query sent out just fetches the data from parse and returns them ordered by the date they were created. If you have more specific needs similarly to the ParseQueryAdapter you need to pass in a QueryFactory, which returns your specific ParseQuery. Example below.

```Java
ParseRecyclerQueryAdapter<ParseObject,DefaultViewHolder> adapter = new ParseRecyclerQueryAdapter<>(this, DefaultViewHolder.class,new ParseRecyclerQueryAdapter.QueryFactory() {
            public ParseQuery<ParseObject> create() {
            //build query here
                ParseQuery query = ParseQuery.getQuery("Posts");
                query.orderByDescending("title");
                return query;
            }
        });
```

By default when you use the tag to bind the field to the field on Parse it will simply either set the text or set the ParseImageView, if you have more specific needs you can create an adapter that extends the ParseRecyclerQueryAdater and override the OnBindViewHolder method. Within this method you'll have access to a number of fields from the parent which include objects, which contains a Java List of the ParseObjects and object which will be the current object. See below for an example of a custom adapter using an extra field called staticTitle which has been added to the DefaultViewHolder class.

```Java
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
```
The example module contains an application which uses the above mentioned custom adapter, it can be built out for a working example of the adapter.

## Contributing

Please fork this repository and contribute back using [pull requests](https://github.com/JA-VON/ParseRecyclerViewAdapter/pulls). Features can be requested using [issues](https://github.com/JA-VON/ParseRecyclerViewAdapter/issues). All code, comments, and critiques are greatly appreciated.
