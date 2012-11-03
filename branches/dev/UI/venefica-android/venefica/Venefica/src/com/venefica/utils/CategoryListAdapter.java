package com.venefica.utils;

import java.util.ArrayList;
import java.util.List;

import com.venefica.activity.R;
import com.venefica.market.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CategoryListAdapter
{
	Context context;
	LayoutInflater inflater;
	ViewGroup parentView;
	List<Holder> items = new ArrayList<Holder>();
	boolean oneBlockSpinerBehavior = false;

	class Holder
	{
		public List<Category> category;
		public View view;
		public Spinner spinner;
		public int position;
		boolean oneBlockBehavior = false;
	}

	public CategoryListAdapter(Context context, ViewGroup parentView)
	{
		if (context == null)
			throw new IllegalArgumentException("context == null");

		if (parentView == null)
			throw new IllegalArgumentException("parentView == null");

		this.context = context;
		inflater = (LayoutInflater)context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
		this.parentView = parentView;
	}

	public void AddCategory(List<Category> newCat)
	{
		if (newCat == null)
			throw new IllegalArgumentException("newCat == null");

		Holder holder = new Holder();

		holder.oneBlockBehavior = oneBlockSpinerBehavior;
		holder.position = items.size();
		holder.category = newCat;
		holder.view = inflater.inflate(R.layout.category_list_row, parentView, false);

		holder.spinner = (Spinner)holder.view.findViewById(R.id.spinnerCategory);
		ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(context, android.R.layout.simple_spinner_item, newCat);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		holder.spinner.setAdapter(adapter);

		holder.spinner.setTag(holder);
		holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> adapter, View v, int position, long id)
			{
				Category item = (Category)adapter.getItemAtPosition(position);
				List<Category> subCat = item.GetSubCategory();

				Holder holder = (Holder)((Spinner)v.getParent()).getTag();

				if (holder.oneBlockBehavior)
				{
					holder.oneBlockBehavior = false;
					return;
				}

				if (holder.position < items.size() - 1)
				{
					DeleteAfterPosition(holder.position);
				}

				if (subCat.size() > 0)
				{
					AddCategory(subCat);
				}
			}

			public void onNothingSelected(AdapterView<?> paramAdapterView)
			{
				;
			}
		});
		holder.spinner.setSelection(0);

		parentView.addView(holder.view);
		items.add(holder);
	}

	public void DeleteAfterPosition(int position)
	{
		for (int i = position + 1; i < items.size(); i++)
		{
			parentView.removeView(items.get(i).view);
		}
	}

	public Category GetLastCategory()
	{
		Holder holder = items.get(items.size() - 1);
		return (Category)holder.spinner.getSelectedItem();
	}

	public void CreateBackHierarchy(long lastId)
	{
		List<Category> backHierarchy = new ArrayList<Category>();

		Category curCat = Category.GetCategoryById(lastId);
		backHierarchy.add(curCat);

		while (curCat.Parent != null)
		{
			curCat = curCat.Parent;
			backHierarchy.add(curCat);
		}

		List<List<Category>> spinerItems = new ArrayList<List<Category>>();
		List<Integer> spinerSelect = new ArrayList<Integer>();

		spinerItems.add(Category.GetRootCategory());
		spinerSelect.add(new Integer(GetCategoryIndexInList(backHierarchy.get(backHierarchy.size() - 1), Category.GetRootCategory())));
		for (int i = backHierarchy.size() - 2; i >= 0; i--)
		{
			if (i < 0)
				break;

			Category cat = backHierarchy.get(i);
			Integer index = new Integer(GetCategoryIndexInList(cat, cat.Parent.GetSubCategory()));

			spinerItems.add(cat.Parent.GetSubCategory());
			spinerSelect.add(index);
		}

		oneBlockSpinerBehavior = true;

		for (int i = 0; i < spinerItems.size(); i++)
		{
			AddCategory(spinerItems.get(i));
			items.get(items.size() - 1).spinner.setSelection(spinerSelect.get(i));
		}

		//blockSpinerBehavior = false;
	}

	protected int GetCategoryIndexInList(Category cat, List<Category> list)
	{
		if (cat == null || list == null)
			throw new IllegalArgumentException("cat == null || list == null");

		int i;
		for (i = 0; i < list.size(); i++)
		{
			if (cat.Id == list.get(i).Id)
				break;
		}

		return i;
	}
}
