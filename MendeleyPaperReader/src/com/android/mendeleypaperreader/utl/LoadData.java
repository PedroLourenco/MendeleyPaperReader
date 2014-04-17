package com.android.mendeleypaperreader.utl;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
import com.android.mendeleypaperreader.db.MendeleyDataSource;

public class LoadData {
	
	
	private Context mcontext;
	private String doc_detail_id;
	
	
	public LoadData(Context context) {	
		mcontext = context;
    }
	
	public void GetUserLibrary(String url){
		
		ContentValues values = new ContentValues();
		ContentValues authors_values = new ContentValues();

		//mendeleyDataSource = new MendeleyDataSource(mcontext);
		//mendeleyDataSource.open();
		
	
		Log.d(Globalconstant.TAG, url);
		JSONParser jParser = new JSONParser();
		 
        // get JSON data from URL
        String strResponse = jParser.getJSONFromUrl(url);

        Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::");
        try {
        
        	JSONArray jcols = new JSONArray(strResponse);
        Log.d(Globalconstant.TAG, "jcols.length(): " + jcols.length());
        
        for (int i=0; i< jcols.length(); i++) {
        
        	 String authors = "";
        	
        	JSONObject lib = jcols.getJSONObject(i);
			 
			 Log.d(Globalconstant.TAG, "id: " + lib. optString(Globalconstant.ID));
			 doc_detail_id = lib. optString(Globalconstant.ID);
			 values.put(DatabaseOpenHelper._ID, lib. optString(Globalconstant.ID));
			 
			 Log.d(Globalconstant.TAG, "type: " + lib.optString(Globalconstant.TYPE));
			 	
			 values.put(DatabaseOpenHelper.TYPE, lib. optString(Globalconstant.TYPE));
			 Log.d(Globalconstant.TAG, "month: " + lib.optString(Globalconstant.MONTH));
			 
			 values.put(DatabaseOpenHelper.MONTH, lib. optString(Globalconstant.MONTH));
			 Log.d(Globalconstant.TAG, "year: " + lib.optString(Globalconstant.YEAR));
			 			 
			 values.put(DatabaseOpenHelper.YEAR, lib. optString(Globalconstant.YEAR));
			 
			 Log.d(Globalconstant.TAG, "last_modified: " + lib.optString(Globalconstant.LAST_MODIFIED));
			 
			 values.put(DatabaseOpenHelper.LAST_MODIFIED, lib. optString(Globalconstant.LAST_MODIFIED));
			 
			 Log.d(Globalconstant.TAG, "day: " + lib.optString(Globalconstant.DAY));
			 
			 values.put(DatabaseOpenHelper.DAY, lib. optString(Globalconstant.DAY));
			 
			 Log.d(Globalconstant.TAG, "group_id: " + lib.optString(Globalconstant.GROUP_ID));
			 
			 values.put(DatabaseOpenHelper.GROUP_ID, lib. optString(Globalconstant.GROUP_ID));
			 
			 Log.d(Globalconstant.TAG, "source: " + lib.optString(Globalconstant.SOURCE));
			 
			 values.put(DatabaseOpenHelper.SOURCE, lib. optString(Globalconstant.SOURCE));
			 Log.d(Globalconstant.TAG, "title: " + lib.optString(Globalconstant.TITLE));
			 
			 values.put(DatabaseOpenHelper.TITLE, lib. optString(Globalconstant.TITLE));
			 Log.d(Globalconstant.TAG, "revision: " + lib.optString(Globalconstant.REVISION));
			 
			 values.put(DatabaseOpenHelper.REVISION, lib. optString(Globalconstant.REVISION));
			 Log.d(Globalconstant.TAG, "identifiers: " + lib.optString(Globalconstant.IDENTIFIERS));
						
			 if(!lib.isNull(Globalconstant.IDENTIFIERS)){
			 JSONObject structure = (JSONObject) lib.get(Globalconstant.IDENTIFIERS);			 
			 
				 Log.d(Globalconstant.TAG, "PMID: " + structure.optString(Globalconstant.PMID));
				
				 values.put(DatabaseOpenHelper.PMID, lib. optString(Globalconstant.PMID));
				 Log.d(Globalconstant.TAG, "doi: " + structure.optString(Globalconstant.DOI));
				
				 values.put(DatabaseOpenHelper.DOI, lib. optString(Globalconstant.DOI));
				 Log.d(Globalconstant.TAG, "issn: " + structure.optString(Globalconstant.ISSN));
				
				 values.put(DatabaseOpenHelper.ISSN, lib. optString(Globalconstant.ISSN));
			 }
			 
			 Log.d(Globalconstant.TAG, "abstract: " + lib.optString(Globalconstant.ABSTRACT));
			 
			 values.put(DatabaseOpenHelper.ABSTRACT, lib. optString(Globalconstant.ABSTRACT));
			 Log.d(Globalconstant.TAG, "profile_id: " + lib.optString(Globalconstant.PROFILE_ID));
			 
			 values.put(DatabaseOpenHelper.PROFILE_ID, lib. optString(Globalconstant.PROFILE_ID));
			 JSONArray documents_array = lib.getJSONArray(Globalconstant.AUTHORS);
			
			 for (int j=0; j< documents_array.length(); j++) {
				 JSONObject sub_documents_array = documents_array.getJSONObject(j);
				 
				 Log.d(Globalconstant.TAG, "Name: " + sub_documents_array.optString(Globalconstant.FORENAME) + " " + sub_documents_array.optString(Globalconstant.SURNAME));
				  String author_name = sub_documents_array.optString(Globalconstant.FORENAME) +" "+sub_documents_array.optString(Globalconstant.SURNAME);
				  
				  authors += author_name +",";
				  
				  authors_values.put(DatabaseOpenHelper.DOC_DETAILS_ID, doc_detail_id);
				  authors_values.put(DatabaseOpenHelper.AUTHOR_NAME, author_name);
				  
				  Uri uri_authors = mcontext.getContentResolver().insert(MyContentProvider.CONTENT_URI_AUTHORS, authors_values);
				  
				  //mendeleyDataSource.insert_author(doc_detail_id, author_name);
			 }
			 Log.d(Globalconstant.TAG, authors);
			 values.put(DatabaseOpenHelper.AUTHORS, authors);
			 Log.d(Globalconstant.TAG, "added: " + lib.optString(Globalconstant.ADDED));
	
					 values.put(DatabaseOpenHelper.ADDED, lib. optString(Globalconstant.ADDED));
			 Log.d(Globalconstant.TAG, "pages: " + lib.optString(Globalconstant.PAGES));

			 
			 values.put(DatabaseOpenHelper.PAGES, lib. optString(Globalconstant.PAGES));
			 Log.d(Globalconstant.TAG, "volume: " + lib.optString(Globalconstant.VOLUME));

			 
			 values.put(DatabaseOpenHelper.VOLUME, lib. optString(Globalconstant.VOLUME));
			 Log.d(Globalconstant.TAG, "issue: " + lib.optString(Globalconstant.ISSUE));

			 values.put(DatabaseOpenHelper.ISSUE, lib. optString(Globalconstant.ISSUE));
			 Log.d(Globalconstant.TAG, "website: " + lib.optString(Globalconstant.WEBSITE));

			 values.put(DatabaseOpenHelper.WEBSITE, lib. optString(Globalconstant.WEBSITE));
			 Log.d(Globalconstant.TAG, "publisher: " + lib.optString(Globalconstant.PUBLISHER));

			 values.put(DatabaseOpenHelper.PUBLISHER, lib. optString(Globalconstant.PUBLISHER));
			 Log.d(Globalconstant.TAG, "city: " + lib.optString(Globalconstant.CITY));

			 values.put(DatabaseOpenHelper.CITY, lib. optString(Globalconstant.CITY));
			 Log.d(Globalconstant.TAG, "edition: " + lib.optString(Globalconstant.EDITION));
	
			 values.put(DatabaseOpenHelper.EDITION, lib. optString(Globalconstant.EDITION));
			 Log.d(Globalconstant.TAG, "institution: " + lib.optString(Globalconstant.INSTITUTION));
	
			 values.put(DatabaseOpenHelper.INSTITUTION, lib. optString(Globalconstant.INSTITUTION));
			 Log.d(Globalconstant.TAG, "series: " + lib.optString(Globalconstant.SERIES));
	
			 values.put(DatabaseOpenHelper.SERIES, lib. optString(Globalconstant.SERIES));
			 Log.d(Globalconstant.TAG, "editors: " + lib.optString(Globalconstant.EDITORS));
	
			 values.put(DatabaseOpenHelper.EDITORS, lib. optString(Globalconstant.EDITORS));
			 Log.d(Globalconstant.TAG, "read: " + lib.optString(Globalconstant.READ));	
	
			 values.put(DatabaseOpenHelper.READ, lib. optString(Globalconstant.READ));
			 Log.d(Globalconstant.TAG, "starred: " + lib.optString(Globalconstant.STARRED));
	
			 values.put(DatabaseOpenHelper.STARRED, lib. optString(Globalconstant.STARRED));
			 Log.d(Globalconstant.TAG, "authored: " + lib.optString(Globalconstant.AUTHORED));
	
			 values.put(DatabaseOpenHelper.AUTHORED, lib. optString(Globalconstant.AUTHORED));
			 Log.d(Globalconstant.TAG, "confirmed: " + lib.optString(Globalconstant.CONFIRMED));
	
			 values.put(DatabaseOpenHelper.CONFIRMED, lib. optString(Globalconstant.CONFIRMED));
			 Log.d(Globalconstant.TAG, "hidden: " + lib.optString(Globalconstant.HIDDEN));
	
			 values.put(DatabaseOpenHelper.HIDDEN, lib. optString(Globalconstant.HIDDEN));
			 Log.d(Globalconstant.TAG, ":::::::::::::::::::::::::::::::::::::::::::::::");
			 
				 
			 Uri uri = mcontext.getContentResolver().insert(MyContentProvider.CONTENT_URI_DOC_DETAILS, values);

			 //Log.d(Globalconstant.TAG, uri.toString() + " inserted!");
        
        }
       
        } catch (Exception e) {
            Log.e(Globalconstant.TAG,"Got exception when parsing online data");
            Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
            //mendeleyDataSource.delete_author_table_by_doc_id(doc_detail_id);
            //rollback á tabela de authores com o doc id respetivo
    }
        
		
	}
        
        
        public void getFolders(String url){
        	
        	
        ContentValues values = new ContentValues();
        	
        	Log.d(Globalconstant.TAG, url);
    		JSONParser jParser = new JSONParser(); 
        	// get JSON data from URL
            String strResponse = jParser.getJSONFromUrl(url);

            Log.d(Globalconstant.TAG, ":::::::LoadData  - Folders:::::");
            try {
            
            	JSONArray jcols = new JSONArray(strResponse);
            Log.d(Globalconstant.TAG, "jcols.length(): " + jcols.length());
            
            for (int i=0; i< jcols.length(); i++) {
                
           	           	
           	JSONObject lib = jcols.getJSONObject(i);
   			 
   			 Log.d(Globalconstant.TAG, "folder name: " + lib. optString(Globalconstant.NAME));
   			 values.put(DatabaseOpenHelper.FOLDER_NAME, lib. optString(Globalconstant.NAME));
   			 
   			Log.d(Globalconstant.TAG, "folder parent: " + lib. optString(Globalconstant.PARENT));
  			 values.put(DatabaseOpenHelper.FOLDER_PARENT, lib. optString(Globalconstant.PARENT));
            
  			Log.d(Globalconstant.TAG, "folder id: " + lib. optString(Globalconstant.ID));
 			 values.put(DatabaseOpenHelper.FOLDER_ID, lib. optString(Globalconstant.ID));
 			 
 			Log.d(Globalconstant.TAG, "folder group: " + lib. optString(Globalconstant.GROUP));
			 values.put(DatabaseOpenHelper.FOLDER_GROUP, lib. optString(Globalconstant.GROUP));
			 
			 Log.d(Globalconstant.TAG, "folder added: " + lib. optString(Globalconstant.ADDED));
			 values.put(DatabaseOpenHelper.FOLDER_ADDED, lib. optString(Globalconstant.ADDED));
  			 
            
			 
			 Uri uri = mcontext.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS, values);

            }
            
            } catch (Exception e) {
                Log.e(Globalconstant.TAG,"Got exception when parsing online data");
                Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
               
        }
        	
        }

		


}
