package com.android.mendeleypaperreader;



import java.util.Arrays;
import java.util.List;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mendeleypaperreader.utl.Globalconstant;



public class MainMenuActivityFragmentList extends ListFragment
{
	
	boolean mDualPane;
    int mCurCheckPosition = 0;
        
    ListView list;
    
    Integer[] imageId = {
        R.drawable.alldocuments,
        R.drawable.clock,
        R.drawable.starim,
        R.drawable.person,
        R.drawable.empty_trash
    };

 
    

@Override 
public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

              
        
        // Populate list with our static array of titles.

        // Use a custom adapter so we can have something more than the just the text view filled in.
        setListAdapter (new CustomAdapterLibrary (getActivity (),  R.id.title, Arrays.asList (Globalconstant.MYLIBRARY)));
        
        
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
        
       
    }



@Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	showDetails(position);
    	Toast.makeText(getActivity().getApplicationContext(), "Click on Library!!", Toast.LENGTH_SHORT).show();
    }

    
    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            MainMenuActivityFragmentDetails details = (MainMenuActivityFragmentDetails)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = MainMenuActivityFragmentDetails.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
        	
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    
    }
    
/**
 */
// CustomAdapter

/**
 * CustomAdapter
 *
 */
    private class CustomAdapterLibrary extends ArrayAdapter<String> {

        private Context mContext;
        
    /**
     * Constructor
     */

    public CustomAdapterLibrary(Context context, int textViewResourceId, List<String> items) 
    {
    	super(context, textViewResourceId, items);
       mContext = context;
    }

    /**
     * getView
     *
     * Return a view that displays an item in the array.
     *
     */

    public View getView (int position, View convertView, ViewGroup parent) 
    {
       View v = convertView;
    	if (v == null) {
    		LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		 v = vi.inflate (R.layout.list_row_with_image, null, true);
    	}

       View itemView = v;

       TextView txtTitle = (TextView) itemView.findViewById(R.id.title);
		ImageView imageView = (ImageView) itemView.findViewById(R.id.list_image);
		txtTitle.setText(Globalconstant.MYLIBRARY[position]);
		
		imageView.setImageResource(imageId[position]);

       return itemView;
    }

    } // end class CustomAdapter

   


}





