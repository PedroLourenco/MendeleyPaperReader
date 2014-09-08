package com.mendeleypaperreader.utl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendeleypaperreader.db.DatabaseOpenHelper;

/**
 * Classname: LoadData 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */



public class LoadData {

	private Context context;
	//private String doc_detail_id;
	private static SessionManager session;
	private static String access_token;
	public LoadData(Context context) {
		this.context = context;

		session = new SessionManager(this.context);  
		access_token = session.LoadPreference("access_token");


	}


	public void getFolders(String url){

		ContentValues values = new ContentValues();

		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);

		try {
			for( InputStream oneItem : link ) {
				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();

				while (ite.hasNext() ) {
					JsonNode temp = ite.next();

					if(temp.has(Globalconstant.ID)){
						values.put(DatabaseOpenHelper.FOLDER_ID,temp.get(Globalconstant.ID).asText());
					}

					if(temp.has(Globalconstant.NAME)){
						values.put(DatabaseOpenHelper.FOLDER_NAME,temp.get(Globalconstant.NAME).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_NAME, "");
					}

					if(temp.has(Globalconstant.PARENT_ID)){
						values.put(DatabaseOpenHelper.FOLDER_PARENT,temp.get(Globalconstant.PARENT_ID).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_PARENT, "");
					}

					if(temp.has(Globalconstant.ADDED)){
						values.put(DatabaseOpenHelper.FOLDER_ADDED,temp.get(Globalconstant.ADDED).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_ADDED, "");
					}
					if(temp.has(Globalconstant.GROUP)){
						values.put(DatabaseOpenHelper.FOLDER_GROUP,temp.get(Globalconstant.GROUP).asText());
					}else{
						values.put(DatabaseOpenHelper.FOLDER_GROUP, "");
					}

					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS, values);
					getDocsInFolder(temp.get(Globalconstant.ID).asText());	
				}

				jp.close();
			}



		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public void getUserLibrary(String url) {
		int startTime = (int) System.currentTimeMillis();
		ContentValues values = new ContentValues();
		ContentValues authors_values = new ContentValues();
		JSONParser jParser = new JSONParser();
		String docTitle ;
		String docId = null ;

		List<InputStream> link = new ArrayList<InputStream>();

		ObjectMapper mapper = new ObjectMapper();

		JsonFactory factory = mapper.getFactory();
		link = jParser.getJACKSONFromUrl(url,true);

		try {

			for( InputStream oneItem : link ) {

				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();
				while (ite.hasNext() ) {

					JsonNode temp = ite.next();

					if(temp.has(Globalconstant.ID)){
						docId = temp.get(Globalconstant.ID).asText();
						values.put(DatabaseOpenHelper._ID,docId);
					}

					if(temp.has(Globalconstant.TITLE)){
						docTitle = temp.get(Globalconstant.TITLE).asText();
						values.put(DatabaseOpenHelper.TITLE,docTitle);
					}else{
						docTitle = "";
						values.put(DatabaseOpenHelper.TITLE, docTitle);
					}

					if(temp.has(Globalconstant.TYPE)){
						values.put(DatabaseOpenHelper.TYPE,temp.get(Globalconstant.TYPE).asText());
					}else{
						values.put(DatabaseOpenHelper.TYPE, "");
					}

					if(temp.has(Globalconstant.MONTH)){
						values.put(DatabaseOpenHelper.MONTH,temp.get(Globalconstant.MONTH).asText());
					}else{
						values.put(DatabaseOpenHelper.MONTH, "");
					}
					if(temp.has(Globalconstant.YEAR)){
						values.put(DatabaseOpenHelper.YEAR,temp.get(Globalconstant.YEAR).asText());
					}else{
						values.put(DatabaseOpenHelper.YEAR, "");
					}
					if(temp.has(Globalconstant.LAST_MODIFIED)){
						values.put(DatabaseOpenHelper.LAST_MODIFIED,temp.get(Globalconstant.LAST_MODIFIED).asText());
					}else{
						values.put(DatabaseOpenHelper.LAST_MODIFIED, "");
					}

					/*if(temp.has(Globalconstant.DAY)){
						values.put(DatabaseOpenHelper.DAY,temp.get(Globalconstant.DAY).asText());
					}else{
						values.put(DatabaseOpenHelper.DAY, "");
					}
					*/
					if(temp.has(Globalconstant.GROUP_ID)){
						values.put(DatabaseOpenHelper.GROUP_ID,temp.get(Globalconstant.GROUP_ID).asText());
					}else{
						values.put(DatabaseOpenHelper.GROUP_ID, "");
					}
					if(temp.has(Globalconstant.SOURCE)){
						values.put(DatabaseOpenHelper.SOURCE,temp.get(Globalconstant.SOURCE).asText());
					}else{
						values.put(DatabaseOpenHelper.SOURCE, "");
					}
					/*
					if(temp.has(Globalconstant.REVISION)){
						values.put(DatabaseOpenHelper.REVISION,temp.get(Globalconstant.REVISION).asText());
					}else{
						values.put(DatabaseOpenHelper.REVISION, "");
					}
					*/
					if(temp.has(Globalconstant.PAGES)){
						values.put(DatabaseOpenHelper.PAGES, temp.get(Globalconstant.PAGES).asText());
					}else{
						values.put(DatabaseOpenHelper.PAGES, "");
					}

					if(temp.has(Globalconstant.VOLUME)){
						values.put(DatabaseOpenHelper.VOLUME, temp.get(Globalconstant.VOLUME).asText());
					}else{
						values.put(DatabaseOpenHelper.VOLUME, "");
					}
					if(temp.has(Globalconstant.ISSUE)){
						values.put(DatabaseOpenHelper.ISSUE, temp.get(Globalconstant.ISSUE).asText());
					}else{
						values.put(DatabaseOpenHelper.ISSUE, "");
					}
					//if(temp.has(Globalconstant.WEBSITE)){
					//	values.put(DatabaseOpenHelper.WEBSITE, temp.get(Globalconstant.WEBSITE).asText());
					//}else{
					//	values.put(DatabaseOpenHelper.WEBSITE, "");
					//
					/*
					if(temp.has(Globalconstant.PUBLISHER)){
						values.put(DatabaseOpenHelper.PUBLISHER, temp.get(Globalconstant.PUBLISHER).asText());
					}else{
						values.put(DatabaseOpenHelper.PUBLISHER, "");
					}
					
					if(temp.has(Globalconstant.CITY)){
						values.put(DatabaseOpenHelper.CITY, temp.get(Globalconstant.CITY).asText());
					}else{
						values.put(DatabaseOpenHelper.CITY, "");
					}
					
					if(temp.has(Globalconstant.EDITION)){
						values.put(DatabaseOpenHelper.EDITION, temp.get(Globalconstant.EDITION).asText());
					}else{
						values.put(DatabaseOpenHelper.EDITION, "");
					}
					
					if(temp.has(Globalconstant.INSTITUTION)){
						values.put(DatabaseOpenHelper.INSTITUTION, temp.get(Globalconstant.INSTITUTION).asText());
					}else{
						values.put(DatabaseOpenHelper.INSTITUTION, "");
					}
					
					if(temp.has(Globalconstant.SERIES)){
						values.put(DatabaseOpenHelper.SERIES, temp.get(Globalconstant.SERIES).asText());
					}else{
						values.put(DatabaseOpenHelper.SERIES, "");
					}
					
					if(temp.has(Globalconstant.EDITORS)){
						values.put(DatabaseOpenHelper.EDITORS, temp.get(Globalconstant.EDITORS).asText());
					}else{
						values.put(DatabaseOpenHelper.EDITORS, "");
					}
					
					if(temp.has(Globalconstant.READ)){
						values.put(DatabaseOpenHelper.READ, temp.get(Globalconstant.READ).asText());
					}else{
						values.put(DatabaseOpenHelper.READ, "");
					}
					*/
					if(temp.has(Globalconstant.STARRED)){
						values.put(DatabaseOpenHelper.STARRED, temp.get(Globalconstant.STARRED).asText());
					}else{
						values.put(DatabaseOpenHelper.STARRED, "");
					}
					if(temp.has(Globalconstant.AUTHORED)){
						values.put(DatabaseOpenHelper.AUTHORED, temp.get(Globalconstant.AUTHORED).asText());

					}else{
						values.put(DatabaseOpenHelper.AUTHORED, "");
					}
					/*
					if(temp.has(Globalconstant.CONFIRMED)){
						values.put(DatabaseOpenHelper.CONFIRMED, temp.get(Globalconstant.CONFIRMED).asText());
					}else{
						values.put(DatabaseOpenHelper.CONFIRMED, "");
					}
					if(temp.has(Globalconstant.HIDDEN)){
						values.put(DatabaseOpenHelper.HIDDEN, temp.get(Globalconstant.HIDDEN).asText());
					}else{
						values.put(DatabaseOpenHelper.HIDDEN, "");
					}
					*/
					if(temp.has(Globalconstant.ABSTRACT)){
						values.put(DatabaseOpenHelper.ABSTRACT, temp.get(Globalconstant.ABSTRACT).asText());
					}else{
						values.put(DatabaseOpenHelper.ABSTRACT, "");
					}

					//Array
					//authors":[{"first_name":"Asger","last_name":"Hobolth"},{"first_name":"Ole F","last_name":"Christensen"},{"first_name":"Thomas","last_name":"Mailund"},{"first_name":"Mikkel H","last_name":"Schierup"}]
					if(temp.has(Globalconstant.AUTHORS)){	
						Iterator<JsonNode> authorsIterator = temp.get(Globalconstant.AUTHORS).elements();
						String authors = "";
						while (authorsIterator.hasNext() ){

							JsonNode author = authorsIterator.next();
							author.get(Globalconstant.FORENAME);
							author.get(Globalconstant.SURNAME);

							String author_name = author.get(Globalconstant.FORENAME).asText()	+ " "+ author.get(Globalconstant.SURNAME).asText();

							authors += author_name + ",";
							values.put(DatabaseOpenHelper.AUTHORS, authors.substring(0,authors.length()-1));

							authors_values.put(DatabaseOpenHelper.DOC_DETAILS_ID,temp.get("id").asText());
							authors_values.put(DatabaseOpenHelper.AUTHOR_NAME,author_name);

							Uri uri_authors = context.getContentResolver().insert(MyContentProvider.CONTENT_URI_AUTHORS,authors_values);

						}
					}else{
						values.put(DatabaseOpenHelper.AUTHORS, "");
					}

					//"identifiers":{"pmid":"17319744","doi":"10.1371/journal.pgen.0030007","issn":"1553-7404"}
					//String pmid = "";
					//String doi = "";
					//String issn = "";
					
					if(temp.has(Globalconstant.IDENTIFIERS)){	

						Iterator<Entry<String, JsonNode>> identifierIterator = temp.get(Globalconstant.IDENTIFIERS).fields();

						values.put(DatabaseOpenHelper.ISSN, "");
					    values.put(DatabaseOpenHelper.ISBN, "");
						values.put(DatabaseOpenHelper.PMID, "");
						values.put(DatabaseOpenHelper.SCOPUS, "");
						values.put(DatabaseOpenHelper.SSN, "");
						values.put(DatabaseOpenHelper.ARXIV, "");
						values.put(DatabaseOpenHelper.DOI, "");

						while (identifierIterator.hasNext() ){

							Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) identifierIterator.next();
							
							
							/*
							if(entry.getKey().equals("pmid")){
								pmid = entry.getValue().asText();
							}else if (entry.getKey().equals("doi")){
								doi = entry.getValue().asText();	
							}else if (entry.getKey().equals("issn")){
								issn = entry.getValue().asText();	
							}
							*/
							
							values.put(entry.getKey(),entry.getValue().asText());
						}
					}else{
						values.put(DatabaseOpenHelper.ISSN, "");
						values.put(DatabaseOpenHelper.ISBN, "");
						values.put(DatabaseOpenHelper.PMID, "");
						values.put(DatabaseOpenHelper.SCOPUS, "");
						values.put(DatabaseOpenHelper.SSN, "");
						values.put(DatabaseOpenHelper.ARXIV, "");
						values.put(DatabaseOpenHelper.DOI, "");
					}

					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_DOC_DETAILS, values);
					
				}
				jp.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
int executionTime = (int) (System.currentTimeMillis() - startTime);
		
		Log.i(Globalconstant.TAG, "Execute Time - getLIBRARY:" + executionTime);
		
	}




	private void getDocsInFolder(String folderId){

		ContentValues values = new ContentValues();

		String auxurl = Globalconstant.get_docs_in_folders;
		String url = auxurl.replace("id", folderId) + access_token; 

		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);


		try {

			for( InputStream oneItem : link ) {
				JsonParser jp = factory.createParser(oneItem);	
				JsonNode rootNode = mapper.readTree(jp);

				Iterator<JsonNode> ite = rootNode.iterator();
				while (ite.hasNext() ) {
					JsonNode temp = ite.next();

					if(temp.has(Globalconstant.ID)){

						values.put(DatabaseOpenHelper.FOLDER_ID, folderId);
						values.put(DatabaseOpenHelper.DOC_DETAILS_ID, temp.get(Globalconstant.ID).asText());

					}else{
						values.put(DatabaseOpenHelper.FOLDER_ID, "");
						values.put(DatabaseOpenHelper.DOC_DETAILS_ID, "");

					}
					Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLDERS_DOCS, values);

				}
				jp.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


private Cursor getDocInfo2(){

		
		String[] projection = null;
		String selection = null;
		String orderBy = null;
		Cursor query;
		
		projection = new String[] {DatabaseOpenHelper._ID + " as _id",  DatabaseOpenHelper.PMID,DatabaseOpenHelper.DOI, DatabaseOpenHelper.ISSN, DatabaseOpenHelper.ISBN, DatabaseOpenHelper.SCOPUS, DatabaseOpenHelper.ARXIV};
		
		Uri  uri = MyContentProvider.CONTENT_URI_DOC_DETAILS;

		int startTime = (int) System.currentTimeMillis();
		query = this.context.getContentResolver().query(uri, projection, selection, null, orderBy);
		int executionTime = (int) (System.currentTimeMillis() - startTime);
		
		
		
		return query;

	}
	
	
	
	

public void getCatalogId2(){

	ContentValues values = new ContentValues();
	ContentValues academic_docs_values = new ContentValues();
	//ContentValues country_docs_values = new ContentValues();
	Cursor cursorDocs;
	String urlfilter = null;
	//String counter = null;
	boolean toProcess = true;
	Uri uri_ = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");

	cursorDocs = getDocInfo2();

	while(cursorDocs.moveToNext()){

		String docId = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper._ID));
		String pmid = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.PMID));
		String doi = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.DOI));
		String issn = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.ISSN));
		String isbn = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.ISBN));
		String scopus = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.SCOPUS));
		String arxiv = cursorDocs.getString(cursorDocs.getColumnIndex(DatabaseOpenHelper.ARXIV));

		String where = DatabaseOpenHelper._ID + " = '" + docId + "'";
		String where2 = DatabaseOpenHelper._ID + " = '" + docId + "' and " + DatabaseOpenHelper.READER_COUNT + " IS NULL";

		
		if(!pmid.isEmpty()){
			toProcess = true;
			urlfilter = "pmid=" + pmid;		
		}else if(!doi.isEmpty()){	
			toProcess = true;
			urlfilter = "doi=" + doi;	
		}else if(!issn.isEmpty()){	
			toProcess = true;
			urlfilter = "issn=" + issn;	
		}else if(!doi.isEmpty()){	
			toProcess = true;
			urlfilter = "isbn=" + isbn;	
		} else if(!scopus.isEmpty()){
			toProcess = true;
			urlfilter = "scopus=" + scopus;	
		}else if(!arxiv.isEmpty()){		
			toProcess = true;
			urlfilter = "arxiv=" + arxiv;	
		}else{
			toProcess = false;
			//update table
			
			values.put(DatabaseOpenHelper.READER_COUNT, "0");	
			
			this.context.getContentResolver().update(uri_, values, where, null);
			
		}


		if(toProcess){

			String url = Globalconstant.get_catalog_url + urlfilter + "&view=stats&access_token=" + access_token;
			
			if(Globalconstant.LOG)
				Log.d(Globalconstant.TAG, "getCatalogId url: " + url);

			JSONParser jParser = new JSONParser();
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory factory = mapper.getFactory(); 
			List<InputStream> link = new ArrayList<InputStream>();
			link = jParser.getJACKSONFromUrl(url,false);
			
			try {

				for( InputStream oneItem : link ) {
					JsonParser jp = factory.createParser(oneItem);	
					JsonNode rootNode = mapper.readTree(jp);
				
					
					Iterator<JsonNode> ite = rootNode.iterator();

					while (ite.hasNext() ) {
						JsonNode temp = ite.next();
			
				if (temp.has("link")){
					values.put(DatabaseOpenHelper.WEBSITE, temp.get("link").asText());
				}

				if (temp.has("reader_count")){
		
					values.put(DatabaseOpenHelper.READER_COUNT, temp.get("reader_count").asText());
				}
				
	
				//update table
				
				this.context.getContentResolver().update(uri_, values, where, null);
					
					
				if (temp.has("reader_count_by_academic_status")){

						Iterator<Entry<String, JsonNode>> identifierIterator = temp.get("reader_count_by_academic_status").fields();

						while (identifierIterator.hasNext() ){

							Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) identifierIterator.next();

							academic_docs_values.put(DatabaseOpenHelper.DOC_DETAILS_ID, docId);
							academic_docs_values.put(DatabaseOpenHelper.STATUS,entry.getKey());
							academic_docs_values.put(DatabaseOpenHelper.COUNT,entry.getValue().asText());
							Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_ACADEMIC_DOCS, academic_docs_values); 
						}
					}

				/*
					if (temp.has("reader_count_by_country")){

						Iterator<Entry<String, JsonNode>> identifierIterator = temp.get("reader_count_by_country").fields();

						while (identifierIterator.hasNext() ){

							Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) identifierIterator.next();

							country_docs_values.put(DatabaseOpenHelper.DOC_DETAILS_ID, docId);
							country_docs_values.put(DatabaseOpenHelper.COUNTRY,entry.getKey());
							country_docs_values.put(DatabaseOpenHelper.COUNT,entry.getValue().asText());
							Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_COUNTRY_DOCS, country_docs_values); 
						}
					}
					*/
				}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		values.put(DatabaseOpenHelper.READER_COUNT, "0");
		values.put(DatabaseOpenHelper.WEBSITE, "");
		this.context.getContentResolver().update(uri_, values, where2, null);
		
	}
}



	public void getProfileInfo(String url){

		ContentValues values = new ContentValues();


		JSONParser jParser = new JSONParser();
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory(); 
		List<InputStream> link = new ArrayList<InputStream>();
		link = jParser.getJACKSONFromUrl(url,true);

		try {

			for( InputStream oneItem : link ) {

				Map<String, Object> mapObject = mapper.readValue(oneItem, new TypeReference<Map<String, Object>>() {});

				values.put(DatabaseOpenHelper.PROFILE_ID, mapObject.get(Globalconstant.ID).toString());
				values.put(DatabaseOpenHelper.PROFILE_FIRST_NAME, mapObject.get(Globalconstant.FORENAME).toString());	   
				values.put(DatabaseOpenHelper.PROFILE_LAST_NAME, mapObject.get(Globalconstant.SURNAME).toString() );	   
				values.put(DatabaseOpenHelper.PROFILE_DISPLAY_NAME,	mapObject.get(Globalconstant.PROFILE_DISPLAY_NAME).toString());	   
				values.put(DatabaseOpenHelper.PROFILE_LINK,mapObject.get(Globalconstant.PROFILE_LINK).toString());

				Uri uri = this.context.getContentResolver().insert(MyContentProvider.CONTENT_URI_PROFILE, values);
				
				
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}






}

