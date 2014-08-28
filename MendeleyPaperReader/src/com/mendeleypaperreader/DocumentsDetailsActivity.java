package com.mendeleypaperreader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mendeleypaperreader.db.DatabaseOpenHelper;
import com.mendeleypaperreader.utl.ConnectionDetector;
import com.mendeleypaperreader.utl.GetAccessToken;
import com.mendeleypaperreader.utl.Globalconstant;
import com.mendeleypaperreader.utl.MyContentProvider;
import com.mendeleypaperreader.utl.SessionManager;
import com.mendeleypaperreader.utl.SyncDataAsync;

/**
 * Classname: DocumentsDetailsActivity 
 * 	 
 * 
 * @date July 8, 2014
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

public class DocumentsDetailsActivity extends Activity  {

	// private Cursor mAdapter;
	private TextView doc_abstract, doc_url, doc_pmid, doc_issn, doc_catalog;
	private String mAbstract, t_doc_url, issn, doi, pmid, doc_title, doc_authors_text, doc_source_text;
	private static SessionManager session;
	private static String code;
	private static String refresh_token;
	private Boolean isInternetPresent = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_documents_details);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		doc_abstract = new TextView(this);
		doc_url = new TextView(this);
		doc_pmid = new TextView(this);
		doc_issn = new TextView(this);
		doc_catalog = new TextView(this);

		if(Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "DOC_DETAILS - doc_id: " + getDocId());

		//Get to populate activity
		fillData(getdocDetails());

		//Onlcick on abstract
		OnClickListener click_on_abstract = new OnClickListener() {

			public void onClick(View v) {
				Intent abstract_intent = new Intent(getApplicationContext(), AbstractDescriptionActivity.class);
				abstract_intent.putExtra("abstract", mAbstract);
				startActivity(abstract_intent);
			}
		};

		doc_abstract.setOnClickListener(click_on_abstract);

		//onclick on url link
		OnClickListener click_on_url = new OnClickListener() {

			public void onClick(View v) {
				Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(t_doc_url));
				internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
				internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(internetIntent);   
			}
		};

		doc_url.setOnClickListener(click_on_url);


		//onclick on PMID link
		OnClickListener click_on_pmid = new OnClickListener() {

			public void onClick(View v) {
				String url = Globalconstant.PMID_URL+pmid; 
				Intent internetIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
				internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
				internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(internetIntent);   
			}
		};



		doc_pmid.setOnClickListener(click_on_pmid);


		//onclick on ISSN link
		OnClickListener click_on_issn = new OnClickListener() {

			public void onClick(View v) {
				String url = Globalconstant.ISSN_URL+issn; 
				Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
				internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(internetIntent);   
			}
		};


		doc_issn.setOnClickListener(click_on_issn);

		//onclick on DOI link
		OnClickListener click_on_doi = new OnClickListener() {

			public void onClick(View v) {
				String url = Globalconstant.DOI_URL+doi; 
				Intent internetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				internetIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
				internetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(internetIntent);
			}
		};
		doc_catalog.setOnClickListener(click_on_doi);


		ImageView share = (ImageView) findViewById(R.id.share);

		//onclick on Share button link
		OnClickListener click_on_share_icon = new OnClickListener() {

			public void onClick(View v) {

				// check internet connection
				ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

				isInternetPresent = connectionDetector.isConnectingToInternet();

				if(isInternetPresent){
					onShareClick(v);
				}
				else{
					connectionDetector.showDialog(DocumentsDetailsActivity.this, ConnectionDetector.DEFAULT_DIALOG);
				}

			}
		};
		share.setOnClickListener(click_on_share_icon);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_refresh, menu);
		return super.onCreateOptionsMenu(menu);
	}



	//ActionBar Menu Options 
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			refreshToken();
			return true;


		default:
			return super.onOptionsItemSelected(item);
		}
	}






	private String getDocId(){

		Bundle bundle = getIntent().getExtras();
		return bundle.getString("doc_id");
	}


	private Cursor getdocDetails(){

		if(Globalconstant.LOG)
			Log.d(Globalconstant.TAG, "getdocDetails - DOC_DETAILS");

		String doc_id = getDocId();
		String[] projection = null;
		String selection = null;

		projection = new String[] {DatabaseOpenHelper.TYPE + " as _id",  DatabaseOpenHelper.TITLE, DatabaseOpenHelper.AUTHORS, DatabaseOpenHelper.SOURCE , DatabaseOpenHelper.YEAR, DatabaseOpenHelper.VOLUME, DatabaseOpenHelper.PAGES,DatabaseOpenHelper.ISSUE,  DatabaseOpenHelper.ABSTRACT, DatabaseOpenHelper.WEBSITE, DatabaseOpenHelper.DOI, DatabaseOpenHelper.PMID, DatabaseOpenHelper.ISSN, DatabaseOpenHelper.STARRED };
		selection = DatabaseOpenHelper._ID + " = '" + doc_id +"'";
		Uri  uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");

		return getApplicationContext().getContentResolver().query(uri, projection, selection, null, null);

	}


	private String getProfileSettings(String setting_name){

		String[] projection = null;
		String selection = null;

		projection = new String[] {setting_name + " as _id"};
		Uri uri = MyContentProvider.CONTENT_URI_PROFILE;

		Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, selection, null, null); 
		cursor.moveToPosition(0);

		return cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper._ID));

	}



	private void fillData(Cursor cursor){

		cursor.moveToPosition(0);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativedoc);
		relativeLayout.setBackgroundColor(Color.parseColor("#e5e5e5"));

		TextView doc_type = (TextView) findViewById(R.id.docype);
		doc_type.setText(cursor.getString(cursor.getColumnIndex("_id")).substring(0, 1).toUpperCase() + cursor.getString(cursor.getColumnIndex("_id")).substring(1)); 

		//Starred icon
		ImageView starred = (ImageView) findViewById(R.id.favorite_star);
		String aux_starred = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.STARRED)); 

		if(aux_starred.equals("true"))
			starred.setImageResource(R.drawable.ic_action_important);

		//Document Title
		TextView doc_tilte = new TextView(this);
		doc_tilte.setId(3);
		doc_title = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TITLE));
		doc_tilte.setText(doc_title);
		doc_tilte.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
		doc_tilte.setTypeface(null, Typeface.BOLD);
		doc_tilte.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);

		RelativeLayout.LayoutParams layout_doc_tilte = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_tilte.addRule(RelativeLayout.BELOW, R.id.docype);
		layout_doc_tilte.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_tilte.setLayoutParams(layout_doc_tilte);
		relativeLayout.addView(doc_tilte); 

		//Document Authors
		TextView doc_authors = new TextView(this);
		doc_authors.setId(4);
		doc_authors_text = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.AUTHORS));
		doc_authors.setText(doc_authors_text);
		doc_authors.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
		doc_authors.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		doc_authors.setTextColor(Color.parseColor("#000080"));
		RelativeLayout.LayoutParams layout_doc_authors = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_authors.addRule(RelativeLayout.BELOW, doc_tilte.getId());
		layout_doc_authors.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_authors.setLayoutParams(layout_doc_authors);
		relativeLayout.addView(doc_authors); 

		//Document Source
		TextView doc_source = new TextView(this);
		doc_source.setId(5);
		doc_source_text = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SOURCE));
		doc_source.setText(doc_source_text);
		doc_source.setTypeface(null, Typeface.BOLD);
		doc_source.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
		doc_source.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_source = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_source.addRule(RelativeLayout.BELOW, doc_authors.getId());
		layout_doc_source.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_source.setLayoutParams(layout_doc_source);
		relativeLayout.addView(doc_source); 

		//Document Year
		TextView doc_year = new TextView(this);
		doc_year.setId(6);

		String aux_year = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.YEAR));
		String aux_volume = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.VOLUME));
		String aux_pages = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PAGES));
		String aux_issue = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ISSUE));
		String aux_line = null;


		if(!aux_year.isEmpty() && !aux_volume.isEmpty() && aux_pages.isEmpty() && !aux_issue.isEmpty()){
			aux_line = aux_year +" vol." + aux_volume + " ("+ aux_issue +")";

		}else if (!aux_year.isEmpty() && !aux_volume.isEmpty() && !aux_pages.isEmpty() && aux_issue.isEmpty()){
			aux_line = aux_year +" vol." + aux_volume + " pp."+ aux_pages;

		}else if(!aux_year.isEmpty() && aux_volume.isEmpty() && !aux_pages.isEmpty() && !aux_issue.isEmpty()){
			aux_line = aux_year + " ("+ aux_issue +") pp."+ aux_pages;

		}else if(aux_year.isEmpty() && !aux_volume.isEmpty() && !aux_pages.isEmpty() && !aux_issue.isEmpty()){
			aux_line = "vol." + aux_volume + " ("+ aux_issue +") pp."+ aux_pages;

		}else {
			aux_line = aux_year +" vol." + aux_volume + " ("+ aux_issue +") pp."+ aux_pages;
		}


		doc_year.setText(aux_line);
		doc_year.setMaxLines(1);
		doc_year.setEllipsize(TruncateAt.END);
		doc_year.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
		doc_year.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_year = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_year.addRule(RelativeLayout.BELOW, doc_source.getId());
		layout_doc_year.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_year.setLayoutParams(layout_doc_year);
		relativeLayout.addView(doc_year); 


		//Line
		RelativeLayout relativeLayout_line = new RelativeLayout(this);
		relativeLayout_line.setId(7);
		relativeLayout_line.setBackgroundColor(Color.parseColor("#cccccc"));
		RelativeLayout.LayoutParams relativeLayout_lines = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,getResources().getDimensionPixelOffset(R.dimen.line_height));
		relativeLayout_lines.addRule(RelativeLayout.BELOW, doc_year.getId());
		relativeLayout_line.setLayoutParams(relativeLayout_lines);
		relativeLayout.addView(relativeLayout_line); 


		//Document Abstract
		mAbstract = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ABSTRACT));
		doc_abstract.setId(8);
		doc_abstract.setText(mAbstract);
		doc_abstract.setMaxLines(5);
		doc_abstract.setMinLines(1);
		doc_abstract.setEllipsize(TruncateAt.END);
		doc_abstract.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
		doc_abstract.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);

		//Resize arraw
		Drawable image = getApplicationContext().getResources().getDrawable(R.drawable.arrow);
		image.setBounds(0, 0, 20, 20);
		doc_abstract.setCompoundDrawables(null, null, image, null);
		RelativeLayout.LayoutParams layout_doc_abstract = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_abstract.addRule(RelativeLayout.BELOW, relativeLayout_line.getId());
		layout_doc_abstract.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_abstract.setLayoutParams(layout_doc_abstract);
		relativeLayout.addView(doc_abstract); 


		//Line
		RelativeLayout relativeLayout_line_f = new RelativeLayout(this);
		relativeLayout_line_f.setId(10);
		relativeLayout_line_f.setBackgroundColor(Color.parseColor("#cccccc"));
		RelativeLayout.LayoutParams relativeLayout_lines_f = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,getResources().getDimensionPixelOffset(R.dimen.line_height));
		relativeLayout_lines_f.addRule(RelativeLayout.BELOW, doc_abstract.getId());
		relativeLayout_line_f.setLayoutParams(relativeLayout_lines_f);
		relativeLayout.addView(relativeLayout_line_f); 


		/*
		//Document Tags
		TextView doc_tag_title = new TextView(this);
		doc_tag_title.setId(11);
		doc_tag_title.setText("TAGS");
		doc_tag_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
		doc_tag_title.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_tag_title = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_tag_title.addRule(RelativeLayout.BELOW, relativeLayout_line_f.getId());
		layout_doc_tag_title.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_tag_title.setLayoutParams(layout_doc_tag_title);
		relativeLayout.addView(doc_tag_title); 


		EditText doc_tags = new EditText(this);
		doc_tags.setId(12);
		doc_tags.setBackgroundColor(Color.WHITE);
		doc_tags.setClickable(true);
		doc_tags.setEnabled(false);
		doc_tags.setMinLines(1);
		doc_tags.setMaxLines(2);
		doc_tags.setEllipsize(TruncateAt.END);;
		doc_tags.setText("Tag for test");
		doc_tags.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
		doc_tags.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_tags = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_tags.addRule(RelativeLayout.BELOW, doc_tag_title.getId());
		layout_doc_tags.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_tags.setLayoutParams(layout_doc_tags);
		relativeLayout.addView(doc_tags); 

		//Document Notes
		TextView doc_note_title = new TextView(this);
		doc_note_title.setId(13);
		doc_note_title.setText("NOTES");
		doc_note_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
		doc_note_title.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_note_title = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_note_title.addRule(RelativeLayout.BELOW, doc_tags.getId());
		layout_doc_note_title.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_note_title.setLayoutParams(layout_doc_note_title);
		relativeLayout.addView(doc_note_title); 


		EditText doc_notes = new EditText(this);
		doc_notes.setId(14);
		doc_notes.setBackgroundColor(Color.WHITE);
		doc_notes.setClickable(true);
		doc_notes.setEnabled(false);
		doc_notes.setMinLines(1);
		doc_notes.setMaxLines(2);
		doc_notes.setEllipsize(TruncateAt.END);
		doc_notes.setText("Note for test");
		doc_notes.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
		doc_notes.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_notes = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_notes.addRule(RelativeLayout.BELOW, doc_note_title.getId());
		layout_doc_notes.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_notes.setLayoutParams(layout_doc_notes);
		relativeLayout.addView(doc_notes); 

		 */	

		issn = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ISSN));
		pmid = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PMID));
		doi = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.DOI));

		TextView doc_catalog_title = new TextView(this);

		if(!issn.isEmpty() || !doi.isEmpty() || !pmid.isEmpty()){

			//Document Catalog IDS
			doc_catalog_title.setId(15);
			doc_catalog_title.setText(getResources().getString(R.string.catalog_ids));
			doc_catalog_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
			doc_catalog_title.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_catalog_title = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_catalog_title.addRule(RelativeLayout.BELOW, relativeLayout_line_f.getId());
			layout_doc_catalog_title.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_catalog_title.setLayoutParams(layout_doc_catalog_title);
			relativeLayout.addView(doc_catalog_title);


			//Document Catalog DOI
			doc_catalog.setId(16);
			doc_catalog.setBackgroundColor(Color.WHITE);
			doc_catalog.setText(getResources().getString(R.string.doi) + "\t\t" + doi);
			doc_catalog.setMaxLines(1);
			doc_catalog.setEllipsize(TruncateAt.END);
			doc_catalog.setCompoundDrawables(null, null, image, null);
			doc_catalog.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
			doc_catalog.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_catalog = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_catalog.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_catalog.setLayoutParams(layout_doc_catalog);

			//Document Catalog PMID
			doc_pmid.setId(18);
			doc_pmid.setBackgroundColor(Color.WHITE);
			doc_pmid.setText(getResources().getString(R.string.pmid) + "\t\t" + pmid);
			doc_pmid.setMaxLines(1);
			doc_pmid.setEllipsize(TruncateAt.END);
			doc_pmid.setCompoundDrawables(null, null, image, null);
			doc_pmid.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
			doc_pmid.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_pmid = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_pmid.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_pmid.setLayoutParams(layout_doc_pmid);

			//Document Catalog ISSN
			doc_issn.setId(20);
			doc_issn.setBackgroundColor(Color.WHITE);
			doc_issn.setText(getResources().getString(R.string.issn) + "\t\t" + issn);
			doc_issn.setMaxLines(1);
			doc_issn.setEllipsize(TruncateAt.END);
			doc_issn.setCompoundDrawables(null, null, image, null);
			doc_issn.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
			doc_issn.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_issn = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_issn.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_issn.setLayoutParams(layout_doc_issn);


			if(!issn.isEmpty() && !doi.isEmpty() && !pmid.isEmpty()){

				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog.getId());

				//ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_pmid.getId());
				relativeLayout.addView(doc_issn);
			}


			else if(!issn.isEmpty() && !doi.isEmpty() && pmid.isEmpty()){

				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());

				//Document Catalog ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_catalog.getId());
				relativeLayout.addView(doc_issn);
			}

			else if(issn.isEmpty() && !doi.isEmpty() && !pmid.isEmpty()){

				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog.getId());
			}

			else if(!issn.isEmpty() && doi.isEmpty() && !pmid.isEmpty()){

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());

				//Document Catalog ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_pmid.getId());
				relativeLayout.addView(doc_issn);
			}

			else if(issn.isEmpty() && !doi.isEmpty() && pmid.isEmpty()){

				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
			}

			else if(issn.isEmpty() && doi.isEmpty() && !pmid.isEmpty()){

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
			}

			else if(!issn.isEmpty() && doi.isEmpty() && pmid.isEmpty()){

				//Document Catalog ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				relativeLayout.addView(doc_issn);
			}

		}


		t_doc_url = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.WEBSITE));
		TextView doc_url_title = new TextView(this);

		RelativeLayout.LayoutParams layout_doc_url_title;
		RelativeLayout.LayoutParams layout_doc_url;

		if(!t_doc_url.isEmpty()){

			//Document URL Title
			doc_url_title.setId(22);
			doc_url_title.setText(getResources().getString(R.string.urls));
			doc_url_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
			doc_url_title.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			layout_doc_url_title = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			doc_url_title.setLayoutParams(layout_doc_url_title);
			relativeLayout.addView(doc_url_title);

			//Document URL
			doc_url.setId(23);
			doc_url.setBackgroundColor(Color.WHITE);	    
			doc_url.setText(t_doc_url);
			doc_url.setMaxLines(1);
			doc_url.setEllipsize(TruncateAt.END);
			doc_url.setCompoundDrawables(null, null, image, null);
			doc_url.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_details));
			doc_url.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			layout_doc_url = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_url.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_url.setLayoutParams(layout_doc_url);
			layout_doc_url.addRule(RelativeLayout.BELOW, doc_url_title.getId());
			relativeLayout.addView(doc_url);



			if(!issn.isEmpty()){

				//Document URL Title
				layout_doc_url_title.addRule(RelativeLayout.BELOW, doc_issn.getId());
			}

			else if(issn.isEmpty() && !pmid.isEmpty()){

				//Document URL
				layout_doc_url_title.addRule(RelativeLayout.BELOW, doc_pmid.getId());
			}

			else if(!issn.isEmpty() && !pmid.isEmpty() && doi.isEmpty()){

				//Document URL
				layout_doc_url_title.addRule(RelativeLayout.BELOW, doc_catalog.getId());
			}
			else{
				//Document URL
				layout_doc_url_title.addRule(RelativeLayout.BELOW, relativeLayout_line_f.getId());
			}

		}
	}


	/*
	 * onShareClick - Click on shared icon 
	 * 
	 */

	public void onShareClick(View v) {
		Resources resources = getResources();
		String url = "";

		if(!t_doc_url.isEmpty()){

			url = t_doc_url;

		}else if(!issn.isEmpty() ){

			url  = Globalconstant.ISSN_URL+issn;	    
		}else if(!pmid.isEmpty()){
			url  = Globalconstant.PMID_URL+pmid;
		}else if(!doi.isEmpty()){
			url  = Globalconstant.DOI_URL+doi;

		}





		String email_text = resources.getString(R.string.email_text) + "<br/><br/><b>" + doc_title + "</b><br/><br/>" + resources.getString(R.string.email_authors) + doc_authors_text + "<br/><br/>" + resources.getString(R.string.email_publication) + doc_source_text + "<br/><br/>" + resources.getString(R.string.email_mendeley_profile) + getProfileSettings(DatabaseOpenHelper.PROFILE_LINK) + "<br/><br/>" + resources.getString(R.string.email_play_store) ;
		String email_subject_text = getProfileSettings(DatabaseOpenHelper.PROFILE_DISPLAY_NAME)  + resources.getString(R.string.email_subject);
		String sms_text = doc_title; 

		if(sms_text.length() > 85){

			sms_text = sms_text.substring(0, Math.min(sms_text.length(), 85)) + "...";
		}

		Intent emailIntent = new Intent();
		emailIntent.setAction(Intent.ACTION_SEND);
		// Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
		emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(email_text));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject_text);
		emailIntent.setType("message/rfc822");

		PackageManager pm = getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);     
		sendIntent.setType("text/plain");


		Intent openInChooser = Intent.createChooser(emailIntent, "Chooser Options");

		List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a LabeledIntent
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;
			if(packageName.contains("android.email")) {
				emailIntent.setPackage(packageName);
			} else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				if(packageName.contains("twitter")) {
					intent.putExtra(Intent.EXTRA_TEXT, sms_text +" "+ url);
				} else if(packageName.contains("facebook")) {
					// Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
					// One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
					// will show the <meta content ="..."> text from that page with our link in Facebook.
					intent.putExtra(Intent.EXTRA_TEXT, sms_text + " " + url);
				} else if(packageName.contains("mms")) {
					intent.putExtra(Intent.EXTRA_TEXT, sms_text +" "+ url);
				} else if(packageName.contains("android.gm")) {
					intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(email_text));
					intent.putExtra(Intent.EXTRA_SUBJECT, email_subject_text);               
					intent.setType("message/rfc822");
				}

				intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
			}
		}

		// convert intentList to array
		LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		startActivity(openInChooser);       
	}




	private void syncData(){

		new SyncDataAsync(DocumentsDetailsActivity.this, DocumentsDetailsActivity.this).execute();
	}


	private void refreshToken(){

		//delete data from data base and get new access token to start sync

		// check internet connection
		ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

		isInternetPresent = connectionDetector.isConnectingToInternet();

		if(isInternetPresent){
			getContentResolver().delete(MyContentProvider.CONTENT_URI_DELETE_DATA_BASE,null, null);
			new ProgressTask().execute();
		}
		else{
			connectionDetector.showDialog(DocumentsDetailsActivity.this, ConnectionDetector.DEFAULT_DIALOG);
		}

	}





	//AsyncTask to download DATA from server

	class ProgressTask extends AsyncTask<String, Integer, JSONObject> {


		protected void onPreExecute() {
			session = new SessionManager(DocumentsDetailsActivity.this); 
			code = session.LoadPreference("Code");
			refresh_token = session.LoadPreference("refresh_token");
		}

		protected void onPostExecute(final JSONObject json) {


			if (json != null) {
				try {
					String token = json.getString("access_token");
					String expire = json.getString("expires_in");
					String refresh = json.getString("refresh_token");

					// Save access token in shared preferences
					session.savePreferences("access_token", json.getString("access_token"));
					session.savePreferences("expires_in", json.getString("expires_in"));
					session.savePreferences("refresh_token", json.getString("refresh_token"));


					//Get data from server
					syncData();

					if (Globalconstant.LOG) {
						Log.d("refresh_token - Token Access", token);
						Log.d("refresh_token - Expire", expire);
						Log.d("refresh_token - Refresh", refresh);			
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}



		protected JSONObject doInBackground(final String... args) {

			GetAccessToken jParser = new GetAccessToken();

			JSONObject json = jParser.refresh_token(Globalconstant.TOKEN_URL, code, Globalconstant.CLIENT_ID, Globalconstant.CLIENT_SECRET, Globalconstant.REDIRECT_URI, Globalconstant.GRANT_TYPE, refresh_token);

			return json;
		} 
	}	  


}
