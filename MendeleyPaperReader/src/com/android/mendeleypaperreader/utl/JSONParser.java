package com.android.mendeleypaperreader.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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

	List<String> jsonArray = new ArrayList<String>();
	List<InputStream> jacksonArray = new ArrayList<InputStream>();
	Map<InputStream, String> myMap = new HashMap<InputStream, String>();

	// constructor
	public JSONParser() {
	}
	public List<String> getJSONFromUrl(String url, boolean with_header) {
		// Making HTTP request

		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "url: " + url);

		//InputStream content = null;	
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
				content.close();
				jsonArray.add(builder.toString());


				if(with_header){
					String link = header(response.getHeaders("Link"));
					jsonArray.add(link);
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

		return jsonArray;
	}



	public String header(org.apache.http.Header[] header){

		String aux = null;

		if(header.length > 0){

			if(header[0].toString().contains("rel=\"next\"")){

				Pattern pattern = Pattern.compile("\\<(.+?)\\>");
				Matcher matcher = pattern.matcher(header[0].toString());


				while (matcher.find()) {
					
					aux = matcher.group(1);
				}
				return aux;
			}
		}

		return "meu";

	}


	public void header2(org.apache.http.Header[] header){

		String aux = null;

		if(header.length > 0){

			if(header[0].toString().contains("rel=\"next\"")){

				Pattern pattern = Pattern.compile("\\<(.+?)\\>");
				Matcher matcher = pattern.matcher(header[0].toString());

				while (matcher.find()) {

					aux = matcher.group(1);
					getJACKSONFromUrl(aux, true);

				}
			}
		}

	}



	public List<InputStream> getJACKSONFromUrl(String url, boolean with_header) {
		// Making HTTP request

		if(url.equals("meu")){
			Log.d(Globalconstant.TAG, "url: " + jacksonArray.size());
			return jacksonArray;
		}

		//InputStream content = null;	
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				jacksonArray.add(entity.getContent());
				
				if(with_header){
					String link = header(response.getHeaders("Link"));
					getJACKSONFromUrl(link, true);
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
		
		return jacksonArray;
	}



}