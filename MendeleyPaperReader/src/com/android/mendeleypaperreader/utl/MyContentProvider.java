package com.android.mendeleypaperreader.utl;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
import com.android.mendeleypaperreader.db.MendeleyDataSource;

public class MyContentProvider extends ContentProvider {
	
	private DatabaseOpenHelper db_helper;
	//private MendeleyDataSource mendeleyDataSource;
	
	
	private static final String AUTHORITY = 
            "com.android.mendeleypaperreader.utl.MyContentProvider";
	
	
	public static final Uri CONTENT_URI_DOC_DETAILS = Uri.parse("content://" + AUTHORITY + "/" + DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS);
	public static final Uri CONTENT_URI_AUTHORS = Uri.parse("content://" + AUTHORITY + "/" + DatabaseOpenHelper.TABLE_AUTHORS);
	public static final Uri CONTENT_URI_FOLDERS = Uri.parse("content://" + AUTHORITY + "/" + DatabaseOpenHelper.TABLE_FOLDERS);
	
	public static final int ALLDOCS = 1;
	public static final int My_DOC = 2;  
	public static final int ALL_DOC_AUTHORS = 3;
	public static final int DOC_AUTHORS_ID = 4;
	public static final int ALL_FOLDERS = 5;
	public static final int FOLDERS_ID = 6;
	
	private static final UriMatcher sURIMatcher = 
            new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS, ALLDOCS);
		sURIMatcher.addURI(AUTHORITY, DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS + "/true", My_DOC);
		sURIMatcher.addURI(AUTHORITY, DatabaseOpenHelper.TABLE_AUTHORS, ALL_DOC_AUTHORS);
		sURIMatcher.addURI(AUTHORITY, DatabaseOpenHelper.TABLE_AUTHORS + "/#", DOC_AUTHORS_ID);
		sURIMatcher.addURI(AUTHORITY, DatabaseOpenHelper.TABLE_FOLDERS, ALL_FOLDERS);
		sURIMatcher.addURI(AUTHORITY, DatabaseOpenHelper.TABLE_FOLDERS + "/#", FOLDERS_ID);
}
	

	
	// system calls onCreate() when it starts up the provider.
	 @Override
	 public boolean onCreate() {
	  // get access to the database helper
		 db_helper = new DatabaseOpenHelper(getContext());
	  return false;
	 }
	
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		SQLiteDatabase db = db_helper.getWritableDatabase();
		
		 Uri _uri = null;
		    switch (sURIMatcher.match(uri)){
		    case ALLDOCS:
		    	
		    	long row = db.insert(DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS, null, values);
		    	//long row = mendeleyDataSource.insertDocument(DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS, values);
	
		    	// If record is added successfully		 
		           if(row > 0) {		 
		              Uri newUri = ContentUris.withAppendedId(CONTENT_URI_DOC_DETAILS, row);		 
		              getContext().getContentResolver().notifyChange(newUri, null);		 
		              return newUri;	
		           }
		           
		           break;
		           
		    case ALL_DOC_AUTHORS:
		    
		    	long authors_row = db.insert(DatabaseOpenHelper.TABLE_AUTHORS, null, values);
		    	//long authors_row = mendeleyDataSource.insert_author(DatabaseOpenHelper.TABLE_AUTHORS, values);
		    
		    // If record is added successfully		 
	           if(authors_row > 0) {		 
	              Uri newUri = ContentUris.withAppendedId(CONTENT_URI_AUTHORS, authors_row);		 
	              getContext().getContentResolver().notifyChange(newUri, null);		 
	              return newUri;	
	           }
		           
		    
		    case ALL_FOLDERS:
		    	  
		    	long folders_row = db.insert(DatabaseOpenHelper.TABLE_FOLDERS, null, values);
		    	//long folders_row = mendeleyDataSource.insert_user_folders(DatabaseOpenHelper.TABLE_FOLDERS, values);
		    	// If record is added successfully		 
		           if(folders_row > 0) {		 
		              Uri newUri = ContentUris.withAppendedId(CONTENT_URI_FOLDERS, folders_row);		 
		              getContext().getContentResolver().notifyChange(newUri, null);		 
		              return newUri;	
		           }
		    
		    
		    default: throw new SQLException("Failed to insert row into " + uri);
		    }
		    return _uri; 
		    
	
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
		        String[] selectionArgs, String sortOrder) {
		
		  SQLiteDatabase db = db_helper.getWritableDatabase();
		  SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		  
		
		  switch (sURIMatcher.match(uri)) {
		  case ALLDOCS:
			  queryBuilder.setTables(DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS);
		   break;
		  case My_DOC:		   
			  
			  String id = uri.getPathSegments().get(1);
			  queryBuilder.setTables(DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS);
		      queryBuilder.appendWhere(DatabaseOpenHelper.AUTHORED + "=  '" + id + "' ");
		   break;
		  case ALL_FOLDERS:
			  
			  queryBuilder.setTables(DatabaseOpenHelper.TABLE_FOLDERS);
			  
			  
			   break;
		  default:
		   throw new IllegalArgumentException("Unsupported URI: " + uri);
		  }
		  
		  Cursor cursor = queryBuilder.query(db, projection, selection,
		    selectionArgs, null, null, sortOrder);
		  cursor.setNotificationUri(getContext().getContentResolver(), uri);
		  return cursor;
		  
		
	} 

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	



	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
