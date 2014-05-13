package com.android.mendeleypaperreader.utl;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;

public class LoadData {

	private Context mcontext;
	private String mtoken;
	private String doc_detail_id;

	public LoadData(Context context, String token) {
		mcontext = context;
		mtoken = token;
	}

	public void GetUserLibrary(String url) {

		ContentValues values = new ContentValues();
		ContentValues authors_values = new ContentValues();

		Log.d(Globalconstant.TAG, url);
		JSONParser jParser = new JSONParser();

		// get JSON data from URL
		String strResponse = jParser.getJSONFromUrl(url);

		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::");
		try {

			JSONArray jcols = new JSONArray(strResponse);

			for (int i = 0; i < jcols.length(); i++) {

				String authors = "";

				JSONObject lib = jcols.getJSONObject(i);

				doc_detail_id = lib.optString(Globalconstant.ID);
				values.put(DatabaseOpenHelper._ID,lib.optString(Globalconstant.ID));
				values.put(DatabaseOpenHelper.TYPE,	lib.optString(Globalconstant.TYPE));
				values.put(DatabaseOpenHelper.MONTH,lib.optString(Globalconstant.MONTH));
				values.put(DatabaseOpenHelper.YEAR,	lib.optString(Globalconstant.YEAR));
				values.put(DatabaseOpenHelper.LAST_MODIFIED,lib.optString(Globalconstant.LAST_MODIFIED));
				values.put(DatabaseOpenHelper.DAY,lib.optString(Globalconstant.DAY));
				values.put(DatabaseOpenHelper.GROUP_ID,	lib.optString(Globalconstant.GROUP_ID));
				values.put(DatabaseOpenHelper.SOURCE,lib.optString(Globalconstant.SOURCE));
				values.put(DatabaseOpenHelper.TITLE,lib.optString(Globalconstant.TITLE));
				values.put(DatabaseOpenHelper.REVISION,	lib.optString(Globalconstant.REVISION));
				
				
				Log.d(Globalconstant.TAG, "title: " + lib.optString(Globalconstant.TITLE));
				
				if (!lib.isNull(Globalconstant.IDENTIFIERS)) {
					JSONObject structure = (JSONObject) lib.get(Globalconstant.IDENTIFIERS);
					Log.d(Globalconstant.TAG, "IDENTIFIERS: " + lib.get(Globalconstant.IDENTIFIERS));

					values.put(DatabaseOpenHelper.PMID,	structure.optString(Globalconstant.PMID));
					values.put(DatabaseOpenHelper.DOI,structure.optString(Globalconstant.DOI));
					values.put(DatabaseOpenHelper.ISSN,	structure.optString(Globalconstant.ISSN));
					Log.d(Globalconstant.TAG, "PMID: " + structure.optString(Globalconstant.PMID));
					Log.d(Globalconstant.TAG, "DOI: " + structure.optString(Globalconstant.DOI));
					Log.d(Globalconstant.TAG, "ISSN: " + structure.optString(Globalconstant.ISSN));
				}
				else{
					
					values.put(DatabaseOpenHelper.PMID,	"");
					values.put(DatabaseOpenHelper.DOI,"");
					values.put(DatabaseOpenHelper.ISSN,	"");
					
				}

				values.put(DatabaseOpenHelper.ABSTRACT,	lib.optString(Globalconstant.ABSTRACT));
				values.put(DatabaseOpenHelper.PROFILE_ID,lib.optString(Globalconstant.PROFILE_ID));
				
				JSONArray documents_array = lib.getJSONArray(Globalconstant.AUTHORS);

				for (int j = 0; j < documents_array.length(); j++) {
					
					JSONObject sub_documents_array = documents_array.getJSONObject(j);

					String author_name = sub_documents_array.optString(Globalconstant.FORENAME)	+ " "+ sub_documents_array.optString(Globalconstant.SURNAME);

					authors += author_name + ",";

					authors_values.put(DatabaseOpenHelper.DOC_DETAILS_ID,doc_detail_id);
					authors_values.put(DatabaseOpenHelper.AUTHOR_NAME,author_name);

					Uri uri_authors = mcontext.getContentResolver().insert(MyContentProvider.CONTENT_URI_AUTHORS,authors_values);

				}

				values.put(DatabaseOpenHelper.AUTHORS, authors);
				values.put(DatabaseOpenHelper.ADDED,lib.optString(Globalconstant.ADDED));
				values.put(DatabaseOpenHelper.PAGES,lib.optString(Globalconstant.PAGES));
				values.put(DatabaseOpenHelper.VOLUME,lib.optString(Globalconstant.VOLUME));
				values.put(DatabaseOpenHelper.ISSUE,lib.optString(Globalconstant.ISSUE));
				values.put(DatabaseOpenHelper.WEBSITE,lib.optString(Globalconstant.WEBSITE));
				values.put(DatabaseOpenHelper.PUBLISHER,lib.optString(Globalconstant.PUBLISHER));
				values.put(DatabaseOpenHelper.CITY,	lib.optString(Globalconstant.CITY));
				values.put(DatabaseOpenHelper.EDITION,lib.optString(Globalconstant.EDITION));
				values.put(DatabaseOpenHelper.INSTITUTION,lib.optString(Globalconstant.INSTITUTION));
				values.put(DatabaseOpenHelper.SERIES,lib.optString(Globalconstant.SERIES));
				values.put(DatabaseOpenHelper.EDITORS,lib.optString(Globalconstant.EDITORS));
				values.put(DatabaseOpenHelper.READ,	lib.optString(Globalconstant.READ));
				values.put(DatabaseOpenHelper.STARRED,lib.optString(Globalconstant.STARRED));
				values.put(DatabaseOpenHelper.AUTHORED,	lib.optString(Globalconstant.AUTHORED));
				values.put(DatabaseOpenHelper.CONFIRMED,lib.optString(Globalconstant.CONFIRMED));
				values.put(DatabaseOpenHelper.HIDDEN,lib.optString(Globalconstant.HIDDEN));
				
				if (Globalconstant.LOG)
					Log.d(Globalconstant.TAG,
							":::::::::::::::::::::::::::::::::::::::::::::::");

				Uri uri = mcontext.getContentResolver().insert(MyContentProvider.CONTENT_URI_DOC_DETAILS, values);

			}

		} catch (Exception e) {
			if (Globalconstant.LOG) {
				Log.e(Globalconstant.TAG,
						"Got exception when parsing online data");
				Log.e(Globalconstant.TAG, e.getClass().getSimpleName() + ": "+ e.getMessage());
				// mendeleyDataSource.delete_author_table_by_doc_id(doc_detail_id);
				// rollback � tabela de authores com o doc id respetivo
			}
		}

	}

	public void getFolders(String url) {

		ContentValues values = new ContentValues();

		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, url);
		JSONParser jParser = new JSONParser();
		// get JSON data from URL
		String strResponse = jParser.getJSONFromUrl(url);

		Log.d(Globalconstant.TAG, ":::::::LoadData  - Folders:::::");
		try {

			JSONArray jcols = new JSONArray(strResponse);
			Log.d(Globalconstant.TAG, "jcols.length(): " + jcols.length());

			for (int i = 0; i < jcols.length(); i++) {

				JSONObject lib = jcols.getJSONObject(i);

				values.put(DatabaseOpenHelper.FOLDER_NAME,	lib.optString(Globalconstant.NAME));
				values.put(DatabaseOpenHelper.FOLDER_PARENT,lib.optString(Globalconstant.PARENT));
				values.put(DatabaseOpenHelper.FOLDER_ID,lib.optString(Globalconstant.ID));
				values.put(DatabaseOpenHelper.FOLDER_GROUP,	lib.optString(Globalconstant.GROUP));
				values.put(DatabaseOpenHelper.FOLDER_ADDED,	lib.optString(Globalconstant.ADDED));

				Uri uri = mcontext.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS, values);
				
				
				get_docs_in_folders(lib.optString(Globalconstant.ID));
				

			}

		} catch (Exception e) {
			if (Globalconstant.LOG) {
				Log.e(Globalconstant.TAG, "Got exception when parsing online data");
				Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
			}

		}

	}
	
	
	public void get_docs_in_folders(String folder_id){
		
		ContentValues values = new ContentValues();
		String auxurl = Globalconstant.get_docs_in_folders;
		String where= null;
		//String [] selectionArgs = {};
		String url = auxurl.replace("id", folder_id) + mtoken; 
		
		if (Globalconstant.LOG)
			Log.d(Globalconstant.TAG, url);
		JSONParser jParser = new JSONParser();
		// get JSON data from URL
		String strResponse = jParser.getJSONFromUrl(url);

		Log.d(Globalconstant.TAG, ":::::::LoadData  - Docs in Folders:::::");
		
		try {

			JSONObject jcols = new JSONObject(strResponse);
			JSONArray docs_ids = (JSONArray) jcols.get(Globalconstant.DOCUMENTS_ID);
			Log.d(Globalconstant.TAG, "jcols.length(): " + docs_ids.length());

			
			for (int i = 0; i < docs_ids.length(); i++) {

				String doc_id = docs_ids.get(i).toString();
				values.put(DatabaseOpenHelper.FOLDER_ID, folder_id);
				Log.d(Globalconstant.TAG, "DOCUMENTS_ID: " + doc_id);
				
				where = DatabaseOpenHelper._ID + " = '" + doc_id + "'";
				
			    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");
			    mcontext.getContentResolver().update(uri, values, where, null);
	}
		
		} catch (Exception e) {
			if (Globalconstant.LOG) {
				Log.e(Globalconstant.TAG, "Got exception when parsing online data");
				Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
			}
		}
}
}