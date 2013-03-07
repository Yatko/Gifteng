package com.venefica.module.listings.browse;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.venefica.module.main.R;
import com.venefica.services.CategoryDto;

/**
 * @author avinash
 * Adapter for Category List
 */
public class CategoryListAdapter extends BaseAdapter {
	
	private Context context;
	private List<CategoryDto> categories;
	private TextView txtCatName;
	private ImageView nextImg;
	private static ViewHolder holder;
	
	public CategoryListAdapter(Context context, List<CategoryDto> categories) {
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
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.view_category_list_item, parent, false);
			txtCatName = (TextView) convertView.findViewById(R.id.txtCatListItemName);
			nextImg = (ImageView) convertView.findViewById(R.id.imgCatListItemMore);
			
			holder.txtCatName = txtCatName;
			holder.nextImg = nextImg;
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtCatName.setText(categories.get(position).getName());
		if(categories.get(position).getSubcategories() == null || categories.get(position).getSubcategories().size() == 0){
			holder.nextImg.setVisibility(View.INVISIBLE);
		}else {
			holder.nextImg.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView txtCatName;
		ImageView nextImg;
	}
}
