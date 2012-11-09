package com.venefica.module.listings.browse;

import java.util.ArrayList;

import com.venefica.activity.R;
import com.venefica.module.dashboard.DashBoardActivity;
import com.venefica.module.listings.bookmarks.BookmarkListingsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 
 * @author avinash
 * Activity to search by Categories
 */
public class BrowseCategoriesActivity extends Activity {

	/**
	 * List to show Categories
	 */
	private ListView listViewCategories;
	/**
	 * List adapter
	 */
	private CategoryListAdapter categoriesListAdapter;
	/**
	 * Categories list
	 */
	private ArrayList<CategoryData> categories;
	/**
	 * Bookmark and search buttons
	 */
	private Button btnBookmarks, btnSearch;
	/**
	 * Activity modes
	 */
	public static final int ACT_MODE_GET_CATEGORY = 1001;
	public static final int ACT_MODE_BROWSE_CATEGORY = 1002;
	private int CURRENT_MODE = ACT_MODE_BROWSE_CATEGORY;
	private ViewGroup laySearchOptions;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_categories);
        btnBookmarks = (Button) findViewById(R.id.btnActBrowseCatBookmarks);
        btnBookmarks.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent bookmarkIntent = new Intent(BrowseCategoriesActivity.this, BookmarkListingsActivity.class);     
		    	startActivity(bookmarkIntent);
			}
		});
        btnSearch = (Button) findViewById(R.id.btnActBrowseCatSearchListings);
        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent searchIntent = new Intent(BrowseCategoriesActivity.this, SearchListingsActivity.class);     
		    	startActivity(searchIntent);
			}
		});
        
        listViewCategories = (ListView) findViewById(R.id.listActBrowseCatCategories);
		listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				Intent intent = new Intent(BrowseCategoriesActivity.this, SearchListingsActivity.class);
				intent.putExtra("hide_searchbar", true);
				intent.putExtra("category_name", categories.get(index).getCategoryName());
		    	startActivity(intent);
				
			}
		});
        categories = getCategories();
        categoriesListAdapter = new CategoryListAdapter(this, categories);
        listViewCategories.setAdapter(categoriesListAdapter);
        CURRENT_MODE = getIntent().getIntExtra("act_mode", ACT_MODE_BROWSE_CATEGORY);
        if (CURRENT_MODE == ACT_MODE_GET_CATEGORY) {
        	hideSearchOptions();
		}
    }

    private void hideSearchOptions() {
    	laySearchOptions.setVisibility(ViewGroup.GONE);
	}

	private ArrayList<CategoryData> getCategories() {
    	ArrayList<CategoryData> categories = new ArrayList<CategoryData>();
    	CategoryData category = new CategoryData();
    	category.setCatId(1);
    	category.setCategoryName("local places");
    	categories.add(category);
    	
    	category = new CategoryData();
    	category.setCatId(2);
    	category.setCategoryName("automotive");
    	categories.add(category);
    	
    	category = new CategoryData();
    	category.setCatId(3);
    	category.setCategoryName("musician");
    	categories.add(category);
    	
    	category = new CategoryData();
    	category.setCatId(4);
    	category.setCategoryName("real estate");
    	categories.add(category);
    	
    	category = new CategoryData();
    	category.setCatId(5);
    	category.setCategoryName("restaurants");
    	categories.add(category);
    	
    	category = new CategoryData();
    	category.setCatId(6);
    	category.setCategoryName("events");
    	categories.add(category);
    	
    	category = new CategoryData();
    	category.setCatId(7);
    	category.setCategoryName("rentals");
    	categories.add(category);
		return categories;
	}

	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browse_categories, menu);
        return true;
    }*/
}
