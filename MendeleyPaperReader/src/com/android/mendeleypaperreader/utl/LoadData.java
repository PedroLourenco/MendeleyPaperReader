package com.android.mendeleypaperreader.utl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Classname: LoadData 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

//TODO - adicionar arvore de pastas e documentos

public class LoadData {

    private Context context;
    private String doc_detail_id;
    private static SessionManager session;
    private static String access_token;
    public LoadData(Context context) {
	this.context = context;

	session = new SessionManager(this.context);  
	access_token = session.LoadPreference("access_token");


    }
    
    
    

    public void GetUserLibrary2(String url) {

	ContentValues values = new ContentValues();
	ContentValues authors_values = new ContentValues();
	JSONParser jParser = new JSONParser();
	JsonFactory factory = new JsonFactory();
	List<InputStream> link = new ArrayList<InputStream>();
	
	Log.d(Globalconstant.TAG, "--fieldname--: " + link.size() );
	
	try {
	    link = jParser.getJACKSONFromUrl(url,true);
	    
	    for( InputStream oneItem : link ) {

		JsonParser jp = factory.createJsonParser(oneItem);


	    


		//JsonFactory factory = new JsonFactory();


		//JsonToken token = jp.nextToken();




		while (jp.nextToken() != JsonToken.END_OBJECT) {



		    String fieldname = jp.getCurrentName();
		    //Log.d(Globalconstant.TAG, "--fieldname--: " + fieldname +" --- " + jp.getText());
		    if (Globalconstant.ID.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.TITLE.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.TYPE.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.AUTHORS.equals(fieldname)) {

			while (jp.nextToken() != JsonToken.END_ARRAY ) {
			    String first_name = jp.getCurrentName();

			    if (Globalconstant.FORENAME.equals(first_name)){
				jp.nextToken();
				Log.d(Globalconstant.TAG, "fieldname: " + first_name +" --- " + jp.getText());

				while (jp.nextToken() != JsonToken.END_OBJECT) {
				    String last_name = jp.getCurrentName();

				    if (Globalconstant.SURNAME.equals(last_name)){
					jp.nextToken();
					Log.d(Globalconstant.TAG, "fieldname: " + last_name +" --- " + jp.getText());

				    }
				}

			    }



			}
		    }


		    if (Globalconstant.YEAR.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.SOURCE.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.IDENTIFIERS.equals(fieldname)) {
			while (jp.nextToken() != JsonToken.END_ARRAY) {
			    String pmid = jp.getCurrentName();

			    if (Globalconstant.PMID.equals(pmid)){
				jp.nextToken();
				Log.d(Globalconstant.TAG, "fieldname: " + pmid +" --- " + jp.getText());
			    }



			    if (Globalconstant.DOI.equals(pmid)){
				jp.nextToken();
				Log.d(Globalconstant.TAG, "fieldname: " + pmid +" --- " + jp.getText());

			    }


			    if (Globalconstant.ISSN.equals(pmid)){
				jp.nextToken();
				Log.d(Globalconstant.TAG, "fieldname: " + pmid +" --- " + jp.getText());

			    }
			}

		    }

		    if (Globalconstant.ABSTRACT.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.PAGES.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.CREATED.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.VOLUME.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.ISSUE.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }

		    if (Globalconstant.WEBSITES.equals(fieldname)) {
			while (jp.nextToken() != JsonToken.END_ARRAY) {
			    //String website = jp.getCurrentName();

			    jp.nextToken();

			    Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

			}


		    }
		    if (Globalconstant.INSTITUTION.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }
		    if (Globalconstant.READ.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }
		    if (Globalconstant.STARRED.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }
		    if (Globalconstant.AUTHORED.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }
		    if (Globalconstant.CONFIRMED.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }
		    if (Globalconstant.HIDDEN.equals(fieldname)) {
			// current token is "name",
			// move to next, which is "name"'s value
			jp.nextToken();

			Log.d(Globalconstant.TAG, "fieldname: " + fieldname +" --- " + jp.getText());

		    }
		}
	}

	    } catch (Exception e) {
		e.printStackTrace();
	    }



	}



    public void GetUserLibrary(String url) {

	ContentValues values = new ContentValues();
	ContentValues authors_values = new ContentValues();

	Log.d(Globalconstant.TAG, "GetUserLibrary: " + url);
	Log.d(Globalconstant.TAG, "access_token: " + access_token);
	JSONParser jParser = new JSONParser();

	List<String> jsostrResponse = new ArrayList<String>();

	// get JSON data from URL
	
	jsostrResponse = jParser.getJSONFromUrl(url, true);


	// iterate over the array
	//for( String oneItem : jsostrResponse ) {
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::" + jsostrResponse.size());
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::" + jsostrResponse.get(0));
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::" + jsostrResponse.get(1));

   

	    //String strResponse = jParser.getJSONFromUrl(url+access_token);

	    if (Globalconstant.LOG)
		Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::");
	    try {

		JSONArray jcols = new JSONArray(jsostrResponse.get(0));

		Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::" + jcols.length());
		
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

		    if (!lib.isNull(Globalconstant.IDENTIFIERS)) {
			JSONObject structure = (JSONObject) lib.get(Globalconstant.IDENTIFIERS);
			Log.d(Globalconstant.TAG, "IDENTIFIERS: " + lib.get(Globalconstant.IDENTIFIERS));

			values.put(DatabaseOpenHelper.PMID,	structure.optString(Globalconstant.PMID));
			values.put(DatabaseOpenHelper.DOI,structure.optString(Globalconstant.DOI));
			values.put(DatabaseOpenHelper.ISSN,	structure.optString(Globalconstant.ISSN));

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

			Uri uri_authors = context.getContentResolver().insert(MyContentProvider.CONTENT_URI_AUTHORS,authors_values);

		    }

		    values.put(DatabaseOpenHelper.AUTHORS, authors.substring(0,authors.length()-1));
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

		    Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_DOC_DETAILS, values);

		    //get_files_doc_id(doc_detail_id);

		}

	    } catch (Exception e) {
		if (Globalconstant.LOG) {
		    Log.e(Globalconstant.TAG,
			    "Got exception when parsing online data");
		    Log.e(Globalconstant.TAG, e.getClass().getSimpleName() + ": "+ e.getMessage());
		    // mendeleyDataSource.delete_author_table_by_doc_id(doc_detail_id);
		    // rollback á tabela de authores com o doc id respetivo
		}
	    }
	    
	    if(jsostrResponse.size() > 1 && !jsostrResponse.get(1).equals("meu") ){
	    	
	    	Log.e(Globalconstant.TAG , "CONTINUACAO!!!");
	    	Log.e(Globalconstant.TAG , "CONTINUACAO!!!" + jsostrResponse.get(1));
	    	GetUserLibrary(jsostrResponse.get(1));
	    	
	    }
	    
	

    }

    public void getFolders(String url) {

	ContentValues values = new ContentValues();

	if (Globalconstant.LOG)
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Folders:::::");
	Log.d(Globalconstant.TAG, url);

	JSONParser jParser = new JSONParser();
	// get JSON data from URL
	//String strResponse = jParser.getJSONFromUrl(url+access_token);
	List<String> jsostrResponse = new ArrayList<String>();
	jsostrResponse = jParser.getJSONFromUrl(url+access_token, false);


	// iterate over the array
	for( String oneItem : jsostrResponse ) {
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Folders:::::" + jsostrResponse.size());


	    try {

		JSONArray jcols = new JSONArray(oneItem);


		for (int i = 0; i < jcols.length(); i++) {

		    JSONObject lib = jcols.getJSONObject(i);

		    values.put(DatabaseOpenHelper.FOLDER_NAME,	lib.optString(Globalconstant.NAME));
		    values.put(DatabaseOpenHelper.FOLDER_PARENT,lib.optString(Globalconstant.PARENT_ID));
		    values.put(DatabaseOpenHelper.FOLDER_ID,lib.optString(Globalconstant.ID));
		    values.put(DatabaseOpenHelper.FOLDER_GROUP,	lib.optString(Globalconstant.GROUP));
		    values.put(DatabaseOpenHelper.FOLDER_ADDED,	lib.optString(Globalconstant.ADDED));

		    Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS, values);

		    get_docs_in_folders(lib.optString(Globalconstant.ID));

		}

	    } catch (Exception e) {
		if (Globalconstant.LOG) {
		    Log.e(Globalconstant.TAG, "Got exception when parsing online data");
		    Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
		}

	    }


	}
    }


    private void get_docs_in_folders(String folder_id){

	ContentValues values = new ContentValues();
	String auxurl = Globalconstant.get_docs_in_folders;
	String where= null;
	Log.d(Globalconstant.TAG, "access_token: " + access_token);
	//String [] selectionArgs = {};
	String url = auxurl.replace("id", folder_id) + access_token; 

	if (Globalconstant.LOG)
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Docs in Folders:::::");
	Log.d(Globalconstant.TAG, url);
	JSONParser jParser = new JSONParser();

	// get JSON data from URL
	List<String> jsostrResponse = new ArrayList<String>();
	jsostrResponse = jParser.getJSONFromUrl(url, false);



	if (jsostrResponse.size() > 0) {

	    try {

		JSONArray docs_ids = new JSONArray(jsostrResponse.get(0));

		for (int i = 0; i < docs_ids.length(); i++) {

		    JSONObject lib = docs_ids.getJSONObject(i);

		    String doc_id = lib.optString(Globalconstant.ID);

		    Log.d(Globalconstant.TAG, "DOC_ID: " + lib.optString(Globalconstant.ID));

		    values.put(DatabaseOpenHelper.FOLDER_ID, folder_id);
		    values.put(DatabaseOpenHelper.DOC_DETAILS_ID, lib.optString(Globalconstant.ID));

		    Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS_DOCS, values);
		    //values.put(DatabaseOpenHelper.FOLDER_ID, folder_id);
		    //where = DatabaseOpenHelper._ID + " = '" + doc_id + "'";

		    //Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");
		    //this.context.getContentResolver().update(uri, values, where, null);
		}

	    } catch (Exception e) {
		if (Globalconstant.LOG) {
		    Log.e(Globalconstant.TAG, "Got exception when parsing online data");
		    Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	    }
	}

    }




    private void get_files_doc_id(String doc_id){	

	ContentValues values = new ContentValues();

	String auxurl = Globalconstant.get_files_by_doc_id;
	Log.d(Globalconstant.TAG, "auxurl: " + auxurl);
	String url = auxurl.replace("doc_id", doc_id) + access_token; 

	Log.d(Globalconstant.TAG, ":::::::LoadData  - FILES::::: doc_id: " + doc_id);
	if (Globalconstant.LOG)
	    Log.d(Globalconstant.TAG, "url: " + url);
	JSONParser jParser = new JSONParser();
	// get JSON data from URL

	List<String> jsostrResponse = new ArrayList<String>();
	jsostrResponse = jParser.getJSONFromUrl(url, false);

	Log.d(Globalconstant.TAG, ":::::::LoadData  - FILES::::: doc_id: " + jsostrResponse.size());
	Log.d(Globalconstant.TAG, ":::::::LoadData  - FILES::::: doc_id: " + jsostrResponse.get(1));
	//String strResponse = jParser.getJSONFromUrl(url);
	if (jsostrResponse.size() > 0) {
	try {

	    JSONArray jcols = new JSONArray(jsostrResponse.get(0));	   

	    for (int i = 0; i < jcols.length(); i++) {

		JSONObject lib = jcols.getJSONObject(i);

		values.put(DatabaseOpenHelper.FILE_ID,lib.optString(Globalconstant.FILE_ID));		
		values.put(DatabaseOpenHelper.FILE_DOC_ID,lib.optString(Globalconstant.FILE_DOC_ID));		
		values.put(DatabaseOpenHelper.FILE_MIME_TYPE,lib.optString(Globalconstant.FILE_MIME_TYPE));		
		values.put(DatabaseOpenHelper.FILE_NAME,lib.optString(Globalconstant.FILE_NAME));		
		values.put(DatabaseOpenHelper.FILE_FILEHASH,lib.optString(Globalconstant.FILE_FILEHASH));

	    }
	} catch (Exception e) {
	    if (Globalconstant.LOG) {
		Log.e(Globalconstant.TAG, "Got exception when parsing online data");
		Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
	    }
	}

	Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FILES, values);
	}
    }


    public void getProfileInformation(String url) {
	ContentValues values = new ContentValues();

	Log.d(Globalconstant.TAG, url);
	JSONParser jParser = new JSONParser();

	// get JSON data from URL
	//String strResponse = jParser.getJSONFromUrl(url+access_token);

	List<String> jsostrResponse = new ArrayList<String>();

	// get JSON data from URL
	jsostrResponse = jParser.getJSONFromUrl(url+access_token, false);


	// iterate over the array
	for( String oneItem : jsostrResponse ) {
	    Log.d(Globalconstant.TAG, ":::::::LoadData  - Library:::::" + oneItem);


	    try {

		JSONObject jcols = new JSONObject(new String(oneItem));

		values.put(DatabaseOpenHelper.PROFILE_ID, jcols.optString(Globalconstant.ID));
		values.put(DatabaseOpenHelper.PROFILE_FIRST_NAME, jcols.optString(Globalconstant.FORENAME));	   
		values.put(DatabaseOpenHelper.PROFILE_LAST_NAME, jcols.optString(Globalconstant.SURNAME));	   
		values.put(DatabaseOpenHelper.PROFILE_DISPLAY_NAME,	jcols.optString(Globalconstant.PROFILE_DISPLAY_NAME));	   
		values.put(DatabaseOpenHelper.PROFILE_LINK,jcols.optString(Globalconstant.PROFILE_LINK));

	    } catch (Exception e) {
		if (Globalconstant.LOG) {
		    Log.e(Globalconstant.TAG, "Got exception when parsing online data");
		    Log.e(Globalconstant.TAG,e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	    }


	}






	Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_PROFILE, values);

    }




}

