package com.android.mendeleypaperreader.utl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.util.Log;
import com.android.mendeleypaperreader.R;


public class SyncDataAsync extends AsyncTask<String,Integer,String> {

    Context context;
    Activity activity;
    private static LoadData load;
    ProgressDialog dialog;
    private static SessionManager session;


    public SyncDataAsync(Context context, Activity activity) 
    {   
	this.context = context;
	this.activity = activity;
	load = new LoadData(this.context.getApplicationContext());
	dialog = new ProgressDialog(context);	
    }




    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	lockScreenOrientation();
	dialog.setIndeterminate(true);
	dialog.setCancelable(false);
	dialog.show();

    }


    @Override
    protected void onProgressUpdate(Integer... values) {

	dialog.setMessage(this.context.getResources().getString(R.string.sync_data) + values[0] + "%)");
    } 



    @Override
    protected  String doInBackground(String... arg0) {

	syncronizeData();

	if (Globalconstant.LOG)
	    Log.d(Globalconstant.TAG, "Fim do Load Data");


	return null;
    }


    @Override
    protected void onPostExecute(String json) {
	if(dialog.isShowing())
	    dialog.dismiss();	    
	session = new SessionManager(this.context); 
	session.savePreferences("IS_DB_CREATED", "YES");
	unlockScreenOrientation();
    }

    protected void updateProgress(int progress) {
	dialog.setMessage(this.context.getResources().getString(R.string.sync_data) + progress + "%)");
    }


    private void syncronizeData(){

	publishProgress((int) (1 / ((float) 4) * 100));
	load.getProfileInformation(Globalconstant.get_profile);
	publishProgress((int) (2 / ((float) 4) * 100));
	load.GetUserLibrary(Globalconstant.get_user_library_url);
	//publishProgress((int) (3 / ((float) 4) * 100));
	//load.getFolders(Globalconstant.get_user_folders_url);
	//publishProgress((int) (4 / ((float) 3.99) * 100));

    }
    
    
    private void lockScreenOrientation() {
        int currentOrientation = this.context.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
        	this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
        	 this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
     
    private void unlockScreenOrientation() {
    	this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
    


}
