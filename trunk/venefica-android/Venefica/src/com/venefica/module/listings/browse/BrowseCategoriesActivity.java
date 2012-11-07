package com.venefica.module.listings.browse;

import java.util.ArrayList;

import com.venefica.activity.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_categories);
        listViewCategories = (ListView) findViewById(R.id.listActBrowseCatCategories);
        categories = getCategories();
        categoriesListAdapter = new CategoryListAdapter(this, categories);
        listViewCategories.setAdapter(categoriesListAdapter);
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

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browse_categories, menu);
        return true;
    }
}
