package com.venefica.module.user;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.venefica.module.main.R;
import com.venefica.module.user.UserDto.Gender;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.services.AddressDto;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

/**
 * @author avinash
 * Fragment class to edit profile details
 */
public class EditProfileFragment extends SherlockFragment implements OnClickListener {
	
	/**
	 * @author avinash
	 * Listener to communicate with activity
	 */
	public interface OnEditProfileListener{
		/**
		 * show date picker dialog
		 */
		public void onDateEdit();
		public void onChangePassword();
		public void onSaveButtonClick();
	}
	private OnEditProfileListener onEditProfileListener;
	/**
	 * Input fields for user data.
	 */
	private EditText edtEmail, edtPhone, 
		edtFirstName, edtLastName, edtDateOfBirth, edtZipCode, edtCounty, edtCity, edtArea,
		edtAddress1, edtAddress2, edtState, edtCountry;
	private TextView txtMemberSince;
	/**
	 * Buttons for profile selection and signup
	 */
	private Button btnSelProfileImg, btnResetPassword, btnSave;
	private ImageView imageView;
	private RadioGroup genderRadioGroup;
	/**
	 * Field validator
	 */
	private InputFieldValidator vaildator;
	/**
	 * user data
	 */
	private UserDto userDto;
	/**
	 * image request code
	 */
	private static final int REQ_GET_IMAGE = 1002;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userDto = ((VeneficaApplication)getActivity().getApplication()).getUser();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.view_edit_profile, container, false);
		//Input fields
        edtEmail = (EditText) view.findViewById(R.id.edtActProfileEmail);
        edtPhone = (EditText) view.findViewById(R.id.edtActProfilePhone);
        edtPhone.setText(Utility.getPhoneNo(getActivity()));
		edtFirstName = (EditText) view.findViewById(R.id.edtActProfileFName);
		edtLastName = (EditText) view.findViewById(R.id.edtActProfileLName);
		edtDateOfBirth = (EditText) view.findViewById(R.id.edtActProfileBirthDate);
		edtZipCode = (EditText) view.findViewById(R.id.edtActProfileZip);
		edtCounty = (EditText) view.findViewById(R.id.edtActProfileCounty);
		edtCity = (EditText) view.findViewById(R.id.edtActProfileCity);
		edtArea = (EditText) view.findViewById(R.id.edtActProfileArea);        
		edtAddress1  = (EditText) view.findViewById(R.id.edtActProfileAddress1);
		edtAddress2 = (EditText) view.findViewById(R.id.edtActProfileAddress2);
		edtState = (EditText) view.findViewById(R.id.edtActProfileState);
		edtCountry = (EditText) view.findViewById(R.id.edtActProfileCountry);;
		edtDateOfBirth.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				onEditProfileListener.onDateEdit();
				return true;
			}
		});
		//Buttons
        btnSelProfileImg = (Button) view.findViewById(R.id.btnActProfileChangeImg);
        btnSelProfileImg.setOnClickListener(this);
        
        btnResetPassword = (Button) view.findViewById(R.id.btnActProfileChangePassword);
        btnResetPassword.setOnClickListener(this);
        
        btnSave =  (Button) view.findViewById(R.id.btnActProfileSave);
        btnSave.setOnClickListener(this);
        txtMemberSince = (TextView)view.findViewById(R.id.txtActProfileMemberFrom);
        imageView = (ImageView) view.findViewById(R.id.imgActProfileProfileImg);
        
        genderRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroupActProfileGender);
        //display data
        setUserData(userDto);
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            this.onEditProfileListener = (OnEditProfileListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEditProfileListener");
        }
	}
	
	/**
	 * Set user data
	 * @param userDto
	 */
	public void setUserData(UserDto userDto){
		try {			
			/*if (userDto.getAvatar() != null
					&& userDto.getAvatar().getUrl() != null) {
				((VeneficaApplication) getActivity().getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ userDto.getAvatar().getUrl(),
										imageView,
								getResources().getDrawable(
										R.drawable.icon_picture_white));
			}*/
			txtMemberSince.append(" ");
			if (userDto.getJoinedAt() != null) {
				txtMemberSince.append(Utility.convertShortDateToString(userDto
						.getJoinedAt()));
			}
			edtEmail.setText(userDto.getEmail());
			if (!userDto.getPhoneNumber().trim().equals("")) {
				edtPhone.setText(userDto.getPhoneNumber());
			}
			edtFirstName.setText(userDto.getFirstName());
			edtLastName.setText(userDto.getLastName());
			if (userDto.getDateOfBirth() != null) {
				edtDateOfBirth.setText(Utility.convertDateToString(userDto
						.getDateOfBirth()));
			}
			if (userDto.getAddress() != null) {
				edtZipCode.setText(userDto.getAddress().getZipCode());
				edtCounty.setText(userDto.getAddress().getCounty());
				edtCity.setText(userDto.getAddress().getCity());
				edtArea.setText(userDto.getAddress().getArea());
				edtAddress1.setText(userDto.getAddress().getAddress1());
				edtAddress2.setText(userDto.getAddress().getAddress2());
				edtState.setText(userDto.getAddress().getState());
				edtCountry.setText(userDto.getAddress().getCountry());
			}
			if (userDto.getGender() == Gender.MALE) {
				genderRadioGroup.check(R.id.radioActProfileMale);
			}else {
				genderRadioGroup.check(R.id.radioActProfileFemale);
			}
			
			if (userDto.getAvatar() != null
					&& userDto.getAvatar().getUrl() != null) {
				((VeneficaApplication) getActivity().getApplication())
						.getImgManager().loadImage(
								Constants.PHOTO_URL_PREFIX
										+ userDto.getAvatar().getUrl(),
										imageView,
								getResources().getDrawable(
										R.drawable.icon_picture_white));
			}
		} catch (Exception e) {
			Log.e("EditProfileFragment::setUserData: ", e.toString());
		}
	}
	
	/**
	 * Get user data to update
	 * @return UserDto
	 */
	public UserDto getUserDataToUpdate(){
		try {
			userDto.setFirstName(edtFirstName.getText().toString());
			userDto.setLastName(edtLastName.getText().toString());
			
			userDto.setEmail(edtEmail.getText().toString());
			userDto.setPhoneNumber(edtPhone.getText().toString());
			if(!edtDateOfBirth.getText().toString().equals("")){
				userDto.setDateOfBirth(((Date)new SimpleDateFormat("yyyy/mm/dd", Locale.getDefault()).parse(edtDateOfBirth.getText().toString())));
			}else{
				userDto.setDateOfBirth(Calendar.getInstance().getTime());
			}
			AddressDto addressDto = new AddressDto();
			addressDto.setZipCode(edtZipCode.getText().toString());
			addressDto.setCity(edtCity.getText().toString());
			addressDto.setCounty(edtCounty.getText().toString());
			addressDto.setArea(edtArea.getText().toString());
			addressDto.setAddress1(edtAddress1.getText().toString());
			addressDto.setAddress2(edtAddress2.getText().toString());
			addressDto.setState(edtState.getText().toString());
			addressDto.setCountry(edtCountry.getText().toString());
			userDto.setAddress(addressDto);
			switch (genderRadioGroup.getCheckedRadioButtonId()) {
			  case R.id.radioActProfileMale :
				  userDto.setGender(Gender.MALE);
   	              break;
			  case R.id.radioActProfileFemale :
				  userDto.setGender(Gender.FEMALE);
				  break;
			}
		} catch (ParseException e) {
			Log.e("EditProfileFragment::getUserData: ", e.toString());
		}		
		return userDto;		
	}
	/**
	 * set selected date from dialog to EditText 
	 * @param date
	 */
	public void setSelectedDate(String date){
		edtDateOfBirth.setText(date);
	}
	
	/**
     * Method to validate input fields for registration
     * @return result of validation
     */
	public boolean validateFields(){
    	boolean result = true;
    	StringBuffer message = new StringBuffer();
    	if(vaildator == null){
    		vaildator = new InputFieldValidator();    		
    	}
    	if(!vaildator.validateField(edtEmail, Pattern.compile(InputFieldValidator.EMAIL_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_email).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_email));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtPhone, Pattern.compile(InputFieldValidator.PHONE_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_phone).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_phone));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtFirstName, Pattern.compile(InputFieldValidator.USER_NAME_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_f_name).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtLastName, Pattern.compile(InputFieldValidator.USER_NAME_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_l_name).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtZipCode, Pattern.compile(InputFieldValidator.ZIP_PATTERN_REGX))
    			 || edtZipCode.getText().toString().length() < Constants.ZIP_CODE_LENGTH){
    		result = false;
    		message.append(getResources().getString(R.string.label_zip).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_zipcode));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCounty, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_county).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCity, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_city).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtArea, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_area).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtAddress1, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_addr1).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtAddress2, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_addr2).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtState, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_state).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCountry, Pattern.compile(InputFieldValidator.COUNTY_CITY_AREA_PATTERN_REGX))){
    		result = false;
    		message.append(getResources().getString(R.string.label_country).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if (!result) {
			Utility.showLongToast(getActivity(), message.toString()); 
		}
		return result;    	
    }
	/**
     * Get image from gallery
     */
    private void pickImageFromGallery() {
        // Gallery.
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQ_GET_IMAGE);
    }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			//get image from gallery
			if (requestCode == REQ_GET_IMAGE && data != null) {
				try {
					InputStream stream = getActivity().getContentResolver()
							.openInputStream(data.getData());
					Rect rect = new Rect(0, 0, Constants.IMAGE_MAX_SIZE_X,
							Constants.IMAGE_MAX_SIZE_Y);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inInputShareable = false;
					opts.inSampleSize = 2;
					Bitmap bitmap = BitmapFactory.decodeStream(stream, rect,
							opts);
					
					imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
							Constants.IMAGE_THUMBNAILS_WIDTH,
							Constants.IMAGE_THUMBNAILS_HEIGHT, false));
					userDto.setAvatar(new ImageDto(Bitmap.createScaledBitmap(bitmap,
							Constants.IMAGE_THUMBNAILS_WIDTH,
							Constants.IMAGE_THUMBNAILS_HEIGHT, false)));
					bitmap.recycle();
					stream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.btnActProfileChangeImg) {
			pickImageFromGallery();
		} else if (id == R.id.btnActProfileChangePassword) {
			//show change password dialog
			onEditProfileListener.onChangePassword();
		} else if (id == R.id.btnActProfileSave) {
			//save
			onEditProfileListener.onSaveButtonClick();
		}
		
	}	
}
