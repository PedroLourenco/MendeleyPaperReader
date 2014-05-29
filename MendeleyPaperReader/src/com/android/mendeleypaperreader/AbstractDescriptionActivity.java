package com.android.mendeleypaperreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.mendeleypaperreader.utl.Globalconstant;

public class AbstractDescriptionActivity extends Activity{
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_abstract_description);
	   
	   if(Globalconstant.LOG)
		   Log.d(Globalconstant.TAG, "DOC_DETAILS - Abstract: " + getAbstract());
	  
	    TextView v_abstract = (TextView) findViewById(R.id.abstractDescription);
	    v_abstract.setText(getAbstract());
	
	}
	
	
	
	private String getAbstract(){
		
		
		Bundle bundle = getIntent().getExtras();
		
		String v_abstract = bundle.getString("abstract");
		
		return v_abstract;
	}

	
	
	

}
