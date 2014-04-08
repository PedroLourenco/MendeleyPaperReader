package com.android.mendeleypaperreader.utl;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mendeleypaperreader.R;

public class CustomCursorAdapter extends CursorAdapter{

	
	 LayoutInflater inflater;
	    public CustomCursorAdapter(Context context, Cursor c) {
	    super(context, c);
	    inflater = LayoutInflater.from(context);
	    }
	     
	    @Override
	    public void bindView(View view, Context context, Cursor cursor) {
	    
	    	TextView doc_title = (TextView)view.findViewById(R.id.Doctitle);
	    	TextView authors = (TextView)view.findViewById(R.id.authors);
	    	TextView data = (TextView)view.findViewById(R.id.data);	     
	   
	    	doc_title.setText(cursor.getString(0));
	    	authors.setText(cursor.getString(1));
	    	data.setText(cursor.getString(2));
	    
	    }
	     
	    @Override
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {
	    	return inflater.inflate(R.layout.list_row_all_doc, parent, false);
	    }


}
