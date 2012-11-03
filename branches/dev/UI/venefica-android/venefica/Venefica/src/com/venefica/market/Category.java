package com.venefica.market;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

public class Category
{
	public long Id;
	public String Desc;
	public long ParentId = -1;
	public Category Parent;
	HashMap<Long, Category> SubCategory = new HashMap<Long, Category>();
	long subCategoryNumber;

	/** Содержит все категории */
	static HashMap<Long, Category> staticAllCategory = new HashMap<Long, Category>();
	/** Содержит только рут категории */
	static HashMap<Long, Category> staticRootCategory = new HashMap<Long, Category>();

	/** Пустая категория */
	public static final Category EmptyCategory = new Category(-1, "None");

	/** Добовляем рут категорию */
	public static Category AddRootCategory(long Id, String Desc)
	{
		Category cat = new Category(Id, Desc);
		staticAllCategory.put(new Long(Id), cat);
		staticRootCategory.put(new Long(Id), cat);
		return cat;
	}

	/** @return NULL-a не будет */
	public static Category GetCategoryById(long Id)
	{
		Category result = staticAllCategory.get(new Long(Id));
		if (result == null)
			result = EmptyCategory;

		return result;
	}

	/** @return NULL-a не будет */
	public static List<Category> GetRootCategory()
	{
		List<Category> result = new ArrayList<Category>();

		try
		{
			Collection<Category> arr = staticRootCategory.values();
			if (arr != null)
				result.addAll(arr);
		}
		catch (Exception e)
		{
			Log.d("GetRootCategory Exception:", e.getLocalizedMessage());
		}

		return result;
	}

	/** @return NULL-a не будет */
	public static Category GetRootCategoryById(long Id)
	{
		Category result = staticRootCategory.get(new Long(Id));
		if (result == null)
			result = EmptyCategory;

		return result;
	}

	/** Удалить все категории */
	public static void DeleteAllCategories()
	{
		staticRootCategory.clear();
		staticAllCategory.clear();
	}

	/** @return NULL-a не будет */
	public static List<Category> GetAllCategory()
	{
		List<Category> result = new ArrayList<Category>();

		try
		{
			Collection<Category> arr = staticAllCategory.values();
			if (arr != null)
				result.addAll(arr);
		}
		catch (Exception e)
		{
			Log.d("GetAllCategory Exception:", e.getLocalizedMessage());
		}

		return result;
	}

	public Category()
	{

	}

	public Category(long Id, String Desc)
	{
		this.Id = Id;
		this.Desc = Desc;
	}

	/** Добовляем суб категорию в текущую категорию */
	public Category AddSubCategory(long Id, String Desc)
	{
		Category cat = new Category(Id, Desc);
		cat.ParentId = this.Id;
		cat.Parent = this;
		staticAllCategory.put(new Long(Id), cat);
		SubCategory.put(new Long(Id), cat);
		subCategoryNumber++;
		return cat;
	}

	/** @return NULL-a не будет */
	public List<Category> GetSubCategory()
	{
		List<Category> result = new ArrayList<Category>();

		try
		{
			Collection<Category> arr = SubCategory.values();
			if (arr != null)
				result.addAll(arr);
		}
		catch (Exception e)
		{
			Log.d("GetSubCategory Exception:", e.getLocalizedMessage());
		}

		return result;
	}

	public String toString()
	{
		return Desc != null ? Desc : "None";
	}
	
	public long getSubCategoryNumber()
	{
		return subCategoryNumber;
	}
}
