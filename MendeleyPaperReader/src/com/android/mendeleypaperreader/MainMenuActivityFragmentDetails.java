package com.android.mendeleypaperreader;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.MyContentProvider;

public class MainMenuActivityFragmentDetails  extends Fragment  implements LoaderCallbacks<Cursor> {
	
	
	ListView mListView;
	SimpleCursorAdapter mAdapter;
		
	public static MainMenuActivityFragmentDetails newInstance(int index) {
    	MainMenuActivityFragmentDetails f = new MainMenuActivityFragmentDetails();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

  
    
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        int index = getShownIndex();
        Log.d(Globalconstant.TAG,"index: " + index );	
        
        
        
        
        View view = inflater.inflate(R.layout.activity_main_menu_details, container, false);
      	
        ListView lv = (ListView) view.findViewById(R.id.listDetails);
  
        
        String[] dataColumns = {"_id", Globalconstant.AUTHORS, "data"}; 
        int[] viewIDs = { R.id.Doctitle ,R.id.authors, R.id.data };
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_row_all_doc, null, dataColumns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
      
        lv.setAdapter(mAdapter);
        
    	if (Globalconstant.LOG)
    		Log.d(Globalconstant.TAG,"onCreateView  Details");
        
    	getActivity().getSupportLoaderManager().initLoader(0, null, this);
    	
                
        return view;
        
    }
    
    
    public void onResume() {
        super.onResume();
        // Restart loader so that it refreshes displayed items according to database
        if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG,"onResume");
     
        
    } 
    
    

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		String[] projection = null;
		int index = getShownIndex();
		if (Globalconstant.LOG){
			Log.d(Globalconstant.TAG,"Loader  Details");
			Log.d(Globalconstant.TAG,"index: " + index );			
		}
    	
		 Uri uri = null;
		 
		if(getShownIndex() == 0) { //All doc
			
			projection = new String[] {DatabaseOpenHelper.TITLE + " as _id",  DatabaseOpenHelper.AUTHORS, DatabaseOpenHelper.SOURCE + "||" + DatabaseOpenHelper.YEAR + " as data"}; 
			uri = MyContentProvider.CONTENT_URI_DOC_DETAILS;
		}
		else if (getShownIndex() == 3){
			
			projection = new String[] {DatabaseOpenHelper.TITLE + " as _id",  DatabaseOpenHelper.AUTHORS, DatabaseOpenHelper.SOURCE + "||" + DatabaseOpenHelper.YEAR + " as data"};
			uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/true");
			
		}
		 
		 return new CursorLoader(getActivity().getApplicationContext(), uri, projection, null, null, null);
		
	}
	

	
	

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		
		if (Globalconstant.LOG)
    		Log.d(Globalconstant.TAG,"onLoadFinished  Details - count: " + cursor.getCount());
	
			mAdapter.changeCursor(cursor);
		if (Globalconstant.LOG)
    		Log.d(Globalconstant.TAG,"onLoadFinished  Details");

	}
	

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		if (Globalconstant.LOG)
    		Log.d(Globalconstant.TAG,"onLoaderReset  Details");
		if(isAdded()){
			getLoaderManager().restartLoader(0, null, this);
		}
		else{
			mAdapter.swapCursor(null);
		}
		
		if (Globalconstant.LOG)
    		Log.d(Globalconstant.TAG,"onLoaderReset  Details");
	}


 


   
    
}