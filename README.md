# ParseRecyclerViewAdapter

A version of ParseQueryAdapter for RecyclerView.
Load data directly from parse into the RecyclerView using the convenience methods provided similar to those in ParseQueryAdapter.

## Installing

There are two ways to install ParseRecyclerViewAdapter:

#### As a Gradle dependency

Simply add:

```groovy
compile 'com.javon.parserecyclerviewadapter:parserecyclerviewadapter:1.0.2'
```

to your project dependencies and run `gradle build` or `gradle assemble`.

#### As a library project

Download the source code and import it as a library project in Eclipse. The project is available in the folder **library**. For more information on how to do this, read [here](http://developer.android.com/tools/projects/index.html#LibraryProjects).

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

That's it! The data will be loaded from parse into the RecyclerView.

##Customizations

A full walkthrough of how to use the adapter and customizations will be added.

## Contributing

Please fork this repository and contribute back using [pull requests](https://github.com/JA-VON/ParseRecyclerViewAdapter/pulls). Features can be requested using [issues](https://github.com/JA-VON/ParseRecyclerViewAdapter/issues). All code, comments, and critiques are greatly appreciated.
