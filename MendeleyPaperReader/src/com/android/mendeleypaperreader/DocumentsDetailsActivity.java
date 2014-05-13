package com.android.mendeleypaperreader;

import com.android.mendeleypaperreader.db.DatabaseOpenHelper;
import com.android.mendeleypaperreader.utl.Globalconstant;
import com.android.mendeleypaperreader.utl.MyContentProvider;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DocumentsDetailsActivity extends Activity  {
	
	Cursor mAdapter;
	TextView doc_abstract; 

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   
	    doc_abstract = new TextView(this);
	   
	    Log.d(Globalconstant.TAG, "DOC_DETAILS - doc_id: " + getDocId());
	   
	    
	    fillData(getdocDetails());
	    
	    
	    
	    
	    OnClickListener Click_on_abstract = new OnClickListener() {

	        public void onClick(View v) {
	            // TODO Open new activity with abstract
	            
	            Log.d(Globalconstant.TAG, "CLick on abstract");
	            
	            
	        }
	    };
	    
	    doc_abstract.setOnClickListener(Click_on_abstract);
	}
	
	
	
	
	private String getDocId(){
		
	
		Bundle bundle = getIntent().getExtras();
		
		String doc_id = bundle.getString("doc_id");
		
		return doc_id;
	}



	
	private Cursor getdocDetails(){
		
		Log.d(Globalconstant.TAG, "getdocDetails - DOC_DETAILS");
		String doc_id = getDocId();
		
		String[] projection = null;
		String selection = null;
		
		projection = new String[] {DatabaseOpenHelper.TYPE + " as _id",  DatabaseOpenHelper.TITLE, DatabaseOpenHelper.AUTHORS, DatabaseOpenHelper.SOURCE , DatabaseOpenHelper.YEAR, DatabaseOpenHelper.VOLUME, DatabaseOpenHelper.PAGES,DatabaseOpenHelper.ISSUE,  DatabaseOpenHelper.ABSTRACT, DatabaseOpenHelper.WEBSITE, DatabaseOpenHelper.DOI, DatabaseOpenHelper.PMID, DatabaseOpenHelper.ISSN };
		selection = DatabaseOpenHelper._ID + " = '" + doc_id +"'";
		Uri  uri = Uri.parse(MyContentProvider.CONTENT_URI_DOC_DETAILS + "/id");
		
		 Log.d(Globalconstant.TAG, "onCreateLoader - DOC_DETAILS");
		 
		  return getApplicationContext().getContentResolver().query(uri, projection, selection, null, null);
		
	}
	
	
	private void fillData(Cursor cursor){
		
		 Log.d(Globalconstant.TAG, "fillData - DOC_DETAILS - " + cursor.getCount());
		
		 cursor.moveToPosition(0);
		 
		 ScrollView scrollView = new ScrollView(this);
		 RelativeLayout relativeLayout = new RelativeLayout(this);
		 relativeLayout.setBackgroundColor(Color.parseColor("#e5e5e5"));

		 //Download button
		ImageView downloadButton = new ImageView(this);
		downloadButton.setId(1);
		downloadButton.setImageResource(R.drawable.download);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.image_size), getResources().getDimensionPixelSize(R.dimen.image_size));
		lp.setMargins(getResources().getDimensionPixelOffset(R.dimen.pading), getResources().getDimensionPixelOffset(R.dimen.pading), 0, 0);
		downloadButton.setLayoutParams(lp);
		relativeLayout.addView(downloadButton); 
		
		
		//Text for button
		TextView button_title = new TextView(this);
		
		button_title.setText("Download Article");
		button_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.d_button_text_size));
		RelativeLayout.LayoutParams layout_button_title = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout_button_title.setMargins(getResources().getDimensionPixelOffset(R.dimen.d_button_margin_left), getResources().getDimensionPixelOffset(R.dimen.d_button_margin_top), 0, 0);
		button_title.setLayoutParams(layout_button_title);
		relativeLayout.addView(button_title); 
		
		
		//Doc Type
		TextView doc_type = new TextView(this);
		
		doc_type.setId(2);
		doc_type.setText(cursor.getString(cursor.getColumnIndex("_id")));
		doc_type.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.d_button_text_size));
		doc_type.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			doc_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_bg_list));
		} else {
			doc_type.setBackground(getResources().getDrawable(R.drawable.gradient_bg_list));
		}
		
		RelativeLayout.LayoutParams layout_doc_type = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_type.addRule(RelativeLayout.BELOW, downloadButton.getId());
		layout_doc_type.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_type.setLayoutParams(layout_doc_type);
		relativeLayout.addView(doc_type); 
		
		
		//Document Title
		TextView doc_tilte = new TextView(this);
		doc_tilte.setId(3);
		doc_tilte.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.TITLE)));
		doc_tilte.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.doc_title_size));
		doc_tilte.setTypeface(null, Typeface.BOLD);
		doc_tilte.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		
		RelativeLayout.LayoutParams layout_doc_tilte = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_tilte.addRule(RelativeLayout.BELOW, doc_type.getId());
		layout_doc_tilte.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
		doc_tilte.setLayoutParams(layout_doc_tilte);
		relativeLayout.addView(doc_tilte); 
		
		//Document Authors
		TextView doc_authors = new TextView(this);
		doc_authors.setId(4);
		doc_authors.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.AUTHORS)));
		doc_authors.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.authors_size));
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
		doc_source.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SOURCE)));
		doc_source.setTypeface(null, Typeface.BOLD);
		doc_source.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.source_size));
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
		doc_year.setText(aux_year +" vol." + aux_volume + " ("+ aux_issue +") pp."+ aux_pages);
		doc_year.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.source_size));
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
		
		
		//Image arrow
		ImageView arrowButton = new ImageView(this);
		
		doc_abstract.setId(8);
		
		arrowButton.setId(9);
		arrowButton.setImageResource(R.drawable.arrow);
		
		RelativeLayout.LayoutParams layout_arrowButton = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.image_size), getResources().getDimensionPixelSize(R.dimen.image_size));
		layout_arrowButton.setMargins(getResources().getDimensionPixelOffset(R.dimen.pading), getResources().getDimensionPixelOffset(R.dimen.pading), 0, 0);
		layout_arrowButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,doc_abstract.getId() );
		layout_arrowButton.addRule(RelativeLayout.BELOW,relativeLayout_line.getId());
		layout_arrowButton.addRule(RelativeLayout.CENTER_IN_PARENT, doc_abstract.getId());
		
		arrowButton.setLayoutParams(layout_arrowButton);
		
		relativeLayout.addView(arrowButton); 
		
		//Document Abstract
		
		doc_abstract.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ABSTRACT)));
		doc_abstract.setMaxLines(5);
		doc_abstract.setMinLines(1);
		doc_abstract.setEllipsize(TruncateAt.END);
		doc_abstract.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.source_size));
		doc_abstract.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
		RelativeLayout.LayoutParams layout_doc_abstract = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		layout_doc_abstract.addRule(RelativeLayout.BELOW, relativeLayout_line.getId());
		layout_doc_abstract.addRule(RelativeLayout.LEFT_OF, arrowButton.getId());
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
		
		
		String aux_doi = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.DOI));
		String aux_pmid = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PMID));
		String aux_issn = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ISSN));
		TextView doc_issn = new TextView(this);
		TextView doc_catalog_title = new TextView(this);
		TextView doc_catalog = new TextView(this);
		TextView doc_pmid = new TextView(this);
		
		
		if(!aux_issn.isEmpty() || !aux_doi.isEmpty() || !aux_pmid.isEmpty()){



			//Document Catalog IDS

			doc_catalog_title.setId(15);
			doc_catalog_title.setText("CATALOG IDS");
			doc_catalog_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
			doc_catalog_title.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_catalog_title = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_catalog_title.addRule(RelativeLayout.BELOW, doc_notes.getId());
			layout_doc_catalog_title.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_catalog_title.setLayoutParams(layout_doc_catalog_title);
			relativeLayout.addView(doc_catalog_title);


			//Document Catalog DOI
			doc_catalog.setId(16);
			doc_catalog.setBackgroundColor(Color.WHITE);

			doc_catalog.setText("DOI:	" + cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.DOI)));

			doc_catalog.setMaxLines(2);
			doc_catalog.setEllipsize(TruncateAt.END);
			doc_catalog.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
			doc_catalog.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_catalog = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_catalog.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_catalog.setLayoutParams(layout_doc_catalog);



			//Image arrow -- DOI
			ImageView linkButton_doi = new ImageView(this);
			linkButton_doi.setId(17);
			linkButton_doi.setImageResource(R.drawable.arrow);
			RelativeLayout.LayoutParams layout_linkButton_doi = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.arrow_size), getResources().getDimensionPixelSize(R.dimen.arrow_size));
			layout_linkButton_doi.setMargins(0, 6, 0, 0);

			layout_linkButton_doi.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,doc_catalog.getId() );

			layout_linkButton_doi.addRule(RelativeLayout.CENTER_IN_PARENT, doc_catalog.getId());
			linkButton_doi.setLayoutParams(layout_linkButton_doi);



			//Document Catalog PMID

			doc_pmid.setId(18);
			doc_pmid.setBackgroundColor(Color.WHITE);
			doc_pmid.setText("PMID:		" + cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.PMID)));

			doc_pmid.setMaxLines(2);
			doc_pmid.setEllipsize(TruncateAt.END);
			doc_pmid.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
			doc_pmid.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_pmid = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_pmid.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_pmid.setLayoutParams(layout_doc_pmid);


			//Image arrow -- PMID
			ImageView linkButton_pmid = new ImageView(this);
			linkButton_pmid.setId(19);
			linkButton_pmid.setImageResource(R.drawable.arrow);
			RelativeLayout.LayoutParams layout_linkButton_pmid = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.arrow_size), getResources().getDimensionPixelSize(R.dimen.arrow_size));
			layout_linkButton_pmid.setMargins(0, 6, 0, 0);
			layout_linkButton_pmid.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,doc_pmid.getId() );
			layout_linkButton_pmid.addRule(RelativeLayout.CENTER_IN_PARENT, doc_pmid.getId());
			linkButton_pmid.setLayoutParams(layout_linkButton_pmid);


			//Document Catalog ISSN
			doc_issn.setId(20);
			doc_issn.setBackgroundColor(Color.WHITE);
			doc_issn.setText("ISSN: 	" + cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.ISSN)));
			doc_issn.setMaxLines(2);
			doc_issn.setEllipsize(TruncateAt.END);
			doc_issn.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
			doc_issn.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			RelativeLayout.LayoutParams layout_doc_issn = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_issn.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_issn.setLayoutParams(layout_doc_issn);


			//Image arrow -- ISSN
			ImageView linkButton_issn = new ImageView(this);
			linkButton_issn.setId(21);
			linkButton_issn.setImageResource(R.drawable.arrow);

			RelativeLayout.LayoutParams layout_linkButton_issn = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.arrow_size), getResources().getDimensionPixelSize(R.dimen.arrow_size));
			layout_linkButton_issn.setMargins(0, 6, 0, 0);
			layout_linkButton_issn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,doc_issn.getId() );


			if(!aux_issn.isEmpty() && !aux_doi.isEmpty() && !aux_pmid.isEmpty()){

				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				layout_linkButton_doi.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				relativeLayout.addView(linkButton_doi); 
				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog.getId());
				layout_linkButton_pmid.addRule(RelativeLayout.BELOW,doc_catalog.getId());
				relativeLayout.addView(linkButton_pmid); 
				//ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_pmid.getId());
				relativeLayout.addView(doc_issn);
				layout_linkButton_issn.addRule(RelativeLayout.BELOW,doc_pmid.getId());
				layout_linkButton_issn.addRule(RelativeLayout.CENTER_IN_PARENT, doc_issn.getId());
				linkButton_issn.setLayoutParams(layout_linkButton_issn);
				relativeLayout.addView(linkButton_issn); 
			}


			else if(!aux_issn.isEmpty() && !aux_doi.isEmpty() && aux_pmid.isEmpty()){


				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				layout_linkButton_doi.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				relativeLayout.addView(linkButton_doi); 


				//Document Catalog ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_catalog.getId());
				relativeLayout.addView(doc_issn);
				layout_linkButton_issn.addRule(RelativeLayout.BELOW,doc_catalog.getId());
				linkButton_issn.setLayoutParams(layout_linkButton_issn);
				relativeLayout.addView(linkButton_issn); 

			}

			else if(aux_issn.isEmpty() && !aux_doi.isEmpty() && !aux_pmid.isEmpty()){


				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				layout_linkButton_doi.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				relativeLayout.addView(linkButton_doi); 

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog.getId());
				layout_linkButton_pmid.addRule(RelativeLayout.BELOW,doc_catalog.getId());
				relativeLayout.addView(linkButton_pmid); 

			}

			else if(!aux_issn.isEmpty() && aux_doi.isEmpty() && !aux_pmid.isEmpty()){

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				layout_linkButton_pmid.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				relativeLayout.addView(linkButton_pmid);
				
				//Document Catalog ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_pmid.getId());
				relativeLayout.addView(doc_issn);
				layout_linkButton_issn.addRule(RelativeLayout.BELOW,doc_pmid.getId());
				linkButton_issn.setLayoutParams(layout_linkButton_issn);
				relativeLayout.addView(linkButton_issn); 
			}

			else if(aux_issn.isEmpty() && !aux_doi.isEmpty() && aux_pmid.isEmpty()){


				//DOI
				relativeLayout.addView(doc_catalog);
				layout_doc_catalog.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				layout_linkButton_doi.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				relativeLayout.addView(linkButton_doi); 
			}

			else if(aux_issn.isEmpty() && aux_doi.isEmpty() && !aux_pmid.isEmpty()){

				//PMID
				relativeLayout.addView(doc_pmid);
				layout_doc_pmid.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				layout_linkButton_pmid.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				relativeLayout.addView(linkButton_pmid); 
			}

			else if(!aux_issn.isEmpty() && aux_doi.isEmpty() && aux_pmid.isEmpty()){

				//Document Catalog ISSN
				layout_doc_issn.addRule(RelativeLayout.BELOW, doc_catalog_title.getId());
				relativeLayout.addView(doc_issn);

				layout_linkButton_issn.addRule(RelativeLayout.BELOW,doc_catalog_title.getId());
				linkButton_issn.setLayoutParams(layout_linkButton_issn);

				relativeLayout.addView(linkButton_issn); 
			}

		}


		String aux_url = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.WEBSITE));
		TextView doc_url_title = new TextView(this);
		TextView doc_url = new TextView(this);
		RelativeLayout.LayoutParams layout_doc_url_title;
		RelativeLayout.LayoutParams layout_doc_url;
		ImageView linkButton3 = new ImageView(this);
		RelativeLayout.LayoutParams layout_linkButton4;


		if(!aux_url.isEmpty()){

			//Document URL Title
			doc_url_title.setId(22);
			doc_url_title.setText("URLS");
			doc_url_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
			doc_url_title.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			layout_doc_url_title = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			doc_url_title.setLayoutParams(layout_doc_url_title);
			relativeLayout.addView(doc_url_title);

			//Document URL
			doc_url.setId(23);
			doc_url.setBackgroundColor(Color.WHITE);
			doc_url.setText(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.WEBSITE)));
			doc_url.setMaxLines(1);
			doc_url.setEllipsize(TruncateAt.END);
			doc_url.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimensionPixelSize(R.dimen.other_size));
			doc_url.setPadding(getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingLeft), 0, 0, 0);
			layout_doc_url = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			layout_doc_url.setMargins(0, getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingTop), 0,getResources().getDimensionPixelOffset(R.dimen.doc_type_paddingBottom));
			doc_url.setLayoutParams(layout_doc_url);
			layout_doc_url.addRule(RelativeLayout.BELOW, doc_url_title.getId());
			relativeLayout.addView(doc_url);

			//Image arrow
			linkButton3.setId(24);
			linkButton3.setImageResource(R.drawable.arrow);
			layout_linkButton4 = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.arrow_size), getResources().getDimensionPixelSize(R.dimen.arrow_size));
			layout_linkButton4.setMargins(0, 4, 0, 0);
			layout_linkButton4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,doc_url.getId() );
			layout_linkButton4.addRule(RelativeLayout.BELOW,doc_url_title.getId());
			layout_linkButton4.addRule(RelativeLayout.CENTER_IN_PARENT, doc_url.getId());
			linkButton3.setLayoutParams(layout_linkButton4);
			relativeLayout.addView(linkButton3); 


			if(!aux_issn.isEmpty()){

				//Document URL Title
				layout_doc_url_title.addRule(RelativeLayout.BELOW, doc_issn.getId());
			}

			else if(aux_issn.isEmpty() && !aux_pmid.isEmpty()){

				//Document URL
				layout_doc_url_title.addRule(RelativeLayout.BELOW, doc_pmid.getId());
			}

			else if(!aux_issn.isEmpty() && !aux_pmid.isEmpty() && aux_doi.isEmpty()){

				//Document URL
				layout_doc_url_title.addRule(RelativeLayout.BELOW, doc_catalog.getId());
			}
		}
		
		
	
		scrollView.addView(relativeLayout); 
		setContentView(scrollView);
		 
		}
		
		
	
	
	
	
	

}
