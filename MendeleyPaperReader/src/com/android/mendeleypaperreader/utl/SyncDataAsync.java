package com.android.mendeleypaperreader.utl;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.util.Log;
import com.android.mendeleypaperreader.R;
import com.fasterxml.jackson.core.JsonProcessingException;


public class SyncDataAsync extends AsyncTask<String,Integer,String> {

    Context context;
    Activity activity;
    private static LoadData load;
    ProgressDialog dialog;
    private static SessionManager session;
    String access_token;


    public SyncDataAsync(Context context, Activity activity) 
    {   
	this.context = context;
	this.activity = activity;
	load = new LoadData(this.context.getApplicationContext());
	dialog = new ProgressDialog(context);
	session = new SessionManager(this.context); 
	session.savePreferences("IS_DB_CREATED", "YES");
	 access_token = session.LoadPreference("access_token");
	
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

	try {
		syncronizeData();
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	if (Globalconstant.LOG)
	    Log.d(Globalconstant.TAG, "Fim do Load Data");


	return null;
    }


    @Override
    protected void onPostExecute(String json) {
	if(dialog.isShowing())
	    dialog.dismiss();	    
	
	unlockScreenOrientation();
    }

    protected void updateProgress(int progress) {
	dialog.setMessage(this.context.getResources().getString(R.string.sync_data) + progress + "%)");
    }


    private void syncronizeData() throws JsonProcessingException, IOException{

	publishProgress((int) (1 / ((float) 4) * 100));
	load.getProfileInfo(Globalconstant.get_profile + access_token);
	publishProgress((int) (2 / ((float) 4) * 100));
	Log.d(Globalconstant.TAG, "access_token: " + access_token);
	load.getUserLibrary(Globalconstant.get_user_library_url + access_token);
	publishProgress((int) (3 / ((float) 4) * 100));
	load.getFolders(Globalconstant.get_user_folders_url + access_token);
	publishProgress((int) (4 / ((float) 3.99) * 100));
	

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
