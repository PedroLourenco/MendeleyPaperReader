package com.android.mendeleypaperreader;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.mendeleypaperreader.db.MendeleyDataSource;
import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;


public class MainMenuActivity extends FragmentActivity
{
	
	  private GetAccessToken jParser = new GetAccessToken();
	  private long mLastPressedTime;
	  private static final int PERIOD = 2000;
	  private MendeleyDataSource mendeleyDataSource;	
	  
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main_menu);
	        
	        mendeleyDataSource = new MendeleyDataSource(getApplicationContext());
			mendeleyDataSource.open();
	        
	        
	    
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
	
	

@Override
public void onDestroy() {
    super.onDestroy();
    
    if (Globalconstant.LOG)
		Log.d(Globalconstant.TAG,"onDestroy MAINMENUACTIVITY");
    
    if (mendeleyDataSource != null) {
    	mendeleyDataSource.close();
    }

}

}