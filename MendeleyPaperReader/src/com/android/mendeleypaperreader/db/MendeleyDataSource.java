package com.android.mendeleypaperreader.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.mendeleypaperreader.utl.Globalconstant;


/**
 * @author Pedro Lourenco
 * 
 */

public class MendeleyDataSource {



	private SQLiteDatabase db;
	private DatabaseOpenHelper mendeley_library;

	
	
	public MendeleyDataSource(Context context) {
		mendeley_library = new DatabaseOpenHelper(context);	
	}


	public void open() throws SQLException {

		db = mendeley_library.getWritableDatabase();
	}

	public void close() {
		
		Log.d(Globalconstant.TAG, "DATABASE CLOSED!!!");
		mendeley_library.close();
	}
	

	
	

	
	}