package com.venefica.module.listings.browse;

import java.util.ArrayList;

import com.venefica.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author avinash
 * Adapter for Category List
 */
public class CategoryListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<CategoryData> categories;
	private TextView txtCatName;
	private ImageView nextImg;
	
	public CategoryListAdapter(Context context, ArrayList<CategoryData> categories) {
		this.context = context;
		this.categories = categories;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return categories.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_category_list_item, parent, false);
			txtCatName = (TextView) convertView.findViewById(R.id.txtCatListItemName);
			nextImg = (ImageView) convertView.findViewById(R.id.imgCatListItemMore);
		}
		txtCatName.setText(categories.get(position).getCategoryName());
		if(categories.get(position).getSubCategories() == null || categories.get(position).getSubCategories().size() == 0){
			nextImg.setVisibility(View.INVISIBLE);
		}else {
			nextImg.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

}
