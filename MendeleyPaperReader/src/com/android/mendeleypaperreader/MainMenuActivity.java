package com.android.mendeleypaperreader;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.LoadData;


public class MainMenuActivity extends FragmentActivity
{

	private static GetAccessToken jParser = new GetAccessToken();
	private long mLastPressedTime;
	private static final int PERIOD = 2000;
	private ProgressTask task=null;
	private static ProgressDialog dialog;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		//Start upload data from server	       
		String db_uploded_flag = jParser.LoadPreference(getApplicationContext(), "BD_Uploded", Globalconstant.shared_file_name);

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

					//jParser.deletePreferences(MainMenuActivity.this, "BD_Uploded", Globalconstant.shared_file_name);
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
				jParser.savePreferences(activity, "BD_Uploded", "YES", Globalconstant.shared_file_name);
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


			GetAccessToken token = new GetAccessToken();

			String tokens = token.LoadPreference(activity, "access_token", Globalconstant.shared_file_name);
			LoadData load = new LoadData(activity, tokens);
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