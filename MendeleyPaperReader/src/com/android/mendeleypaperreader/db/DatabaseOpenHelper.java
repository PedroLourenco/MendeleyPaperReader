package com.android.mendeleypaperreader.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.mendeleypaperreader.utl.Globalconstant;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	public final static String TABLE_DOCUMENT_DETAILS = "document_details";
	public final static String TABLE_AUTHORS = "authors";
	public final static String TABLE_FOLDERS = "folders";
	
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
	public final static String DOC_DETAILS_ID = "doc_details_id";
	public final static String AUTHOR_NAME = "author_name";
	public final static String FOLDER_ID = "folder_id";
	public final static String FOLDER_NAME = "folder_name";
	public final static String FOLDER_ADDED = "folder_added";
	public final static String FOLDER_PARENT = "folder_parent";
	public final static String FOLDER_GROUP = "folder_group";
	
	
	final static String[] document_details_columns = { _ID, TYPE, MONTH, YEAR, LAST_MODIFIED, DAY, GROUP_ID, SOURCE, TITLE, REVISION, IDENTIFIERS, ABSTRACT, PROFILE_ID, AUTHORS, ADDED, PAGES, VOLUME, ISSUE, WEBSITE, PUBLISHER, CITY, EDITION, INSTITUTION, SERIES, CHAPTER, EDITORS, READ, STARRED, AUTHORED, CONFIRMED, HIDDEN};
	final static String[] document_titles_columns = {TITLE, AUTHORS, SOURCE, YEAR};

	final private static String CREATE_TABLE_AUTHORS = "CREATE TABLE authors (" + DOC_DETAILS_ID + " TEXT, "
			+ AUTHOR_NAME + " TEXT, PRIMARY KEY (" + DOC_DETAILS_ID +","+ AUTHOR_NAME +" ) ) ";
	
	final private static String CREATE_TABLE_FOLDERS = "CREATE TABLE folders (" + FOLDER_ID + " TEXT, " + FOLDER_ADDED + " TEXT, " + FOLDER_PARENT + " TEXT, " + FOLDER_GROUP + " TEXT, "
			+ FOLDER_NAME + " TEXT, PRIMARY KEY (" + FOLDER_ID + ") ) ";
	
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
			+ HIDDEN + " TEXT )";
			
			
			
	final private static String DATABASE_NAME = "Mendeley_library.db";
	final private static Integer VERSION = 1;
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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(DATABASE_NAME);
	}
}

