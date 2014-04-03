package com.android.mendeleypaperreader.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


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
		mendeley_library.close();
	}
	
	
	public void insertDocuments (String id, String type, Integer month, Integer year, String last_modified, Integer day, String group_id, String source, String title, String revision, String identifiers, String v_abstract, String profile_id, String authors, String added,
								String pages, String volume, String issue, String website, String publisher, String city, String edition, String institution, String series, String chapter, String editors, String read, String starred, String authored, String confirmed, String hidden	){
	
		
		ContentValues insert_Documents_Values = new ContentValues();
		
		insert_Documents_Values.put(DatabaseOpenHelper._ID, id);
		insert_Documents_Values.put(DatabaseOpenHelper.TYPE, type);
		insert_Documents_Values.put(DatabaseOpenHelper.MONTH, month);
		insert_Documents_Values.put(DatabaseOpenHelper.YEAR, year);
		insert_Documents_Values.put(DatabaseOpenHelper.LAST_MODIFIED, last_modified);
		insert_Documents_Values.put(DatabaseOpenHelper.DAY, day);
		insert_Documents_Values.put(DatabaseOpenHelper.GROUP_ID, group_id);
		insert_Documents_Values.put(DatabaseOpenHelper.SOURCE, source);
		insert_Documents_Values.put(DatabaseOpenHelper.TITLE, title);
		insert_Documents_Values.put(DatabaseOpenHelper.REVISION, revision);
		insert_Documents_Values.put(DatabaseOpenHelper.IDENTIFIERS, identifiers);
		insert_Documents_Values.put(DatabaseOpenHelper.ABSTRACT, v_abstract);
		insert_Documents_Values.put(DatabaseOpenHelper.PROFILE_ID, profile_id);
		insert_Documents_Values.put(DatabaseOpenHelper.AUTHORS, authors);
		insert_Documents_Values.put(DatabaseOpenHelper.ADDED, added);
		insert_Documents_Values.put(DatabaseOpenHelper.PAGES, pages);
		insert_Documents_Values.put(DatabaseOpenHelper.VOLUME, volume);
		insert_Documents_Values.put(DatabaseOpenHelper.ISSUE, issue);
		insert_Documents_Values.put(DatabaseOpenHelper.WEBSITE, website);
		insert_Documents_Values.put(DatabaseOpenHelper.PUBLISHER, publisher);
		insert_Documents_Values.put(DatabaseOpenHelper.CITY, city);
		insert_Documents_Values.put(DatabaseOpenHelper.EDITION, edition);
		insert_Documents_Values.put(DatabaseOpenHelper.INSTITUTION, institution);
		insert_Documents_Values.put(DatabaseOpenHelper.SERIES, series);
		insert_Documents_Values.put(DatabaseOpenHelper.CHAPTER, chapter);
		insert_Documents_Values.put(DatabaseOpenHelper.EDITORS, editors);
		insert_Documents_Values.put(DatabaseOpenHelper.READ, read);
		insert_Documents_Values.put(DatabaseOpenHelper.STARRED, starred);
		insert_Documents_Values.put(DatabaseOpenHelper.AUTHORED, authored);
		insert_Documents_Values.put(DatabaseOpenHelper.CONFIRMED, confirmed);
		insert_Documents_Values.put(DatabaseOpenHelper.HIDDEN, hidden);
		
		
		db.insert(DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS, null, insert_Documents_Values);
		
		
		}
	
	
	
	}