package com.android.mendeleypaperreader;

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.MyContentProvider;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DocumentsDetailsActivity extends Activity  {
	
	Cursor mAdapter;
	private CursorLoader mcursor;

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_documents_details);
	   
	   
	    Log.d(Globalconstant.TAG, "DOC_DETAILS - doc_id: " + getDocId());
	    
	    /*String[] dataColumns = {"_id", Globalconstant.TITLE,Globalconstant.AUTHORS, Globalconstant.SOURCE, "year", Globalconstant.ABSTRACT, Globalconstant.WEBSITE}; 
        int[] viewIDs = { R.id.docype ,R.id.detailTitle, R.id.authors, R.id.source, R.id.year, R.id.abstracts,R.id.urls};
        mAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list_row_doc_details, null, dataColumns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
      */
        
	    
	    fillData(getdocDetails());
	}
	
	
	
	
	private String getDocId(){
		
	
		Bundle bundle = getIntent().getExtras();
		
		String doc_id = bundle.getString("doc_id");
		
		return doc_id;
	}



	
	private Cursor getdocDetails(){
		
		Log.d(Globalconstant.TAG, "getdocDetails - DOC_DETAILS");
		String doc_id = getDocId();
		
		String[] projection = null;
		String selection = null;
		
		projection = new String[] {DatabaseOpenHelper.TYPE + " as _id",  DatabaseOpenHelper.TITLE, DatabaseOpenHelper.AUTHORS, DatabaseOpenHelper.SOURCE , DatabaseOpenHelper.YEAR, DatabaseOpenHelper.VOLUME, DatabaseOpenHelper.PAGES,  DatabaseOpenHelper.ABSTRACT, DatabaseOpenHelper.WEBSITE };
		selection = DatabaseOpenHelper._ID + " = '" + doc_id +"'";
		Uri  uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");
		
		 Log.d(Globalconstant.TAG, "onCreateLoader - DOC_DETAILS");
		 
		  return getApplicationContext().getContentResolver().query(uri, projection, selection, null, null);
		
	}
	
	
	private void fillData(Cursor cursor){
		
		 Log.d(Globalconstant.TAG, "fillData - DOC_DETAILS - " + cursor.getCount());
		
		
		cursor.moveToPosition(0);
			TextView type = (TextView) findViewById(R.id.docype);
			type.setText(cursor.getString(cursor.getColumnIndex("_id")));
			
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TITLE)));	
			
			TextView authors = (TextView) findViewById(R.id.authors);
			authors.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.AUTHORS)));
			
			TextView source = (TextView) findViewById(R.id.source);
			source.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SOURCE)));
			
			TextView year = (TextView) findViewById(R.id.year);
			String aux_year = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.YEAR));
			String aux_volume = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.VOLUME));
			String aux_pages = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PAGES));
			year.setText("Vol." + aux_volume + " pp("+ aux_pages + ") " + aux_year);
			
			TextView abstracts = (TextView) findViewById(R.id.abstracts);
			abstracts.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ABSTRACT)));
			
			TextView urls = (TextView) findViewById(R.id.url);
			urls.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.WEBSITE)));
			
			
		}
		
		
	
	
	
	
	

}
