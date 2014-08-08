package com.android.mendeleypaperreader;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Classname: DetailsActivity 
 * 	This activity displays the details using a MainMenuActivityFragmentDetails. This activity is started
 * 	by a MainMenuActivityFragmentList when a title in the list is selected.
 * 	The activity is used only if a MainMenuActivityFragmentDetails is not on the screen.  
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */



public class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);


	//verify orientation permissions
	if(getResources().getBoolean(R.bool.portrait_only)){
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	else{

	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}


	if (savedInstanceState == null) {
	    // During initial setup, plug in the details fragment.
	    MainMenuActivityFragmentDetails details = new MainMenuActivityFragmentDetails();
	    details.setArguments(getIntent().getExtras());
	    getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
	}
    }
}



