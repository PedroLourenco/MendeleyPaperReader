package com.android.mendeleypaperreader.utl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Classname: ConnectionDetector 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(Context context){
	this.context = context;
    }

    public boolean isConnectingToInternet(){
	ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (connectivity != null) 
	{
	    NetworkInfo[] info = connectivity.getAllNetworkInfo();
	    if (info != null) 
		for (int i = 0; i < info.length; i++) 
		    if (info[i].getState() == NetworkInfo.State.CONNECTED)
		    {
			return true;
		    }

	}
	return false;
    }
}
