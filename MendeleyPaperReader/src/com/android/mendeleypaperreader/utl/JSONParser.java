package com.android.mendeleypaperreader.utl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    // constructor
    public JSONParser() {
    }
    public List<String> getJSONFromUrl(String url, boolean with_header) {
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
		jsonArray.add(builder.toString());
		
		if(with_header)
		    header(response.getHeaders("Link"));


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


	return jsonArray;
    }



    public void header(org.apache.http.Header[] header){

	//Log.e(Globalconstant.TAG, "header_link: " + header);
	String aux = null;

	if(header.length > 0){

	    Log.e(Globalconstant.TAG, "header_link: " + header[0]);

	    if(header[0].toString().contains("rel=\"next\"")){


		Log.e(Globalconstant.TAG, "Next page! ");

		Pattern pattern = Pattern.compile("\\<(.+?)\\>");
		Matcher matcher = pattern.matcher(header[0].toString());


		while (matcher.find()) {
		    System.out.println("I found the text " + matcher.group(1)
			    + " starting at " + "index " + matcher.start()
			    + " and ending at index " +
			    matcher.end());

		    aux = matcher.group(1);
		}
		getJSONFromUrl(aux, true);
	    }



	}



    }




}