package com.android.mendeleypaperreader;




import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.android.mendeleypaperreader.utl.GetAccessToken;
import com.android.mendeleypaperreader.utl.Globalconstant;


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
	  private GetAccessToken jParser = new GetAccessToken();
	  private Dialog auth_dialog;
	  
	 
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
	    }
	   
	    
	    
	    pref = getSharedPreferences("AppPref", MODE_PRIVATE);

	    
	    //Click on "Sign in" Button and make login 
	    auth = (Button)findViewById(R.id.auth);
	    auth.setOnClickListener(new View.OnClickListener() {
	      Dialog auth_dialog;
	      @Override 
	      public void onClick(View arg0) {

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
	                      jParser.savePreferences(getApplicationContext(), "Code",authCode, Globalconstant.shared_file_name);
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
	      }
	    });
	    
	    
	    //New Account Button
	    Button newAccount = (Button) findViewById(R.id.NewAccount);
	    newAccount.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
	                myWebLink.setData(Uri.parse("https://www.mendeley.com/join/?_section=header&_specific="));
	                startActivity(myWebLink);
	    	}
	         }
	     );
	    
	    
	    
	    
	    
	  }
	   private class TokenGet extends AsyncTask<String, String, JSONObject> {
	          private ProgressDialog pDialog;
	          String Code;
	         @Override
	         protected void onPreExecute() {
	             super.onPreExecute();
	             pDialog = new ProgressDialog(MainActivity.this);
	             pDialog.setMessage("Contacting Mendeley ...");
	             pDialog.setIndeterminate(false);
	             pDialog.setCancelable(true);
	             Code =  jParser.LoadPreference(getApplicationContext(), "Code", Globalconstant.shared_file_name);
	             pDialog.show();
	         }
	         @Override
	         protected JSONObject doInBackground(String... args) {
	             GetAccessToken jParser = new GetAccessToken();
	             JSONObject json = jParser.gettoken(TOKEN_URL,Code,CLIENT_ID,CLIENT_SECRET,REDIRECT_URI,GRANT_TYPE);
	             
	            
	             
	             
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
	            	   	jParser.savePreferences(getApplicationContext(), "access_token", json.getString("access_token"), Globalconstant.shared_file_name);
	                    
	            	   	if (Globalconstant.LOG){
	            	   		Log.d("Token Access", tok);
	            	   		Log.d("Expire", expire);
	            	   		Log.d("Refresh", refresh);
	            	   	}
	            	   	 //TODO - Se a autenticação tiver sucesso fazer o upload dos dados e abrir nova ativity
	            	   	
	            	   
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
	}
	   
	   
