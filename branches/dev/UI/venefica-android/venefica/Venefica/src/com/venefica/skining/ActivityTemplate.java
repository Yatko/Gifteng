package com.venefica.skining;

import android.app.Activity;

/**
 * The base class for all form templates, work with the elements of the form must
  * Made through him
 */
public abstract class ActivityTemplate
{
	protected Activity mActivity = null;

	public ActivityTemplate(Activity Activity)
	{
		this.mActivity = Activity;
		
		if(mActivity!=null)
			CreateWidgets();
		
		RepairNullWidgets();
	}

	/**
	 * There should be setting the main view and search of necessary
* Items must be described in the class and Skin-
	 */
	protected abstract void CreateWidgets();
	
	/**
	 * Here are assigned default values which were not init widgets, should be described in the class Template
	 */
	protected abstract void RepairNullWidgets();
}
