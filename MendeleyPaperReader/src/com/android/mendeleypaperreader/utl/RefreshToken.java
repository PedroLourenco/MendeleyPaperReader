package com.android.mendeleypaperreader.utl;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.mendeleypaperreader.MainActivity;
import com.android.mendeleypaperreader.MainMenuActivity;
import com.android.mendeleypaperreader.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;




public class RefreshToken extends AsyncTask<String, String, JSONObject> {

    SessionManager session;
    Context context;
    
    private static String CLIENT_ID = "177";
    // Use your own client id
    private static String CLIENT_SECRET = "V!yw8[5_0ZliXK$0";
    // Use your own client secret
    private static String REDIRECT_URI = "http://localhost";

    private static String GRANT_TYPE = "refresh_token";
    private static String TOKEN_URL = "https://api-oauth2.mendeley.com/oauth/token";
    String code;
    String refresh_token;
    public RefreshToken(Context context) 
    {   
	this.context = context;
    }
    
    
    
    
    @Override
	protected void onPreExecute() {
	    super.onPreExecute();
	   
	   
	    
	 // Session Manager
	    session = new SessionManager(this.context.getApplicationContext());

	    code = session.LoadPreference("Code");
	    refresh_token = session.LoadPreference("refresh_token");
	    
	    Log.d("refresh_token",  "refresh_token: " + refresh_token);
	    
	    
	}
    
    
    
    
    
    
    
    @Override
    protected  JSONObject doInBackground(String... arg0) {
	 GetAccessToken jParser = new GetAccessToken();
	    
	    JSONObject json = jParser.refresh_token(TOKEN_URL, code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE, refresh_token);
	    
	    return json;
    }
    
    
    @Override
	protected void onPostExecute(JSONObject json) {
	   
	    if (json != null) {
		try {
		    String token = json.getString("access_token");
		    String expire = json.getString("expires_in");
		    String refresh = json.getString("refresh_token");

		    // Save access token in shared preferences
		    session.savePreferences("access_token", json.getString("access_token"));
		    session.savePreferences("expires_in", json.getString("expires_in"));
		    session.savePreferences("refresh_token", json.getString("refresh_token"));
		    
		    if (Globalconstant.LOG) {
			Log.d("Token Access", token);
			Log.d("Expire", expire);
			Log.d("Refresh", refresh);
			
		    }

		} catch (JSONException e) {
		    e.printStackTrace();
		}
	    } else {
		Toast.makeText(this.context.getApplicationContext(), this.context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
		
	    }
	} 
    
}
