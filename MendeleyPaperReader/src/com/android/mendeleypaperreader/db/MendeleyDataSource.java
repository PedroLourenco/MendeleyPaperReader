package com.android.mendeleypaperreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	

	public long insertDocument (String tableName, ContentValues values){
	
		return db.insert(tableName, null, values);
	}
	
		
	
	public long insert_author(String tableName, ContentValues values){
				
		return db.insert(tableName, null, values);
	}
	
	
	public void insert_user_folders(String folder_id, String folder_name){
		
		ContentValues insert_author_value = new ContentValues();
		
		insert_author_value.put(DatabaseOpenHelper.FOLDER_ID, folder_id);
		insert_author_value.put(DatabaseOpenHelper.FOLDER_NAME, folder_name);
		
		db.insert(DatabaseOpenHelper.TABLE_AUTHORS, null, insert_author_value);
	}

	
	public long insert_user_folders(String tableName, ContentValues values){
		
		return db.insert(tableName, null, values);
	}
	
	
	
	
	public void delete_author_table_by_doc_id(String doc_id){
		db.delete(DatabaseOpenHelper.TABLE_AUTHORS, "UPPER(" + DatabaseOpenHelper.DOC_DETAILS_ID + ") = UPPER(?)",
				new String[] { doc_id });
	}
	
	
	public Cursor get_all_titles_doc(){
		
		Cursor cursor = db.rawQuery(
				"select " + DatabaseOpenHelper.TITLE + " as _id, " + DatabaseOpenHelper.AUTHORS +", " + DatabaseOpenHelper.SOURCE + "||" + DatabaseOpenHelper.YEAR + " as data from "
						+ DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS + " order by " + DatabaseOpenHelper.TITLE + " ASC",
				new String[] {});
		
		
		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "get_all_titles_doc");
		
		return cursor;		
	}
	
	
	/*public Cursor get_all_folders(){
		
		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "get_all_folders");
		
		Cursor cursor = db.rawQuery(
				"select " + DatabaseOpenHelper.FOLDER_NAME + " as _id from "
						+ DatabaseOpenHelper.TABLE_FOLDERS + " order by " + DatabaseOpenHelper.FOLDER_NAME + " ASC",
				new String[] {});
		
		
		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "get_all_folders");
		
		return cursor;		
	}
	
	*/
public Cursor get_my_docs(){
		
		Cursor cursor = db.rawQuery(
				"select " + DatabaseOpenHelper.TITLE + " as _id, " + DatabaseOpenHelper.AUTHORS +", " + DatabaseOpenHelper.SOURCE + "||" + DatabaseOpenHelper.YEAR + " as data from "
						+ DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS + " where " + Globalconstant.AUTHORED + "= ? order by " + DatabaseOpenHelper.TITLE + " ASC",
				new String[] {"true"});
		
		
		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "get_my_docs");
		
		return cursor;
		
	}
	

	
	}