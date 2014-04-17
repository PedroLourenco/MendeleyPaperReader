package com.android.mendeleypaperreader;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.LoadData;
import com.android.mendeleypaperreader.utl.MyContentProvider;


public class MainMenuFragmentListFolders extends ListFragment implements LoaderCallbacks<Cursor>{

	
	boolean mDualPane;
    int mCurCheckPosition = 0;
        
    ListView list;
    SimpleCursorAdapter mAdapter;
    private GetAccessToken jParser = new GetAccessToken();
       
    
 
    

@Override 
public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

      //Start upload data from server	       
        String db_uploded_flag = jParser.LoadPreference(getActivity().getApplicationContext(), "BD_Uploded", Globalconstant.shared_file_name);
        
        if(!db_uploded_flag.equals("YES")){
        	new ProgressTask().execute();	        	
        }
        
        
        String[] dataColumns = {"_id"};
        int[] viewIDs = { R.id.title };
        mAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.list_row_with_image, null, dataColumns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(mAdapter);
      
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
       

    }



@Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	
    	Toast.makeText(getActivity().getApplicationContext(), "Click on folder!!", Toast.LENGTH_SHORT).show();
       
    }

    


@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	Log.d(Globalconstant.TAG,"onCreateLoader");
	Uri uri = MyContentProvider.CONTENT_URI_FOLDERS;
    return new CursorLoader(getActivity().getApplicationContext(), uri, null, null, null, null);
}



@Override
public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	
	Log.d(Globalconstant.TAG,"onLoadFinished");
	mAdapter.swapCursor(cursor);
  
}



@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	Log.d(Globalconstant.TAG,"onLoaderReset");
	
	if(isAdded()){
		getLoaderManager().restartLoader(0, null, this);
	}
	else{
		mAdapter.swapCursor(null);
	}	
	
}

public void onResume() {
    super.onResume();
    // Restart loader so that it refreshes displayed items according to database
    Log.d(Globalconstant.TAG,"onResume()");
    getLoaderManager().restartLoader(0x01, null, this);
} 




private class ProgressTask extends AsyncTask<String, Void, Boolean> {
    private ProgressDialog dialog;
   
   

    // private List<Message> messages;
    public ProgressTask() {
        
    	
    	//dialog = new ProgressDialog(getActivity().getApplicationContext());
    }

  
    protected void onPreExecute() {
    	//dialog.setMessage("Sync data");
    	//dialog.show();
        
       
        
    }

    
    protected void onPostExecute(final Boolean success) {
    	//if (dialog.isShowing()) {
    	// dialog.dismiss();
    	//}
    	
    	//Save Flag to control data upload
    	 //Save access token in shared preferences
	   	
    	if(isAdded()){
            getResources().getString(R.string.app_name);
        }
    	
    	jParser.savePreferences(getActivity().getApplicationContext(), "BD_Uploded", "YES", Globalconstant.shared_file_name);
	   	onLoaderReset(null);
	    
	   	
    	
    }

    protected Boolean doInBackground(final String... args) {

       
    	GetAccessToken token = new GetAccessToken();
		
		String tokens = token.LoadPreference(getActivity().getApplicationContext(), "access_token", Globalconstant.shared_file_name);
		LoadData load = new LoadData(getActivity().getApplicationContext());
		load.GetUserLibrary(Globalconstant.get_user_library_url+tokens);
		load.getFolders(Globalconstant.get_user_folders_url+tokens);
		
		Log.d(Globalconstant.TAG, "Fim do Load Data");
		return true;
       
    }
}	 
	 

}





