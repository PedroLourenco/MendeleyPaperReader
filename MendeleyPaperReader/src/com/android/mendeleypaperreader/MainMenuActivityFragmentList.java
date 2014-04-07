package com.android.mendeleypaperreader;



import java.util.Arrays;
import java.util.List;


import com.android.mendeleypaperreader.utl.Globalconstant;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class MainMenuActivityFragmentList extends ListFragment
{
    boolean mDualPane;
    int mCurCheckPosition = 0;
    private ListView listView;
    
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

        // The next line works just fine. It uses an ArrayAdapter.
        // setListAdapter (new ArrayAdapter(getActivity (), R.layout.complex_list_item, R.id.text1, Shakespeare.TITLES));

        // Use a custom adapter so we can have something more than the just the text view filled in.
        // Use a custom adapter so we can have something more than the just the text view filled in.
        // Use a custom adapter so we can have something more than the just the text view filled in.
        // Use a custom adapter so we can have something more than the just the text view filled in.
        setListAdapter (new CustomAdapter (getActivity (),
                                         R.id.title, Arrays.asList (Globalconstant.MYLIBRARY)));
        
        
    
        //mAdapter = new CustomListViewWithImage(getActivity(), Globalconstant.MYLIBRARY,imageId);
        //listView.setAdapter(mAdapter);
        
        
        
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

/**
 */

@Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
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
    private class CustomAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private int mLayoutId;

    /**
     * Constructor
     */

    public CustomAdapter(Context context, int textViewResourceId, List<String> items) 
    {
    	super(context, textViewResourceId, items);
       mContext = context;
      // mLayoutId = layoutId;
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
    /*
    	topicListItem item = getItem(position);

       // For any one of the views that are clickable, we are going to use the Tag value to indicate
       // which item we are working on.
       Integer positionTag = new Integer (position);

       View itemView = v;
       itemView.setTag (positionTag);

       // Set the text of the item
       TextView topicTxt = (TextView) itemView.findViewById(R.id.topicTxt);
       TextView desTxt =(TextView) itemView.findViewById(R.id.descriptionTxt);
       topicTxt.setText(item.getName());
       desTxt.setText(item.getDes());

       ImageView starBtn =(ImageView) itemView.findViewById (R.id.checked_icon);
       if (item.isChecked()) starBtn.setImageResource(R.drawable.star_icon);
       else starBtn.setImageResource(R.drawable.unstarred_icon);
       starBtn.setClickable(true);
       starBtn.setTag (positionTag);

       // Set the state of the delete button for the current user.
       ImageView deleteButton =(ImageView) itemView.findViewById(R.id.topicDeleteButton);
     	deleteButton.setVisibility(View.INVISIBLE);
       deleteButton.setTag (positionTag);
       deleteButton.setClickable(true);

       if (item.getAddedByCurrentUser ()) {
       	deleteButton.setVisibility(View.VISIBLE);
         	topicTxt.setTextColor (R.color.background_error_text); // Color.rgb(100,100,90));
       } 

       // It looks like the topicTxt really holds the name of the person who posted the information.
       // Show that person's profile.
       topicTxt.setTag (positionTag);
      topicTxt.setOnClickListener (new Button.OnClickListener() {
    				public void onClick(View v) 
                {
                   Context c = getContext ();
                   TextView tv = (TextView) v;
                   String poster = tv.getText ().toString ();
                   //Toast.makeText(c, poster,Toast.LENGTH_LONG).show();
                   Intent i = new Intent(c.getApplicationContext(), 
                                         profile_activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   i.putExtra("poster", poster);
                   c.startActivity(i);
                }
                });

       return itemView;
    */
    }
    /*
    private void fillText(View v, int id, String text) {
    	TextView textView = (TextView) v.findViewById(id);
    	textView.setText(text == null ? "" : text);
    }
    */

    } // end class CustomAdapter

    } // end class TitlesFragment
