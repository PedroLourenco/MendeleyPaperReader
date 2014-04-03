package com.android.mendeleypaperreader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;



public class Options extends Activity{
	
	 private Context context;
	    private static String url = "https://api-oauth2.mendeley.com/oapi/library?access_token=";


	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
	        new ProgressTask(Options.this).execute();
	        
	 }
	 
	 
	 private class ProgressTask extends AsyncTask<String, Void, Boolean> {
	        private ProgressDialog dialog;
	       
	       
	 
	        // private List<Message> messages;
	        public ProgressTask(Activity activity) {
	            
	            context = activity;
	            dialog = new ProgressDialog(context);
	        }
	 
	        private Context context;
	 
	        protected void onPreExecute() {
	            this.dialog.setMessage("Progress start");
	            this.dialog.show();
	        }
	 
	        
	        protected void onPostExecute(final Boolean success) {
	        	if (dialog.isShowing()) {
	              dialog.dismiss();
	            }
	        }
	 
	        protected Boolean doInBackground(final String... args) {
	 
	            //String accessToken = TokenStore.getString(getApplicationContext(), "oauth_token");
	        	//Log.d("GITHUBB", "!!!accessToken: " + accessToken );
	        	
	        	//String urls = "https://api-oauth2.mendeley.com/oapi/library?access_token="+accessToken ;
	        //	Log.d("GITHUBB", "URL::::" + urls );
	        	
	        	//JSONParser jParser = new JSONParser();
	            
	            // get JSON data from URL
	           // JSONArray json = jParser.getJSONFromUrl(urls);
	            	//System.out.println(json);
	            //Log.d("GITHUBB", json.toString() );
	            
	            return null;
	        }
	    }


	
	 
	 
	 

}
