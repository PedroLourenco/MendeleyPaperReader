package com.mendeleypaperreader.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.mendeleypaperreader.utl.Globalconstant;
import com.mendeleypaperreader.utl.SessionManager;

/**
 * Classname: DatabaseOpenHelper 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	public final static String TABLE_DOCUMENT_DETAILS = "document_details";
	public final static String TABLE_AUTHORS = "authors";
	public final static String TABLE_FOLDERS = "folders";
	public final static String TABLE_FILES = "files";
	public final static String TABLE_PROFILE = "profile";	
	public final static String TABLE_FOLDERS_DOCS = "folders_docs";	
	public final static String TABLE_CATALOG_DOCS = "catalog_docs";
	public final static String TABLE_ACADEMIC_STATUS_DOCS = "academic_status_docs";
	public final static String TABLE_COUNTRY_STATUS_DOCS = "country_status_docs";
	
	public final static String _ID = "_id";
	public final static String TYPE = "type";
	public final static String MONTH = "month";
	public final static String YEAR = "year";
	public final static String LAST_MODIFIED = "last_modified";
	public final static String DAY = "day";
	public final static String GROUP_ID = "group_id";
	public final static String SOURCE = "source";
	public final static String TITLE = "title";
	public final static String REVISION = "revision";
	public final static String IDENTIFIERS  = "identifiers";   //MAP
	public final static String PMID  = "pmid";   
	public final static String DOI  = "doi";   
	public final static String ISSN  = "issn"; 
	public final static String ARXIV  = "arxiv"; 
	public final static String ISBN  = "isbn"; 
	public final static String SCOPUS  = "scopus";
	public final static String SSN  = "ssn";  
	public final static String ABSTRACT = "abstract";
	public final static String PROFILE_ID = "profile_id";
	public final static String AUTHORS = "authors";        //ARRAY
	public final static String ADDED = "added";
	public final static String PAGES = "pages";
	public final static String VOLUME = "volume";
	public final static String ISSUE = "issue";
	public final static String WEBSITE = "website";
	public final static String PUBLISHER = "publisher";
	public final static String CITY = "city";
	public final static String EDITION = "edition";
	public final static String INSTITUTION = "institution";
	public final static String SERIES = "series";
	public final static String CHAPTER = "chapter";
	public final static String EDITORS = "editors";  // array
	public final static String READ = "read";
	public final static String STARRED = "starred";
	public final static String AUTHORED = "authored";
	public final static String CONFIRMED = "confirmed";
	public final static String HIDDEN = "hidden";
	public final static String IS_DOWNLOAD = "is_download";
	public final static String DOC_DETAILS_ID = "doc_details_id";
	public final static String AUTHOR_NAME = "author_name";
	public final static String READER_COUNT = "reader_count";	
	public final static String FOLDER_ID = "folder_id";
	public final static String FOLDER_NAME = "folder_name";
	public final static String FOLDER_ADDED = "folder_added";
	public final static String FOLDER_PARENT = "folder_parent";
	public final static String FOLDER_GROUP = "folder_group";
	public final static String ANNOTATION_COLOR = "color";
	public final static String ANNOTATION_POSITIONS = "positions";
	public final static String ANNOTATION_PRIVACY_LEVEL = "privacy_level";
	public final static String ANNOTATION_FILEHASH = "filehash";
	
	public final static String ANNOTATION_LAST_MODIFIED = "last_modified";
	public final static String ANNOTATION_CREATED = "created";
	public final static String ANNOTATION_TEXT = "text";
	public final static String FILE_ID = "file_id";
	public final static String FILE_NAME = "file_name";
	public final static String FILE_MIME_TYPE = "mime_type";
	public final static String FILE_DOC_ID = "document_id";
	public final static String FILE_FILEHASH = "filehash"; 
	//Table Profile colunms
	public final static String PROFILE_FIRST_NAME = "first_name";
	public final static String PROFILE_LAST_NAME = "last_name";
	public final static String PROFILE_DISPLAY_NAME = "display_name";
	public final static String PROFILE_LINK = "link";
	
	public final static String CATALOG_ID = "catalaog_id";
	public final static String SCORE = "score";
	public final static String STATUS = "status";
	public final static String COUNT = "count";
	public final static String COUNTRY = "country";
	
	

	final static String[] document_details_columns = { _ID, TYPE, MONTH, YEAR, LAST_MODIFIED, DAY, GROUP_ID, SOURCE, TITLE, REVISION, IDENTIFIERS, ABSTRACT, PROFILE_ID, AUTHORS, ADDED, PAGES, VOLUME, ISSUE, WEBSITE, PUBLISHER, CITY, EDITION, INSTITUTION, SERIES, CHAPTER, EDITORS, READ, STARRED, AUTHORED, CONFIRMED, HIDDEN};
	final static String[] document_titles_columns = {TITLE, AUTHORS, SOURCE, YEAR};

	final private static String CREATE_TABLE_AUTHORS = "CREATE TABLE authors (" + DOC_DETAILS_ID + " TEXT, "
			+ AUTHOR_NAME + " TEXT, PRIMARY KEY (" + DOC_DETAILS_ID +","+ AUTHOR_NAME +" ) ) ";
	
	final private static String CREATE_TABLE_FILE = "CREATE TABLE files (" + FILE_ID + " TEXT, "
			+ FILE_NAME + " TEXT, " + FILE_MIME_TYPE + " TEXT, " + FILE_DOC_ID + " TEXT, " + FILE_FILEHASH + " TEXT, PRIMARY KEY (" + FILE_ID +") ) ";
	
	final private static String CREATE_TABLE_PROFILE = "CREATE TABLE profile (" + PROFILE_ID + " TEXT, "
			+ PROFILE_FIRST_NAME + " TEXT, " + PROFILE_LAST_NAME + " TEXT, " + PROFILE_DISPLAY_NAME + " TEXT, " + PROFILE_LINK + " TEXT, PRIMARY KEY (" + PROFILE_ID +") ) ";
	
	
	final private static String CREATE_TABLE_FOLDERS = "CREATE TABLE folders (" + FOLDER_ID + " TEXT, " + FOLDER_ADDED + " TEXT, " + FOLDER_PARENT + " TEXT, " + FOLDER_GROUP + " TEXT, "
			+ FOLDER_NAME + " TEXT, PRIMARY KEY (" + FOLDER_ID + ") ) ";
	
	final private static String CREATE_TABLE_FOLDERS_DOCS = "CREATE TABLE folders_docs (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOLDER_ID + " TEXT, " + DOC_DETAILS_ID + " TEXT) ";
	

	final private static String CREATE_TABLE_CATALOG_DOCS = "CREATE TABLE catalog_docs (" + DOC_DETAILS_ID + " TEXT PRIMARY KEY, " + CATALOG_ID + " TEXT, " + SCORE + " TEXT) ";
	

	final private static String CREATE_TABLE_ACADEMIC_STATUS_DOCS = "CREATE TABLE academic_status_docs (" + DOC_DETAILS_ID + " TEXT, " + STATUS + " TEXT, " + COUNT + " TEXT) ";
	
	//final private static String CREATE_TABLE_COUNTRY_STATUS_DOCS = "CREATE TABLE country_status_docs (" + DOC_DETAILS_ID + " TEXT, " + COUNTRY + " TEXT, " + COUNT + " TEXT) ";
	
	
	final private static String CREATE_TABLE_DOCUMENT_DETAILS =

	"CREATE TABLE document_details (" + _ID + " TEXT PRIMARY KEY, "
			+ TYPE + " TEXT, "
			+ MONTH + " TEXT, "
			+ YEAR + " TEXT, "
			+ LAST_MODIFIED + " TEXT, "
			+ DAY + " TEXT, "
			+ GROUP_ID + " TEXT, "
			+ SOURCE + " TEXT, "
			+ TITLE + " TEXT, "
			+ REVISION + " TEXT, "
			+ PMID + " TEXT, "
			+ DOI + " TEXT, "
			+ ISSN + " TEXT, " 
			+ ARXIV + " TEXT, "
			+ ISBN + " TEXT, "
			+ SCOPUS + " TEXT, "
			+ SSN + " TEXT, "
			+ ABSTRACT + " TEXT, "
			+ PROFILE_ID + " TEXT, "
			+ AUTHORS + " TEXT, "
			+ ADDED + " TEXT, "
			+ PAGES + " TEXT, "
			+ VOLUME + " TEXT, "
			+ ISSUE + " TEXT, "
			+ WEBSITE + " TEXT, "
			+ PUBLISHER + " TEXT, "
			+ CITY + " TEXT, "
			+ EDITION + " TEXT, "
			+ INSTITUTION + " TEXT, "
			+ SERIES + " TEXT, "
			+ EDITORS + " TEXT, "
			+ READ + " TEXT, "
			+ STARRED + " TEXT, "
			+ AUTHORED + " TEXT, "
			+ CONFIRMED + " TEXT, "
			+ FOLDER_ID + " TEXT, "
			+ HIDDEN + " TEXT, "
			+ IS_DOWNLOAD + " TEXT, "
			+ READER_COUNT + " TEXT )";
			
			
			
	final private static String DATABASE_NAME = "Mendeley_library.db";
	final private static Integer VERSION = 2;
	final private Context mContext;
	
	public DatabaseOpenHelper(Context context, String name, 
            CursorFactory factory, int version) {
		super(context, DATABASE_NAME, factory, VERSION);
		this.mContext = context;
		
	}
	
	
	
	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
			
			if (Globalconstant.LOG)
				Log.e(Globalconstant.TAG, "DATABASE CREATE!!!!!!!");
			
			db.execSQL(CREATE_TABLE_DOCUMENT_DETAILS);
			db.execSQL(CREATE_TABLE_AUTHORS);
			db.execSQL(CREATE_TABLE_FOLDERS);
			db.execSQL(CREATE_TABLE_FILE);
			db.execSQL(CREATE_TABLE_PROFILE);
			db.execSQL(CREATE_TABLE_FOLDERS_DOCS);
			db.execSQL(CREATE_TABLE_ACADEMIC_STATUS_DOCS);
			//db.execSQL(CREATE_TABLE_COUNTRY_STATUS_DOCS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Release_2 version 0.2.0 - Add column reader_count to table documents_details 
		
		SessionManager session;
		session = new SessionManager(this.mContext);
		session.savePreferences("versioCode", "03");
		
		//Database verson 2
				db.execSQL("ALTER TABLE document_details ADD COLUMN reader_count TEXT ;");
				db.execSQL("ALTER TABLE document_details ADD COLUMN is_download TEXT ;");
				db.execSQL(CREATE_TABLE_ACADEMIC_STATUS_DOCS);
				//db.execSQL(CREATE_TABLE_COUNTRY_STATUS_DOCS);
				
				
				
				
	}

	void deleteDatabase() {
		mContext.deleteDatabase(DATABASE_NAME);
	}
	
	
	
	
}

