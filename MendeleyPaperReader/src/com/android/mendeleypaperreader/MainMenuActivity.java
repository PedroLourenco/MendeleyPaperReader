package com.android.mendeleypaperreader;


import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


import com.android.mendeleypaperreader.db.MendeleyDataSource;
import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.LoadData;


public class MainMenuActivity extends FragmentActivity
{
	
	  private GetAccessToken jParser = new GetAccessToken();
	  private long mLastPressedTime;
	  private static final int PERIOD = 2000;
	  	
	  
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	      //verify orientation permissions
	        if(getResources().getBoolean(R.bool.portrait_only)){
	            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        }
	        
	        
	      //Start upload data from server	       
	     String db_uploded_flag = jParser.LoadPreference(getApplicationContext(), "BD_Uploded", Globalconstant.shared_file_name);
	        
	      
	        if(!db_uploded_flag.equals("YES")){
	        new ProgressTask().execute();	        	
	        }
	        else{
	        
	        	setContentView(R.layout.activity_main_menu);
	        }
	        
	        
	    
	 }
	 
	     // Exit APP when click back key twice
	    	@Override
	    	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

	    			switch (event.getAction()) {
	    			case KeyEvent.ACTION_DOWN:
	    				if (event.getDownTime() - mLastPressedTime < PERIOD) {
	    					
	    					jParser.deletePreferences(MainMenuActivity.this, "BD_Uploded", Globalconstant.shared_file_name);
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
	
	
	    	private class ProgressTask extends AsyncTask<String, Integer, String> {
	    	    private ProgressDialog dialog;
	    	   
	    	   

	    	    
	    	    public ProgressTask() {
	    	        
	    	    	
	    	    	dialog = new ProgressDialog(MainMenuActivity.this );
	    	    }

	    	  
	    	    protected void onPreExecute() {
	    	    	Log.d(Globalconstant.TAG, "onPreExecute");
	    	    	dialog.setMessage("Sync data");
	    	    	dialog.show();
	    	        
	    	       
	    	        
	    	    }

	    	    
	    	    protected void onPostExecute(final String success) {
	    	    	if (dialog.isShowing()) {
	    	    	   dialog.dismiss();
	    	    	}
	    	    	
	    	    	//Save Flag to control data upload
	    	    	 
	    	    	jParser.savePreferences(getApplicationContext(), "BD_Uploded", "YES", Globalconstant.shared_file_name);	    		   	
	    	    	 setContentView(R.layout.activity_main_menu);
	    	    	
	    	    	
	    	    }

	    	    @Override
	    	    protected void onProgressUpdate(final Integer... values) {
	    	    		   
	    	    	dialog.setMessage("Sync data... (" + values[0] + "%)");
	    	    }
	    	    
	    	    protected String doInBackground(final String... args) {

	    	       
	    	    	GetAccessToken token = new GetAccessToken();
	    			
	    			String tokens = token.LoadPreference(getApplicationContext(), "access_token", Globalconstant.shared_file_name);
	    			LoadData load = new LoadData(getApplicationContext(), tokens);
	    			publishProgress((int) (1 / ((float) 3) * 100));
	    			load.GetUserLibrary(Globalconstant.get_user_library_url+tokens);
	    			publishProgress((int) (2 / ((float) 3) * 100));
	    			load.getFolders(Globalconstant.get_user_folders_url+tokens);
	    			publishProgress((int) (3 / ((float) 3) * 100));
	    			
	    			if (Globalconstant.LOG)
	    				Log.d(Globalconstant.TAG, "Fim do Load Data");
	    			return null;
	    	       
	    	    }
	    	}	 


}