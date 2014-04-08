package com.android.mendeleypaperreader;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.mendeleypaperreader.db.MendeleyDataSource;
import com.android.mendeleypaperreader.utl.CustomCursorAdapter;

public class MainMenuActivityFragmentDetails  extends Fragment  {
	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
	private MendeleyDataSource mendeleyDataSource;
	ListView mListView;
	CustomCursorAdapter wla;
		
	public static MainMenuActivityFragmentDetails newInstance(int index) {
    	MainMenuActivityFragmentDetails f = new MainMenuActivityFragmentDetails();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

       
        
        View view = inflater.inflate(R.layout.activity_main_menu_details, container, false);

        mendeleyDataSource = new MendeleyDataSource(getActivity().getApplicationContext());
        mendeleyDataSource.open();
      	
		Cursor listtitles = mendeleyDataSource.get_all_titles_doc();
        
        wla = new CustomCursorAdapter(getActivity().getApplicationContext(), listtitles);
        ListView lv = (ListView) view.findViewById(R.id.listDetails);
        lv.setAdapter(wla);
        
        
        
        return view;
        
    }

    
 
   
    
}