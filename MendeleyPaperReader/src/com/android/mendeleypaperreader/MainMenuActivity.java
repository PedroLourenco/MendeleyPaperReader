package com.android.mendeleypaperreader;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.mendeleypaperreader.DetailsActivity.ProgressTask;
import com.android.mendeleypaperreader.utl.ConnectionDetector;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.LoadData;
import com.android.mendeleypaperreader.utl.MyContentProvider;
import com.android.mendeleypaperreader.utl.SessionManager;
import com.android.mendeleypaperreader.utl.SyncDataAsync;

/**
 * Classname: MainMenuActivity 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

public class MainMenuActivity extends FragmentActivity
{

    private long mLastPressedTime;
    private static final int PERIOD = 2000;
    private ProgressTask task=null;
    private static ProgressDialog dialog;
    // Session Manager Class
    private static SessionManager session;
    private Boolean isInternetPresent = false;
    private String db_uploded_flag ;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	

	session = new SessionManager(getApplicationContext()); 

	//Start upload data from server	       
	 db_uploded_flag = session.LoadPreference("IS_DB_CREATED");
	if(!db_uploded_flag.equals("YES")){
		if (task==null) {
			task=new ProgressTask(this);
			task.execute();

		}else {
			task.attach(this);
			updateProgress(task.getProgress());

			if (task.getProgress()>=100) {
				markAsDone();				
			}
		}

	}else{
		setContentView(R.layout.activity_main_menu);
	}
    }



    public Object onRetainCusObjectNonConfigurationInstance() {
    	task.detach();
    	return(task);
    }

    void updateProgress(int progress) {
    	dialog.setMessage(getResources().getString(R.string.sync_data) + progress + "%)");
    }

    void markAsDone() {
    	dialog.dismiss();
    	
    }

    void startDialog(){
    	dialog = new ProgressDialog(MainMenuActivity.this );
    	dialog.setCanceledOnTouchOutside(false);
    	dialog.setCancelable(false);
    	dialog.setMessage(getResources().getString(R.string.sync_data_0));
    	dialog.show();
    }



    // Exit APP when click back key twice
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

	    switch (event.getAction()) {
	    case KeyEvent.ACTION_DOWN:
		if (event.getDownTime() - mLastPressedTime < PERIOD) {
		    finish();

		} else {

		    Toast.makeText(getApplicationContext(),
			    getResources().getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();
		    mLastPressedTime = event.getEventTime();
		}
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	//inflater.inflate(R.menu.main_menu_activity_actions, menu);
	inflater.inflate(R.menu.main_menu_activity_actions, menu);
	
	return super.onCreateOptionsMenu(menu);
    }

    //ActionBar Menu Options 
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
    	Log.w(Globalconstant.TAG, "MENU REFRESH: " + item.getItemId());
    	
    	
    	switch (item.getItemId()) {
	case R.id.menu_About:
	    Intent i_about = new Intent(getApplicationContext(), AboutActivity.class);
	    startActivity(i_about);
	    return true;

	case R.id.menu_logout:
	    showDialog();
	    return true;		
	case R.id.menu_refresh :
		
		Log.w(Globalconstant.TAG, "MENU REFRESH: " + item.getItemId());
		refreshToken();
	    return true;	
	default:
	    return super.onOptionsItemSelected(item);
	}
    }





    /*
     * Show dialog to prompt user when logout button is pressed
     */

    public void showDialog() {
	// Use the Builder class for convenient dialog construction
	AlertDialog.Builder builder = new AlertDialog.Builder(
		MainMenuActivity.this);
	builder.setTitle(getResources().getString(R.string.log_out));
	builder.setMessage(getResources().getString(R.string.warning))
	.setPositiveButton(getResources().getString(R.string.word_ok), new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {

		session.deletePreferences();
		getContentResolver().delete(MyContentProvider.CONTENT_URI_DELETE_DATA_BASE,null, null);
		finish();

	    }
	});

	// on pressing cancel button
	builder.setNegativeButton(getResources().getString(R.string.cancel),
		new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
	    }
	});
	// show dialog
	builder.show();
    }		


    public void syncData(){

		new SyncDataAsync(MainMenuActivity.this, MainMenuActivity.this).execute();
	}


	private void refreshToken(){

		//delete data from data base and get new accesstoken to start sync
		
		// check internet connection
		ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

		isInternetPresent = connectionDetector.isConnectingToInternet();

		if(isInternetPresent){
			getContentResolver().delete(MyContentProvider.CONTENT_URI_DELETE_DATA_BASE,null, null);
			new ProgressTask(this).execute();
		}
		else{
			connectionDetector.showDialog(MainMenuActivity.this, ConnectionDetector.DEFAULT_DIALOG);
		}

	}


    //AsyncTask to download DATA from server

    class ProgressTask extends AsyncTask<String, Integer, String> {
	MainMenuActivity activity=null;
	int progress=0;

	ProgressTask(MainMenuActivity activity) {
	    attach(activity);
	}

	protected void onPreExecute() {
		lockScreenOrientation();
			activity.startDialog();
	}


	protected void onPostExecute(final String success) {
		unlockScreenOrientation();

	    if (activity==null) {
	    
		if(Globalconstant.LOG)
		    Log.w(Globalconstant.TAG, "RotationAsync - onPostExecute() skipped -- no activity");
	    }
	    else {
		//Save Flag to control data upload
		session.savePreferences("IS_DB_CREATED", "YES");
		activity.markAsDone();
	    }
	} 




	@Override
	protected void onProgressUpdate(final Integer... values) {

		
	    if (activity==null) {
	    	if(Globalconstant.LOG)
	    		Log.w(Globalconstant.TAG ,"RotationAsync - onProgressUpdate() skipped -- no activity");
	    }else {

	    	progress = values[0];
	    	activity.updateProgress(progress);
	    }
	}



	protected String doInBackground(final String... args) {

	   if(!db_uploded_flag.equals("YES")){
		   String access_token = session.LoadPreference("access_token");
		    Log.d(Globalconstant.TAG, "access_token: " + access_token);
		    LoadData load = new LoadData(activity);
		    publishProgress((int) (1 / ((float) 4) * 100));
		    load.getProfileInformation(Globalconstant.get_profile);
		    publishProgress((int) (2 / ((float) 4) * 100));
		    load.GetUserLibrary(Globalconstant.get_user_library_url);
		    publishProgress((int) (3 / ((float) 4) * 100));
		    load.getFolders(Globalconstant.get_user_folders_url);
		    publishProgress((int) (4 / ((float) 3.99) * 100));

	   }
	   else{
		   syncData();
	   }
		
		

	    if (Globalconstant.LOG)
		Log.d(Globalconstant.TAG, "Fim do Load Data");
	    return null;

	}



	void detach() {
	    activity=null;
	}

	void attach(MainMenuActivity activity) {
	    this.activity=activity;
	}

	int getProgress() {
	    return(progress);
	}

    }
    
    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
     
    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    

}