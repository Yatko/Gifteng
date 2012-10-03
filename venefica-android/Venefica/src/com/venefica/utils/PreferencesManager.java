package com.venefica.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager
{
	private String storageName = null;

	private SharedPreferences settings = null;
	private SharedPreferences.Editor editor = null;
	private Context context = null;

	public PreferencesManager(Context context, String StorageName)
	{
		this.storageName = StorageName;
		this.context = context;
		settings = this.context.getSharedPreferences(this.storageName, Context.MODE_PRIVATE);
		editor = settings.edit();
	}

	public void Commit()
	{
		editor.commit();
	}

	public void Put(String key, boolean value)
	{
		editor.putBoolean(key, value);
	}

	public void Put(String key, float value)
	{
		editor.putFloat(key, value);
	}

	public void Put(String key, int value)
	{
		editor.putInt(key, value);
	}

	public void Put(String key, long value)
	{
		editor.putLong(key, value);
	}

	public void Put(String key, String value)
	{
		editor.putString(key, value);
	}

	public boolean GetBoolean(String key, boolean DefaultValue)
	{
		return settings.getBoolean(key, DefaultValue);
	}

	public float GetFloat(String key, float DefaultValue)
	{
		return settings.getFloat(key, DefaultValue);
	}

	public int GetInt(String key, int DefaultValue)
	{
		return settings.getInt(key, DefaultValue);
	}

	public long GetLong(String key, long DefaultValue)
	{
		return settings.getLong(key, DefaultValue);
	}

	public String GetString(String key, String DefaultValue)
	{
		return settings.getString(key, DefaultValue);
	}

}
