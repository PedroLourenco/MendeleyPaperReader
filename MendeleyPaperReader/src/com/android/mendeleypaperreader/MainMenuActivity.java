package com.android.mendeleypaperreader;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.LoadData;
import com.android.mendeleypaperreader.utl.SessionManager;


public class MainMenuActivity extends FragmentActivity
{

	//private static GetAccessToken jParser = new GetAccessToken();
	private long mLastPressedTime;
	private static final int PERIOD = 2000;
	private ProgressTask task=null;
	private static ProgressDialog dialog;
	// Session Manager Class
	private static SessionManager session;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = new SessionManager(getApplicationContext()); 
		//Start upload data from server	       
		
		String db_uploded_flag = session.LoadPreference("IS_DB_CREATED");
		if(!db_uploded_flag.equals("YES")){
			if (task==null) {
				task=new ProgressTask(this);
				task.execute();

			}else {
				task.attach(this);
				updateProgress(task.getProgress());
				Log.w(Globalconstant.TAG, "progress: " + task.getProgress());

				if (task.getProgress()>=100) {
					markAsDone();				}
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
		dialog.setMessage("Sync data... (" + progress + "%)");
	}

	void markAsDone() {
		dialog.dismiss();
		setContentView(R.layout.activity_main_menu);
	}

	void startDialog(){
		dialog = new ProgressDialog(MainMenuActivity.this );
		dialog.setMessage("Sync data... ( 0% )");
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
	    inflater.inflate(R.menu.main_menu_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	//ActionBar Menu Options 
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle item selection
			switch (item.getItemId()) {
			case R.id. menu_About:
				Intent i_about = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(i_about);
				return true;
				
			case R.id. menu_logout:
				session.deletePreferences();
				getApplicationContext().deleteDatabase("mendeley_library");
				finish();
				return true;		
			default:
				return super.onOptionsItemSelected(item);
			}
		}
	
	
	
	
	
	
	
//AsyncTask to download DATA from server

	static class ProgressTask extends AsyncTask<String, Integer, String> {
		MainMenuActivity activity=null;
		int progress=0;

		ProgressTask(MainMenuActivity activity) {
			attach(activity);
		}

		protected void onPreExecute() {
			Log.d(Globalconstant.TAG, "onPreExecute");
			activity.startDialog();
		}


		protected void onPostExecute(final String success) {


			if (activity==null) {
				if(Globalconstant.LOG)
					Log.w("RotationAsync", "onPostExecute() skipped -- no activity");
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
					Log.w("RotationAsync", "onProgressUpdate() skipped -- no activity");
			}
			else {

				progress = values[0];
				activity.updateProgress(progress);
			}
		}



		protected String doInBackground(final String... args) {

			String tokens = session.LoadPreference("access_token");
			LoadData load = new LoadData(activity, tokens);
			
			load.getProfileInformation(Globalconstant.get_profile+tokens);
			
			publishProgress((int) (1 / ((float) 3) * 100));
			load.GetUserLibrary(Globalconstant.get_user_library_url+tokens);
			publishProgress((int) (2 / ((float) 3) * 100));
			load.getFolders(Globalconstant.get_user_folders_url+tokens);
			publishProgress((int) (3 / ((float) 4) * 100));
			
			

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

}