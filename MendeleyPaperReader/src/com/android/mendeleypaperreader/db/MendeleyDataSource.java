package com.android.mendeleypaperreader.db;

import com.android.mendeleypaperreader.MainMenuActivityFragmentDetails;
import com.android.mendeleypaperreader.utl.Globalconstant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
	
	
	public void insertDocument (String id, String type, String month, String year, String last_modified, String day, String group_id, String source, String title, String revision, String pmi, String doi, String issn, String v_abstract, String profile_id, String authors, String added,
			String volume, String pages, String issue, String website, String publisher, String city, String edition, String institution, String series, String editors, String read, String starred, String authored, String confirmed, String hidden	){
	
		
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
		insert_Documents_Values.put(DatabaseOpenHelper.PMID, pmi);
		insert_Documents_Values.put(DatabaseOpenHelper.DOI, doi);
		insert_Documents_Values.put(DatabaseOpenHelper.ISSN, issn);
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
		insert_Documents_Values.put(DatabaseOpenHelper.EDITORS, editors);
		insert_Documents_Values.put(DatabaseOpenHelper.READ, read);
		insert_Documents_Values.put(DatabaseOpenHelper.STARRED, starred);
		insert_Documents_Values.put(DatabaseOpenHelper.AUTHORED, authored);
		insert_Documents_Values.put(DatabaseOpenHelper.CONFIRMED, confirmed);
		insert_Documents_Values.put(DatabaseOpenHelper.HIDDEN, hidden);		
		
		db.insert(DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS, null, insert_Documents_Values);
		
		
		}
	
	
	public void insert_author(String doc_detail_id, String author_name){
		
		ContentValues insert_author_value = new ContentValues();
		
		insert_author_value.put(DatabaseOpenHelper.DOC_DETAILS_ID, doc_detail_id);
		insert_author_value.put(DatabaseOpenHelper.AUTHOR_NAME, author_name);
		
		db.insert(DatabaseOpenHelper.TABLE_AUTHORS, null, insert_author_value);
	}
	
	public void insert_user_folders(String folder_id, String folder_name){
		
		ContentValues insert_author_value = new ContentValues();
		
		insert_author_value.put(DatabaseOpenHelper.FOLDER_ID, folder_id);
		insert_author_value.put(DatabaseOpenHelper.FOLDER_NAME, folder_name);
		
		db.insert(DatabaseOpenHelper.TABLE_AUTHORS, null, insert_author_value);
	}
	
	
	
	public void delete_author_table_by_doc_id(String doc_id){
		db.delete(DatabaseOpenHelper.TABLE_AUTHORS, "UPPER(" + DatabaseOpenHelper.DOC_DETAILS_ID + ") = UPPER(?)",
				new String[] { doc_id });
	}
	
	
	public Cursor get_all_titles_doc(){
		
		Cursor cursor = this.db.rawQuery(
				"select " + DatabaseOpenHelper.TITLE + " as _id, " + DatabaseOpenHelper.AUTHORS +", " + DatabaseOpenHelper.SOURCE + "|| " + DatabaseOpenHelper.YEAR + " as data from "
						+ DatabaseOpenHelper.TABLE_DOCUMENT_DETAILS + " order by " + DatabaseOpenHelper.TITLE + " ASC",
				new String[] {});
		
		return cursor;
		
		
	}
	
	
	}