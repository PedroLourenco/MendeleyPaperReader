package com.android.mendeleypaperreader.utl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.android.mendeleypaperreader.R;


public class SyncDataAsync extends AsyncTask<String,Integer,String> {

    Context context;
    private static LoadData load;
    ProgressDialog dialog;


    public SyncDataAsync(Context context) 
    {   
	this.context = context;
	load = new LoadData(this.context.getApplicationContext());
	dialog = new ProgressDialog(context);	
    }




    @Override
    protected void onPreExecute() {
	super.onPreExecute();

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

    }

    protected void updateProgress(int progress) {
	dialog.setMessage(this.context.getResources().getString(R.string.sync_data) + progress + "%)");
    }


    private void syncronizeData(){

	publishProgress((int) (1 / ((float) 4) * 100));
	load.getProfileInformation(Globalconstant.get_profile);
	publishProgress((int) (2 / ((float) 4) * 100));
	load.GetUserLibrary(Globalconstant.get_user_library_url);
	publishProgress((int) (3 / ((float) 4) * 100));
	load.getFolders(Globalconstant.get_user_folders_url);
	publishProgress((int) (4 / ((float) 3.99) * 100));

    }


}
