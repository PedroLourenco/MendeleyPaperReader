package com.android.mendeleypaperreader.utl;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.mendeleypaperreader.db.MendeleyDataSource;

public class LoadData {
	
	private MendeleyDataSource mendeleyDataSource;
	private Context mcontext;
	private String doc_detail_id;
	
	public LoadData(Context context) {	
		mcontext = context;
    }
	
	public void GetUserLibrary(String url){
		
		mendeleyDataSource = new MendeleyDataSource(mcontext);
		mendeleyDataSource.open();
		
		Log.d(Globalconstant.TAG, url);
		JSONParser jParser = new JSONParser();
		 
        // get JSON data from URL
        String strResponse = jParser.getJSONFromUrl(url);

        Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::");
        try {
        
        	JSONArray jcols = new JSONArray(strResponse);
        Log.d(Globalconstant.TAG, "jcols.length(): " + jcols.length());
        
        for (int i=0; i< jcols.length(); i++) {
        	 
        	
        	 String type, month, year, last_modified, day, group_id, source, title, revision, pmid, doi, issn, v_abstract, profile_id, added,pages, 
        	 		volume, issue, website, publisher, city, edition, institution, series, editors, read, starred, authored, confirmed, hidden;
        
        	 String authors = "";
        	
        	
        	JSONObject lib = jcols.getJSONObject(i);
			 
			 Log.d(Globalconstant.TAG, "id: " + lib. optString(Globalconstant.ID));
			 doc_detail_id = lib. optString(Globalconstant.ID);
			 Log.d(Globalconstant.TAG, "type: " + lib.optString(Globalconstant.TYPE));
			 type = lib.optString(Globalconstant.TYPE);			 
			 Log.d(Globalconstant.TAG, "month: " + lib.optString(Globalconstant.MONTH));
			 month = lib.optString(Globalconstant.MONTH);
			 Log.d(Globalconstant.TAG, "year: " + lib.optString(Globalconstant.YEAR));
			 year = lib.optString(Globalconstant.YEAR);			 
			 Log.d(Globalconstant.TAG, "last_modified: " + lib.optString(Globalconstant.LAST_MODIFIED));
			 last_modified = lib.optString(Globalconstant.LAST_MODIFIED);
			 Log.d(Globalconstant.TAG, "day: " + lib.optString(Globalconstant.DAY));
			 day = lib.optString(Globalconstant.DAY);
			 Log.d(Globalconstant.TAG, "group_id: " + lib.optString(Globalconstant.GROUP_ID));
			 group_id = lib.optString(Globalconstant.GROUP_ID);
			 Log.d(Globalconstant.TAG, "source: " + lib.optString(Globalconstant.SOURCE));
			 source = lib.optString(Globalconstant.SOURCE);
			 Log.d(Globalconstant.TAG, "title: " + lib.optString(Globalconstant.TITLE));
			 title = lib.optString(Globalconstant.TITLE);
			 Log.d(Globalconstant.TAG, "revision: " + lib.optString(Globalconstant.REVISION));
			 revision = lib.optString(Globalconstant.REVISION);
			 Log.d(Globalconstant.TAG, "identifiers: " + lib.optString(Globalconstant.IDENTIFIERS));
						
			 JSONObject structure = (JSONObject) lib.get(Globalconstant.IDENTIFIERS);
			 Log.d(Globalconstant.TAG, "PMID: " + structure.optString(Globalconstant.PMID));
			 pmid = structure.optString(Globalconstant.PMID);
			 Log.d(Globalconstant.TAG, "doi: " + structure.optString(Globalconstant.DOI));
			 doi = structure.optString(Globalconstant.DOI);
			 Log.d(Globalconstant.TAG, "issn: " + structure.optString(Globalconstant.ISSN));
			 issn = structure.optString(Globalconstant.ISSN);
			 Log.d(Globalconstant.TAG, "abstract: " + lib.optString(Globalconstant.ABSTRACT));
			 v_abstract = lib.optString(Globalconstant.ABSTRACT);
			 Log.d(Globalconstant.TAG, "profile_id: " + lib.optString(Globalconstant.PROFILE_ID));
			 profile_id = lib.optString(Globalconstant.PROFILE_ID);
			 JSONArray documents_array = lib.getJSONArray(Globalconstant.AUTHORS);
			
			 for (int j=0; j< documents_array.length(); j++) {
				 JSONObject sub_documents_array = documents_array.getJSONObject(j);
				 
				 Log.d(Globalconstant.TAG, "Name: " + sub_documents_array.optString(Globalconstant.FORENAME) + " " + sub_documents_array.optString(Globalconstant.SURNAME));
				  String author_name = sub_documents_array.optString(Globalconstant.FORENAME) +" "+sub_documents_array.optString(Globalconstant.SURNAME);
				  
				  authors += author_name +",";
				  mendeleyDataSource.insert_author(doc_detail_id, author_name);
			 }
			 Log.d(Globalconstant.TAG, authors);
       
			 Log.d(Globalconstant.TAG, "added: " + lib.optString(Globalconstant.ADDED));
			 added = lib.optString(Globalconstant.ADDED);
			 Log.d(Globalconstant.TAG, "pages: " + lib.optString(Globalconstant.PAGES));
			 pages = lib.optString(Globalconstant.PAGES);
			 Log.d(Globalconstant.TAG, "volume: " + lib.optString(Globalconstant.VOLUME));
			 volume = lib.optString(Globalconstant.VOLUME);
			 Log.d(Globalconstant.TAG, "issue: " + lib.optString(Globalconstant.ISSUE));
			 issue = lib.optString(Globalconstant.ISSUE);
			 Log.d(Globalconstant.TAG, "website: " + lib.optString(Globalconstant.WEBSITE));
			 website = lib.optString(Globalconstant.WEBSITE);
			 Log.d(Globalconstant.TAG, "publisher: " + lib.optString(Globalconstant.PUBLISHER));
			 publisher = lib.optString(Globalconstant.PUBLISHER);
			 Log.d(Globalconstant.TAG, "city: " + lib.optString(Globalconstant.CITY));
			 city = lib.optString(Globalconstant.CITY);
			 Log.d(Globalconstant.TAG, "edition: " + lib.optString(Globalconstant.EDITION));
			 edition = lib.optString(Globalconstant.EDITION);
			 Log.d(Globalconstant.TAG, "institution: " + lib.optString(Globalconstant.INSTITUTION));
			 institution = lib.optString(Globalconstant.INSTITUTION);
			 Log.d(Globalconstant.TAG, "series: " + lib.optString(Globalconstant.SERIES));
			 series = lib.optString(Globalconstant.SERIES);
			 Log.d(Globalconstant.TAG, "editors: " + lib.optString(Globalconstant.EDITORS));
			 editors = lib.optString(Globalconstant.EDITORS);
			 Log.d(Globalconstant.TAG, "read: " + lib.optString(Globalconstant.READ));	
			 read = lib.optString(Globalconstant.READ);
			 Log.d(Globalconstant.TAG, "starred: " + lib.optString(Globalconstant.STARRED));
			 starred = lib.optString(Globalconstant.STARRED);
			 Log.d(Globalconstant.TAG, "authored: " + lib.optString(Globalconstant.AUTHORED));
			 authored = lib.optString(Globalconstant.AUTHORED);
			 Log.d(Globalconstant.TAG, "confirmed: " + lib.optString(Globalconstant.CONFIRMED));
			 confirmed = lib.optString(Globalconstant.CONFIRMED);
			 Log.d(Globalconstant.TAG, "hidden: " + lib.optString(Globalconstant.HIDDEN));
			 hidden = lib.optString(Globalconstant.HIDDEN);
			 Log.d(Globalconstant.TAG, ":::::::::::::::::::::::::::::::::::::::::::::::");
			 
			 mendeleyDataSource.insertDocument(doc_detail_id,type,month,year,last_modified,day,group_id,source,title,revision,pmid,doi,issn,v_abstract,profile_id,authors.substring(0, authors.length()-1) +"",added,pages,volume,
					 issue,website,publisher,city,edition,institution,series,editors,read,starred,authored,confirmed,hidden);
		 
        
        }
       
        } catch (Exception e) {
            Log.e(Globalconstant.TAG,"Got exception when parsing online data");
            Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
            mendeleyDataSource.delete_author_table_by_doc_id(doc_detail_id);
            //rollback á tabela de authores com o doc id respetivo
    }

		
		
	}

}
