package com.android.mendeleypaperreader;




import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.mendeleypaperreader.utl.ConnectionDetector;
import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.SessionManager;


public class MainActivity extends Activity {
 
    
	private static String CLIENT_ID = "177";
	  //Use your own client id
	  private static String CLIENT_SECRET ="V!yw8[5_0ZliXK$0";
	  //Use your own client secret
	  private static String REDIRECT_URI="http://localhost";
	 
	  private static String GRANT_TYPE="authorization_code";
	  private static String TOKEN_URL ="https://api-oauth2.mendeley.com/oauth/token";
	  private static String OAUTH_URL ="https://api-oauth2.mendeley.com/oauth/authorize?";
	  private static String OAUTH_SCOPE="all";
	  //private GetAccessToken jParser = new GetAccessToken();
	  private Dialog auth_dialog;
	  private Boolean isInternetPresent = false;
	  // Session Manager Class
	  SessionManager session;
	 
	  WebView web;
	  Button auth;
	  SharedPreferences pref;
	  //TextView Access;
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.activity_main);

		  //verify orientation permissions
		  if(getResources().getBoolean(R.bool.portrait_only)){
			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		  }else{

			  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		  }

		  	// Session Manager
	        session = new SessionManager(MainActivity.this); 

		  //check internet connection
		  ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		  isInternetPresent = cd.isConnectingToInternet(); 


		  //Click on "Sign in" Button and make login 
		  auth = (Button)findViewById(R.id.auth);
		  auth.setOnClickListener(new View.OnClickListener() {
			  Dialog auth_dialog;
			  @Override 
			  public void onClick(View arg0) {


				  if (isInternetPresent){ 

					  auth_dialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
					  auth_dialog.setContentView(R.layout.webviewoauth);
					  web = (WebView)auth_dialog.findViewById(R.id.webview);
					  web.getSettings().setJavaScriptEnabled(true);
					  web.loadUrl(OAUTH_URL+"client_id="+CLIENT_ID +"&redirect_uri="+REDIRECT_URI+"&response_type=code&scope="+OAUTH_SCOPE);
					  web.setWebViewClient(new WebViewClient() {
						  boolean authComplete = false;
						  Intent resultIntent = new Intent();

						  @Override
						  public void onPageStarted(WebView view, String url, Bitmap favicon){
							  super.onPageStarted(view, url, favicon);
						  }
						  String authCode;
						  @Override
						  public void onPageFinished(WebView view, String url) {
							  super.onPageFinished(view, url);
							  if (url.contains("?code=") && authComplete != true) {
								  Uri uri = Uri.parse(url);
								  authCode = uri.getQueryParameter("code");

								  if (Globalconstant.LOG)
									  Log.i(Globalconstant.TAG, "CODE : " + authCode);
								  authComplete = true;
								  resultIntent.putExtra("code", authCode);
								  MainActivity.this.setResult(Activity.RESULT_OK, resultIntent);
								  setResult(Activity.RESULT_CANCELED, resultIntent);
								  
								  //save access code in shared preferences
								  session.savePreferences("Code",authCode);
								  auth_dialog.dismiss();
								  
								  new TokenGet().execute();

								  Toast.makeText(getApplicationContext(),"Authorization Code is: " +authCode, Toast.LENGTH_SHORT).show();
							  }else if(url.contains("error=access_denied")){

								  if (Globalconstant.LOG)
									  Log.i(Globalconstant.TAG, "ACCESS_DENIED_HERE");
								  resultIntent.putExtra("code", authCode);
								  authComplete = true;
								  setResult(Activity.RESULT_CANCELED, resultIntent);
								  Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
								  //auth_dialog.dismiss();
							  }
						  }
					  });
					  auth_dialog.show();
					  auth_dialog.setCancelable(true);
				  }else{

					  Log.w(Globalconstant.TAG, "NO INTERNET");
					  showDialog();

				  }

			  }

		  });
	    
	    
		  //New Account Button
		  Button newAccount = (Button) findViewById(R.id.NewAccount);
		  newAccount.setOnClickListener(new OnClickListener() {
			  public void onClick(View v) {

				  if(isInternetPresent){
					  Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
					  myWebLink.setData(Uri.parse("https://www.mendeley.com/join/?_section=header&_specific="));
					  startActivity(myWebLink);

				  }else{
					  showDialog();

				  }

			  }
		  }
				  );
	    
	    
	    
	    
	    
	  }
	   private class TokenGet extends AsyncTask<String, String, JSONObject> {
	          private ProgressDialog pDialog;
	          String code;
	         @Override
	         protected void onPreExecute() {
	             super.onPreExecute();
	             pDialog = new ProgressDialog(MainActivity.this);
	             pDialog.setMessage("Contacting Mendeley ...");
	             pDialog.setIndeterminate(false);
	             pDialog.setCancelable(true);
	             
	             code = session.LoadPreference("Code");  
	             pDialog.show();
	         }
	         @Override
	         protected JSONObject doInBackground(String... args) {
	             GetAccessToken jParser = new GetAccessToken();
	             JSONObject json = jParser.gettoken(TOKEN_URL,code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
	             
	            
	             return json;
	         }
	          @Override
	          protected void onPostExecute(JSONObject json) {
	              pDialog.dismiss();
	              if (json != null){
	               try {
	            	   	String tok = json.getString("access_token");
	            	   	String expire = json.getString("expires_in");
	            	   	String refresh = json.getString("refresh_token");
	            	   	
	            	    //Save access token in shared preferences
	            	   
	                    session.savePreferences("access_token", json.getString("access_token"));
	            	   	
	            	   	if (Globalconstant.LOG){
	            	   		Log.d("Token Access", tok);
	            	   		Log.d("Expire", expire);
	            	   		Log.d("Refresh", refresh);
	            	   	}
	            	   	
	            	   
	            	   	if(!tok.isEmpty()){
	            	   		
	            	   		
	            	   		Intent options = new Intent(getApplicationContext(), MainMenuActivity.class);
	            	   		startActivity(options);	            	   		
	            	   	}
	            	   	
	            	              	   	
	            	   
	          } catch (JSONException e) {
	            
	            e.printStackTrace();
	          }
	                    }else{
	               Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
	               pDialog.dismiss();
	           }
	          }
	     }
	   
	   
	   
	   public void onDestroy() {
		   super.onDestroy();
		   Log.d(Globalconstant.TAG,"ON DESTROY MAIN ACTIVITY ");
		   
		   if (auth_dialog != null) {
			   auth_dialog.dismiss();
			   auth_dialog = null;
		   }
		   
		   
	   }
	   
	   
	   
	   
	   public void showDialog() {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	        builder.setTitle("No network connection");
	        builder.setMessage("Please chek your internet connection")
	               .setPositiveButton("WIFI Settings", new DialogInterface.OnClickListener() {
	            	   public void onClick(DialogInterface dialog, int which) {
							
	            		   // Activity transfer to wifi settings
	                       startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
	                   
	            		   }
	               });
	        
	        builder.setNeutralButton("3G Settings", new DialogInterface.OnClickListener() {
         	   public void onClick(DialogInterface dialog, int which) {
						
         		// Activity transfer to wifi settings
                   //startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
         		  Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
					MainActivity.this.startActivity(intent);
               }
            });
	               // on pressing cancel button
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
	        // show dialog
	        builder.show();
	    }
	}
	   
	   
