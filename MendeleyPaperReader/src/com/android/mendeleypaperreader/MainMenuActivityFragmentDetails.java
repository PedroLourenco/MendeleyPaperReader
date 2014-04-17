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

import com.android.mendeleypaperreader.db.MendeleyDataSource;
import com.android.mendeleypaperreader.utl.CustomCursorAdapter;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.MyContentProvider;

public class MainMenuActivityFragmentDetails  extends Fragment  implements LoaderCallbacks<Cursor> {
	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
	//private MendeleyDataSource mendeleyDataSource;
	ListView mListView;
	CustomCursorAdapter wla;
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

       
        
        View view = inflater.inflate(R.layout.activity_main_menu_details, container, false);

        //mendeleyDataSource = new MendeleyDataSource(getActivity().getApplicationContext());
        //mendeleyDataSource.open();
      	
        ListView lv = (ListView) view.findViewById(R.id.listDetails);
               
        mAdapter = new SimpleCursorAdapter(getActivity(),
        		R.layout.list_row_all_doc,
                null,
                new String[] {"_id", Globalconstant.AUTHORS, "data"},
                new int[] { R.id.Doctitle , R.id.authors, R.id.data },0);
        
        lv.setAdapter(mAdapter);
        
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        
                
        return view;
        
    }

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri uri = MyContentProvider.CONTENT_URI_DOC_DETAILS;
		 return new CursorLoader(getActivity(), uri, null, null, null, null);
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
		if(isAdded()){
			getLoaderManager().restartLoader(0, null, this);
		}
		else{
			mAdapter.swapCursor(null);
		}
		
		
	}


 
   
    
}