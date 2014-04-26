package com.android.mendeleypaperreader.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;import org.json.JSONObject;

import android.util.Log;
 
public class JSONParser {
 

		 
		static InputStream is = null;
		  static JSONObject jObj = null;
		  static String json = "";
		  // constructor
		  public JSONParser() {
		  }
		  public String getJSONFromUrl(String url) {
		    // Making HTTP request
			  
			  if (Globalconstant.LOG)
				  Log.d(Globalconstant.TAG, "url: " + url);
			  
			  StringBuilder builder = new StringBuilder();
	            HttpClient client = new DefaultHttpClient();
	            HttpGet httpGet = new HttpGet(url);
	            try {
	              HttpResponse response = client.execute(httpGet);
	              StatusLine statusLine = response.getStatusLine();
	              int statusCode = statusLine.getStatusCode();
	              
	              if (statusCode == 200) {
	                HttpEntity entity = response.getEntity();
	                InputStream content = entity.getContent();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	                String line;
	                while ((line = reader.readLine()) != null) {
	                  builder.append(line);
	                  
	                }
	              } else {
	            	  if (Globalconstant.LOG)
	            		  Log.e(Globalconstant.TAG, "Failed to download file");
	              }
	            } catch (ClientProtocolException e) {
	              e.printStackTrace();
	            } catch (IOException e) {
	              e.printStackTrace();
	            }

	            if (Globalconstant.LOG)
	            	Log.e(Globalconstant.TAG, "builder.toString()" +builder.toString());
		    
		   
		    return builder.toString();
		  }
		}