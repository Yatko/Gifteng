package com.venefica.module.utils;

import java.util.regex.Pattern;

import android.util.Log;
import android.widget.EditText;

/**
 * @author avinash
 * Class to validate Input field data.
 */
public class InputFieldValidator {
	/**
	 * Expression for char and number pattern.
	 */
	public static final String charNumPatternRegx = "[-0-9a-zA-Z_]{6,32}";

	/**
	 * Expression for user name(First name and Last name).
	 */
	public static final String userNamePatternRegx = "[-_\\w]{1,50}";
	/**
	 * Expression for zip code.
	 */
	public static final String zipCodePatternRegx = "[-\\d]{1,32}";
	/**
	 * Expression for county/city/area.
	 */
	public static final String countyCityAreaPatternRegx = ".{1,50}";
	/**
	 * Expression for phone validation
	 */
	public static final String phonePatternRegx = "[\\+0-9]{1,32}";
	/**
	 * Expression for email validation
	 */
	public static final String emailPatternRegx = "([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})";
	/**
	 * Method to validate EditField data for given pattern
	 * @param edtField input field to validate
	 * @param pattern validation pattern
	 * @return validation result if valid true
	 */
	public boolean validateField(EditText edtField, Pattern pattern){
		boolean result = true;
		try{
			String text = edtField.getText().toString();
			if (pattern.matcher(text).matches() == false){
				result = false;
			}			
		}catch (Exception e){
			Log.e("InputFieldValidator::Validate Exception:", e.getLocalizedMessage());
		}
		return result;
	}
}
