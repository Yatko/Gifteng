package com.venefica.module.listings.browse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.venefica.module.main.R;
import com.venefica.module.messages.MessageListActivity;
import com.venefica.services.CategoryDto;

/**
 * @author avinash
 * Adapter for Category List
 */
public class CategoryListAdapter extends BaseAdapter {
	
	private Context context;
	private List<CategoryDto> categories;
	private static ViewHolder holder;
	private boolean isForFilterScreen;
	private ArrayList<Long> selectedPositions;
	
	public CategoryListAdapter(Context context, List<CategoryDto> categories) {
		this.context = context;
		this.categories = categories;
		selectedPositions = new ArrayList<Long>();
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
			holder.txtCatName = (TextView) convertView.findViewById(R.id.txtCatListItemName);
			holder.nextImg = (ImageView) convertView.findViewById(R.id.imgCatListItemMore);
			holder.chkSelect = (CheckBox) convertView.findViewById(R.id.chkCatListItemSelect);
			holder.chkSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						//handle action mode visibility and message selection 
						Long id = Long.parseLong(buttonView.getContentDescription().toString());
						if (buttonView.isChecked() && !selectedPositions.contains(id)) {
							selectedPositions.add(id);
						} else if (!buttonView.isChecked() && selectedPositions.contains(id)) {
							selectedPositions.remove(id);
							selectedPositions.trimToSize();
						}
					}
				});
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (isForFilterScreen) {
			holder.txtCatName.setTextSize(12);
			holder.chkSelect.setVisibility(View.VISIBLE);
			holder.chkSelect.setContentDescription(categories.get(position).getId()+"");
			if (selectedPositions.contains(categories.get(position).getId())) {
				holder.chkSelect.setChecked(true);
			} else {
				holder.chkSelect.setChecked(false);
			}
		} else {
			holder.chkSelect.setVisibility(View.GONE);
		}
		holder.txtCatName.setText(categories.get(position).getName());
		if(categories.get(position).getSubcategories() == null || categories.get(position).getSubcategories().size() == 0){
			holder.nextImg.setVisibility(View.GONE);
		}else {
			holder.nextImg.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView txtCatName;
		ImageView nextImg;
		CheckBox chkSelect;
	}

	/**
	 * @return the isForFilterScreen
	 */
	public boolean isForFilterScreen() {
		return isForFilterScreen;
	}

	/**
	 * @param isForFilterScreen the isForFilterScreen to set
	 */
	public void setForFilterScreen(boolean isForFilterScreen) {
		this.isForFilterScreen = isForFilterScreen;
	}

	/**
	 * @return the selectedPositions
	 */
	public ArrayList<Long> getSelectedPositions() {
		return selectedPositions;
	}

	/**
	 * @param selectedPositions the selectedPositions to set
	 */
	public void setSelectedPositions(ArrayList<Long> selectedPositions) {
		this.selectedPositions = selectedPositions;
	}
}
