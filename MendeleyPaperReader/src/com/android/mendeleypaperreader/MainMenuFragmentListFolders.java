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

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
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
        

            String[] dataColumns = {"_id"}; //column DatabaseOpenHelper.FOLDER_NAME
            int[] viewIDs = { R.id.title };
            mAdapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.list_row_with_image, null, dataColumns, viewIDs, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            setListAdapter(mAdapter);

         // Check to see if we have a frame in which to embed the details
            // fragment directly in the containing UI.
            View detailsFrame = getActivity().findViewById(R.id.details);
            mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

            if (savedInstanceState != null) {
                // Restore last state for checked position.
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }

            if (mDualPane) {
                // In dual-pane mode, the list view highlights the selected item.
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                // Make sure our UI is in the correct state.
                showDetails(mCurCheckPosition);
            }
      
    }



@Override 
public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	showDetails(position);
    	Toast.makeText(getActivity().getApplicationContext(), "Click on folder!!", Toast.LENGTH_SHORT).show();
       
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            MainMenuActivityFragmentDetails details = (MainMenuActivityFragmentDetails)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = MainMenuActivityFragmentDetails.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }


@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	
	
	String[] projection = {DatabaseOpenHelper.FOLDER_NAME + " as _id"}; 
	
	
	if (Globalconstant.LOG)
		Log.d(Globalconstant.TAG,"onCreateLoader  Folders");
	Uri uri = MyContentProvider.CONTENT_URI_FOLDERS;
    return new CursorLoader(getActivity().getApplicationContext(), uri, projection, null, null, null);
}



@Override
public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	mAdapter.swapCursor(cursor);
	if (Globalconstant.LOG)
		Log.d(Globalconstant.TAG,"onLoadFinished Folders");
	
  
}



@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	
	if (Globalconstant.LOG)
		Log.d(Globalconstant.TAG,"onLoaderReset  Folders");
	
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
    if (Globalconstant.LOG)
    	Log.d(Globalconstant.TAG,"onResume()   Folders");
    getLoaderManager().restartLoader(0x01, null, this);
} 




private class ProgressTask extends AsyncTask<String, Void, Boolean> {
    private ProgressDialog dialog;
   
   

    
    public ProgressTask() {
        
    	
    	dialog = new ProgressDialog(getActivity());
    }

  
    protected void onPreExecute() {
    	dialog.setMessage("Sync data");
    	dialog.show();
        
       
        
    }

    
    protected void onPostExecute(final Boolean success) {
    	if (dialog.isShowing()) {
    	   dialog.dismiss();
    	}
    	
    	//Save Flag to control data upload
    	 //Save access token in shared preferences
    	jParser.savePreferences(getActivity().getApplicationContext(), "BD_Uploded", "YES", Globalconstant.shared_file_name);
	   	
    	//update listview with folders name
    	onLoaderReset(null);
    	
    	
    }

    protected Boolean doInBackground(final String... args) {

       
    	GetAccessToken token = new GetAccessToken();
		
		String tokens = token.LoadPreference(getActivity().getApplicationContext(), "access_token", Globalconstant.shared_file_name);
		LoadData load = new LoadData(getActivity().getApplicationContext());
		load.GetUserLibrary(Globalconstant.get_user_library_url+tokens);
		load.getFolders(Globalconstant.get_user_folders_url+tokens);
		
		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "Fim do Load Data");
		return true;
       
    }
}	 
	 

}





