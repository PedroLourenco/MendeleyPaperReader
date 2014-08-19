package com.android.mendeleypaperreader.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;import org.json.JSONObject;

import android.util.Log;

/**
 * Classname: JSONParser 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

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
				
				//org.apache.http.Header[] headers = response.getHeaders("Link");
				org.apache.http.Header[] headers = response.getAllHeaders();
				//org.apache.http.Header headers_link = response.getFirstHeader("Link");
				//Log.e(Globalconstant.TAG, "header_link: " + headers_link);
				//header(headers_link);
				
				//Log.e(Globalconstant.TAG, "headers.length: " + headers.length);
				for (int i = 0; i < headers.length; i++) {
		            
		        	//String str = headers[i].toString();
					headers[i].
		        	String str = "ZZZZL <%= dsn %> AFFF <%= AFG %>";
		        	//Pattern pattern = Pattern.compile("\<(.*?\)>");
		        	//Log.e(Globalconstant.TAG, "HEADER: " + headers[i].toString());
		        	
		        	
		        	Pattern pattern = Pattern.compile("\\<(.+?)\\>");
		            Matcher matcher = pattern.matcher(str);

		            boolean found = false;
		            while (matcher.find()) {
		                System.out.println("I found the text " + matcher.group(1)
		                        + " starting at " + "index " + matcher.start()
		                        + " and ending at index " +
		                        matcher.end());
		                found = true;
		                getJSONFromUrl(matcher.group(1));
		            }
		            if (!found) {
		                System.out.println("No match found");
		            }
		        	
		        	
		        	
		        	
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
			Log.e(Globalconstant.TAG, "builder.toString(): " +builder.toString());
		Log.e(Globalconstant.TAG, "builder.toString().lenght: " +builder.length());


		return builder.toString();
	}
	
	
	
	public void header(org.apache.http.Header header){
		
		//Log.e(Globalconstant.TAG, "header_link: " + header);
		String aux = header.toString();
		if(header.equals("")){
			Log.e(Globalconstant.TAG, "Next page! ");
			
		}
			
			
		
		if(header.toString().indexOf("next") > 0){
			Log.e(Globalconstant.TAG, "header_link: " + header);
			Log.e(Globalconstant.TAG, "Next page! ");
			
		}
	}
		
	
	
	
}