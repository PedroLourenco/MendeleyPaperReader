package com.android.mendeleypaperreader.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	final static String TABLE_DOCUMENT_DETAILS = "document_details";
	final static String TABLE_AUTHORS = "authors";
	
	final static String _ID = "_id";
	final static String TYPE = "type";
	final static String MONTH = "month";
	final static String YEAR = "year";
	final static String LAST_MODIFIED = "last_modified";
	final static String DAY = "day";
	final static String GROUP_ID = "group_id";
	final static String SOURCE = "source";
	final static String TITLE = "title";
	final static String REVISION = "revision";
	final static String IDENTIFIERS  = "identifiers";   //MAP
	final static String PMID  = "pmid";   //MAP
	final static String DOI  = "doi";   //MAP
	final static String ISSN  = "issn";   //MAP
	final static String ABSTRACT = "abstract";
	final static String PROFILE_ID = "profile_id";
	final static String AUTHORS = "authors";        //ARRAY
	final static String ADDED = "added";
	final static String PAGES = "pages";
	final static String VOLUME = "volume";
	final static String ISSUE = "issue";
	final static String WEBSITE = "website";
	final static String PUBLISHER = "publisher";
	final static String CITY = "city";
	final static String EDITION = "edition";
	final static String INSTITUTION = "institution";
	final static String SERIES = "series";
	final static String CHAPTER = "chapter";
	final static String EDITORS = "editors";  // array
	final static String READ = "read";
	final static String STARRED = "starred";
	final static String AUTHORED = "authored";
	final static String CONFIRMED = "confirmed";
	final static String HIDDEN = "hidden";	
	final static String DOC_DETAILS_ID = "doc_details_id";
	final static String AUTHOR_NAME = "author_name";
	
	
	final static String[] document_details_columns = { _ID, TYPE, MONTH, YEAR, LAST_MODIFIED, DAY, GROUP_ID, SOURCE, TITLE, REVISION, IDENTIFIERS, ABSTRACT, PROFILE_ID, AUTHORS, ADDED, PAGES, VOLUME, ISSUE, WEBSITE, PUBLISHER, CITY, EDITION, INSTITUTION, SERIES, CHAPTER, EDITORS, READ, STARRED, AUTHORED, CONFIRMED, HIDDEN};

	final private static String CREATE_TABLE_AUTHORS = "CREATE TABLE authors (" + DOC_DETAILS_ID + " TEXT, "
			+ AUTHOR_NAME + " TEXT, PRIMARY KEY (" + DOC_DETAILS_ID +","+ AUTHOR_NAME +" ) ) ";
	
	
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

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_DOCUMENT_DETAILS);
		db.execSQL(CREATE_TABLE_AUTHORS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(DATABASE_NAME);
	}
}

