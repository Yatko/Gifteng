package com.venefica.module.user;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.venefica.module.main.R;
import com.venefica.module.main.VeneficaActivity;
import com.venefica.module.network.WSAction;
import com.venefica.module.utils.InputFieldValidator;
import com.venefica.module.utils.Utility;
import com.venefica.services.ImageDto;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author avinash
 * Class for user registration activity.   
 */
public class RegisterUserActivity extends VeneficaActivity implements OnClickListener{
	/**
	 * Input fields for user data.
	 */
	private EditText edtLogin, edtPassword, edtEmail, edtPhone, 
		edtFirstName, edtLastName, edtDateOfBirth, edtZipCode, edtCounty, edtCity, edtArea;
	/**
	 * Text labels for user data input fields.
	 */
	private TextView txtTitle, txtProfileImage;
	/**
	 * Check box for business account type.
	 */
	private CheckBox chkBusinessAcc;
	/**
	 * BitmapView for profile image
	 */
	private ImageView profileImage;
	/**
	 * Bitmap for profile image
	 */
	private Bitmap profileBitmap;
	/**
	 * Buttons for profile selection and signup
	 */
	private Button btnSelProfileImg, btnSignup, btnRegOption;
	/**
	 * Field validator
	 */
	private InputFieldValidator vaildator;
	
	/**
	 * Activity request code
	 */
	private final int REQ_GET_IMAGE = 1001;
	/**
	 * Activity MODE
	 */
	public static final int MODE_REGISTER_USR = 3001, MODE_UPDATE_PROF = 3002,
			MODE_GET_USER = 3003, MODE_VERIFY_CODE = 3004;
	/**
	 * Current MODE
	 */
	private int CURRENT_MODE = MODE_REGISTER_USR;
	/**
	 * Constants to identify dialogs
	 */
	private final int D_PROGRESS = 1, D_ERROR = 2, D_DATE = 3;
	/**
	 * Constants to identify auth request
	 */
	public static final int ACT_REGISTER_BY_FACEBOOK = 11, ACT_REGISTER_BY_TWITTER = 12,ACT_REGISTER_BY_VK = 13, ACT_REGISTER_BY_VENEFICA = 14;
	/**
	 * Auth type
	 */
	private static int AUTH_TYPE = -1;
	/**
	 * Current error code.
	 */
	private int ERROR_CODE;
	/**
	 * Current action code.
	 *//*
	private int ACTION_CODE;*/
	/**
	 * To call web service
	 */
	private WSAction wsAction;
	/**
	 * User data object
	 */
	private UserDto user = null;
	/**
	 * Calendar for current date
	 */
	private Calendar calendar = Calendar.getInstance();
	/**
	 * Date of birth
	 */
	private Date dateOfBitrh;
	/**
	 * flag true if email as userid false if phone
	 */
	private boolean byEmail = false;
	/**
	 * View groups
	 */
	private RelativeLayout layVerify, layJoin, layUpdate;
	/**
	 * Buttons new layout
	 */
	private Button btnVerify, btnReqInvitation, btnFacebook, btnTwitter, btnJoin, btnLostInv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light_DarkActionBar);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_register_user);
        setSupportProgressBarIndeterminateVisibility(false);
        user = new UserDto();
        //Get mode
        CURRENT_MODE = getIntent().getIntExtra("activity_mode", MODE_REGISTER_USR);
        AUTH_TYPE = getIntent().getIntExtra("auth_type", ACT_REGISTER_BY_VENEFICA);
        //view groups
        layVerify = (RelativeLayout) findViewById(R.id.layActRegUserVerify);
        layJoin = (RelativeLayout) findViewById(R.id.layActRegUserJoin);
        layUpdate = (RelativeLayout) findViewById(R.id.layActRegUserUpdate);
        //Buttons new design
        btnVerify = (Button) findViewById(R.id.btnActRegUserVerify);
        btnVerify.setOnClickListener(this);
        btnReqInvitation = (Button) findViewById(R.id.btnActRegUserReqInv);
        btnReqInvitation.setOnClickListener(this);
        btnFacebook = (Button) findViewById(R.id.btnActRegUserFacebook);
        btnFacebook.setOnClickListener(this);
        btnTwitter = (Button) findViewById(R.id.btnActRegUserTwitter);
        btnTwitter.setOnClickListener(this);
        btnJoin = (Button) findViewById(R.id.btnActRegUserJoin);
        btnJoin.setOnClickListener(this);
        btnLostInv = (Button) findViewById(R.id.btnActRegUserForgotInvCode);
        btnLostInv.setOnClickListener(this);
        
        //TextViews
        txtTitle = (TextView) findViewById(R.id.txtActRegUserHeader);
    	txtProfileImage = (TextView) findViewById(R.id.txtActRegUserProfileImg);
    	
        //Profile image field
        profileImage = (ImageView) findViewById(R.id.imgActRegUserProfileImg);
        //Business chk box
        chkBusinessAcc = (CheckBox) findViewById(R.id.chkActRegUserBusinessAcc);

        //Input fields
        edtLogin = (EditText) findViewById(R.id.edtActRegUserLogin);
        edtPassword = (EditText) findViewById(R.id.edtActRegUserPassword);
        edtEmail = (EditText) findViewById(R.id.edtActRegUserEmail);
        edtEmail.setText(Utility.getEmail(this));
        edtPhone = (EditText) findViewById(R.id.edtActRegUserPhone);
        edtPhone.setText(Utility.getPhoneNo(this));
		edtFirstName = (EditText) findViewById(R.id.edtActRegUserFName);
		edtLastName = (EditText) findViewById(R.id.edtActRegUserLName);
		edtDateOfBirth = (EditText) findViewById(R.id.edtActRegUserBirthDate);
		edtZipCode = (EditText) findViewById(R.id.edtActRegUserZip);
		edtCounty = (EditText) findViewById(R.id.edtActRegUserCounty);
		edtCity = (EditText) findViewById(R.id.edtActRegUserCity);
		edtArea = (EditText) findViewById(R.id.edtActRegUserArea);        
        
		edtDateOfBirth.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				showDialog(D_DATE);
				return true;
			}
		});
		//Buttons
        btnSelProfileImg = (Button) findViewById(R.id.btnActRegUserSelProfileImg);
        btnSelProfileImg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
//				profileImage.setImageBitmap(Utility.getUserFacebookPic("avinash.sardar.146"));
				pickImage();
			}
		});
        btnSignup = (Button) findViewById(R.id.btnActRegUserSignUp);
        
        if(AUTH_TYPE != ACT_REGISTER_BY_VENEFICA){
        	btnSignup.setText(getResources().getString(R.string.label_btn_save));
        }
        btnSignup.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(WSAction.isNetworkConnected(RegisterUserActivity.this)){
					if(btnSignup.getText().toString().equals(getResources().getString(R.string.label_act_reg_user_btn_join))){
						//Call web service to create user account
						getUserData();
						new RegisterUserTask().execute(MODE_REGISTER_USR+"");
//						//Show profile fields after creating user
//						setProflieFieldsVisibility(View.VISIBLE);
//						btnSignup.setText(getResources().getString(R.string.label_btn_save));					
//						txtTitle.setText(getResources().getString(R.string.label_act_reg_user_complete_profile));
//						//set DOB to current date
//						edtDateOfBirth.setText(Constants.dateFormat.format(calendar.getTime()));
					}else if(btnSignup.getText().toString().equals(getResources().getString(R.string.label_btn_save))){
						//Update user profile
						if(validateFields()){
							getUserData();
							new RegisterUserTask().execute(MODE_UPDATE_PROF+"");
						}
					}					
				}else{
					ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
					showDialog(D_ERROR);
				}
			}
		});
        //option link button
        btnRegOption = (Button) findViewById(R.id.btnActRegUserOption);
        btnRegOption.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(btnRegOption.getText().toString().equals(getResources().getString(R.string.label_act_reg_user_btn_option_phone))){
					btnRegOption.setText(getResources().getString(R.string.label_act_reg_user_btn_option_email));
//					showPhone();
				}else if(btnRegOption.getText().toString().equals(getResources().getString(R.string.label_act_reg_user_btn_option_email))){
//					showEmail();
					btnRegOption.setText(getResources().getString(R.string.label_act_reg_user_btn_option_phone));
				}
			}
		});
        if (CURRENT_MODE == MODE_VERIFY_CODE) {
			layJoin.setVisibility(ViewGroup.GONE);
			layVerify.setVisibility(ViewGroup.VISIBLE);
		} else if(CURRENT_MODE == MODE_REGISTER_USR){
        	//New user registration fields
	        setProflieFieldsVisibility(View.GONE);
//	        showPhone();
        }else if(CURRENT_MODE == MODE_UPDATE_PROF){
        	getSupportActionBar().setIcon(R.drawable.icon_undo);
        	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        	layJoin.setVisibility(ViewGroup.GONE);
			layVerify.setVisibility(ViewGroup.GONE);
			layUpdate.setVisibility(ViewGroup.VISIBLE);
        	//update/ complete profile fields
        	setProflieFieldsVisibility(View.VISIBLE);
        	
        	btnSignup.setText(getResources().getString(R.string.label_btn_save));
        	//get existing user details
        	new RegisterUserTask().execute(MODE_GET_USER+"");
        }
    }
    /* (non-Javadoc)
	 * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
	 */
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    	if(id == D_ERROR) {
    		String message = "";
    		//Display error message as per the error code
    		if (ERROR_CODE == Constants.ERROR_NETWORK_UNAVAILABLE) {
    			message = (String) getResources().getText(R.string.error_network_01);
			} else if(ERROR_CODE == Constants.ERROR_NETWORK_CONNECT){
				message = (String) getResources().getText(R.string.error_network_02);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_REGISTER_USER){
				message = (String) getResources().getText(R.string.error_register_user);
			}else if(ERROR_CODE == Constants.ERROR_RESULT_UPDTAE_USER){
				message = (String) getResources().getText(R.string.error_update_user);
			}else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_DUP_EMAIL){
				message = (String) getResources().getText(R.string.msg_reg_user_duplicate_email);
			}else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_DUP_LOGIN){
				message = (String) getResources().getText(R.string.msg_reg_user_duplicate_login);
			}else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_DUP_PHONE){
				message = (String) getResources().getText(R.string.msg_reg_user_duplicate_phone);
			}else if(ERROR_CODE == Constants.RESULT_REGISTER_USER_SUCCESS){
				message = (String) getResources().getText(R.string.msg_reg_user_registration_success);
			}else if(ERROR_CODE == Constants.RESULT_UPDATE_USER_SUCCESS){
				message = (String) getResources().getText(R.string.msg_update_user_success);
			}
    		((AlertDialog) dialog).setMessage(message);
		}    	
    }
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
    protected Dialog onCreateDialog(int id) {
    	//Create progress dialog
    	if(id == D_PROGRESS){
    		ProgressDialog pDialog = new ProgressDialog(RegisterUserActivity.this);
			pDialog.setTitle(getResources().getString(R.string.app_name));
			pDialog.setMessage(getResources().getString(R.string.msg_progress));
			pDialog.setIcon(R.drawable.ic_launcher);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			return pDialog;
		}
    	//Create error dialog
    	if(id == D_ERROR){
    		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUserActivity.this);
			builder.setTitle(R.string.app_name);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setMessage("");
			builder.setCancelable(true);
			builder.setNeutralButton(R.string.label_btn_ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(D_ERROR);
					if(ERROR_CODE == Constants.RESULT_REGISTER_USER_SUCCESS){
//						RegisterUserActivity.this.finish();
					}
				}
			});			
			AlertDialog aDialog = builder.create();
			return aDialog;
		}
    	if(id == D_DATE){
    		DatePickerDialog dateDg = new DatePickerDialog(RegisterUserActivity.this, new OnDateSetListener() {
				
				public void onDateSet(DatePicker arg0, int year, int month, int date) {
					edtDateOfBirth.setText((month>9? month: "0"+month)+"/"+(date>9? date: "0"+date)+"/"+year);					
				}
			}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			return dateDg;
    	}
    	return null;
    }
    /**
     * Method to show hide group of components
     * @param visibility
     */
    private void setProflieFieldsVisibility(int visibility){
    	chkBusinessAcc.setVisibility(visibility);
		profileImage.setVisibility(visibility);
		btnSelProfileImg.setVisibility(visibility);
		btnRegOption.setVisibility(visibility== View.VISIBLE ? View.GONE :View.VISIBLE);
		
    	edtLogin.setVisibility(visibility);
//    	edtPassword.setVisibility(visibility);
    	edtEmail.setVisibility(visibility);
    	edtPhone.setVisibility(visibility); 
		edtFirstName.setVisibility(visibility);
		edtLastName.setVisibility(visibility);
		edtDateOfBirth.setVisibility(visibility);
		edtZipCode.setVisibility(visibility);
		edtCounty.setVisibility(visibility);
		edtCity.setVisibility(visibility);
		edtArea.setVisibility(visibility);
		
		txtProfileImage.setVisibility(visibility);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			finish();
		}
    	return true;
    }
    
    /**
     * Method to validate input fields for registration
     * @return result of validation
     */
    private boolean validateFields(){
    	boolean result = true;
    	StringBuffer message = new StringBuffer();
    	if(vaildator == null){
    		vaildator = new InputFieldValidator();    		
    	}
    	if(!byEmail && !vaildator.validateField(edtLogin, Pattern.compile(InputFieldValidator.charNumPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_login).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.hint_user_password_pattern));
    		message.append("\n");
    	}else if(byEmail && !vaildator.validateField(edtLogin, Pattern.compile(InputFieldValidator.emailPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_login).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_email));
    		message.append("\n");
    	}
    	/*if(!vaildator.validateField(edtPassword, Pattern.compile(InputFieldValidator.charNumPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_password).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.hint_user_password_pattern));
    		message.append("\n");
    	}*/
    	if(!vaildator.validateField(edtEmail, Pattern.compile(InputFieldValidator.emailPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_email).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_email));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtPhone, Pattern.compile(InputFieldValidator.phonePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_phone).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_phone));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtFirstName, Pattern.compile(InputFieldValidator.userNamePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_f_name).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtLastName, Pattern.compile(InputFieldValidator.userNamePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_l_name).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_fname_lname));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtZipCode, Pattern.compile(InputFieldValidator.zipCodePatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_zip).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_zipcode));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCounty, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_county).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtCity, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_city).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if(!vaildator.validateField(edtArea, Pattern.compile(InputFieldValidator.countyCityAreaPatternRegx))){
    		result = false;
    		message.append(getResources().getString(R.string.label_area).toString());
    		message.append("- ");
    		message.append(getResources().getString(R.string.msg_validation_county_city_area));
    		message.append("\n");
    	}
    	if (!result) {
			Utility.showLongToast(this, message.toString()); 
		}else{
			getUserData();
		}
		return result;
    	
    }
    /**
     * Get image from gallery
     */
    private void pickImage() {
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
    	intent.setType("image/*");
    	intent.putExtra("crop", "true");
    	intent.putExtra("aspectX", Constants.IMAGE_ASPECT_X);
    	intent.putExtra("aspectY", Constants.IMAGE_ASPECT_Y);
    	intent.putExtra("outputX", Constants.IMAGE_MAX_SIZE_X);
    	intent.putExtra("outputY", Constants.IMAGE_MAX_SIZE_Y);
    	intent.putExtra("scale", true);
    	intent.putExtra("return-data", true);
        startActivityForResult(intent, REQ_GET_IMAGE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GET_IMAGE && resultCode == Activity.RESULT_OK){
        	profileBitmap = (Bitmap)data.getExtras().getParcelable("data");
        	ImageDto image = new ImageDto(profileBitmap);
        	if ((image.getData().length() / 1024.0f) > Constants.IMAGE_THUMBNAILS_MIN_SIZE) {	            
	            profileImage.setImageBitmap(profileBitmap);
        	} else {
    			Utility.showLongToast(this, getResources().getString(R.string.msg_postlisting_low_resolution));
    		}  
        }        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
	/**
	 * Method to get user data from input fields
	 */
	private void getUserData(){
		try {
			user.setBusinessAcc(chkBusinessAcc.isChecked());
//			user.setEmail(edtEmail.getText().toString());
//			user.setPhoneNumber(edtPhone.getText().toString());
			user.setFirstName(edtFirstName.getText().toString());
			user.setLastName(edtLastName.getText().toString());
			if (CURRENT_MODE == MODE_REGISTER_USR) {
				if (byEmail) {
					user.setName(edtEmail.getText().toString());
					edtLogin.setText(edtEmail.getText().toString());
					user.setEmail(edtEmail.getText().toString());
					user.setPhoneNumber(null);
				} else {
					user.setName(edtPhone.getText().toString());
					edtLogin.setText(edtPhone.getText().toString());
					user.setEmail(null);
					user.setPhoneNumber(edtPhone.getText().toString());
				}				
			}else{
				user.setName(edtLogin.getText().toString());
				user.setEmail(edtEmail.getText().toString());
				user.setPhoneNumber(edtPhone.getText().toString());
			}
			if(!edtDateOfBirth.getText().toString().equals("")){
				user.setDateOfBirth(((Date)new SimpleDateFormat("yyyy/mm/dd", Locale.getDefault()).parse(edtDateOfBirth.getText().toString())));
			}else{
				user.setDateOfBirth(calendar.getTime());
			}
			user.setZipCode(edtZipCode.getText().toString());
			user.setCity(edtCity.getText().toString());
			user.setCounty(edtCounty.getText().toString());
			user.setArea(edtArea.getText().toString());
			if(profileBitmap != null){
				user.setAvatar(new ImageDto(profileBitmap));
			}
		} catch (ParseException e) {
			Log.e("RegisterUserActivity::getUserData: ", e.toString());
		}			
	}
	
	/**
	 * @author avinash
	 * Class to perform network task.
	 */
	class RegisterUserTask extends AsyncTask<String, Integer, UserRegistrationResultWrapper>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			showDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(true);
		}
		@Override
		protected UserRegistrationResultWrapper doInBackground(String... params) {
			UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
			try {
				if(wsAction == null ){
					wsAction = new WSAction();
				}
				if (params[0].equalsIgnoreCase(MODE_REGISTER_USR+"")) {					
					wrapper.result = wsAction.registerUser(/*edtPassword.getText().toString()*/"81064678", user);					
				}else if (params[0].equalsIgnoreCase(MODE_UPDATE_PROF+"")) {
					wrapper.result = wsAction.updateUser(((VeneficaApplication)getApplication()).getAuthToken(), user);
				}else if (params[0].equalsIgnoreCase(MODE_GET_USER+"")) {
					wrapper.userDto = wsAction.getUser(((VeneficaApplication)getApplication()).getAuthToken());
				}
			}catch (IOException e) {
				Log.e("AuthenticationTask::doInBackground :", e.toString());
				wrapper.result = Constants.ERROR_NETWORK_CONNECT;
			} catch (XmlPullParserException e) {
				Log.e("AuthenticationTask::doInBackground :", e.toString());
			}
			return wrapper;
		}
		@Override
		protected void onPostExecute(UserRegistrationResultWrapper result) {
			super.onPostExecute(result);
//			dismissDialog(D_PROGRESS);
			setSupportProgressBarIndeterminateVisibility(false);
			if(result.userDto == null && result.result == -1){
				ERROR_CODE = Constants.ERROR_NETWORK_CONNECT;
				showDialog(D_ERROR);
			}else if (result.userDto != null) {
				setUserData(result.userDto);
			}else if (result.result != -1) {
				ERROR_CODE = result.result;
				showDialog(D_ERROR);
				if(ERROR_CODE == Constants.RESULT_REGISTER_USER_SUCCESS){
					/*//Show profile fields after creating user
					setProflieFieldsVisibility(View.VISIBLE);
					btnSignup.setText(getResources().getString(R.string.label_btn_save));					
					txtTitle.setText(getResources().getString(R.string.label_act_reg_user_complete_profile));
					//set DOB to current date
					edtDateOfBirth.setText(Constants.dateFormat.format(calendar.getTime()));*/
				}
			}
		}
	}
	/**
	 * Method to set provided user data to input fields. Used in update profile.
	 * @param userDto
	 */
	public void setUserData(UserDto userDto) {
		chkBusinessAcc.setChecked(userDto.isBusinessAcc());
		/*ImageDownloadManager.getImageDownloadManagerInstance()
				.loadDrawable(Constants.PHOTO_URL_PREFIX + userData.getAvatar().getUrl(), profileImage
						, getResources().getDrawable(R.drawable.icon_picture_white));*/
		((VeneficaApplication)getApplication()).getImgManager().loadImage(Constants.PHOTO_URL_PREFIX + userDto.getAvatar().getUrl(), profileImage
				, getResources().getDrawable(R.drawable.icon_picture_white));
		
    	edtLogin.setText(userDto.getName());
//    	edtPassword.setVisibility(visibility);
    	if (!userDto.getEmail().equals("")) {
    		edtEmail.setText(userDto.getEmail());
		}
    	if (!userDto.getPhoneNumber().equals("")) {
    		edtPhone.setText(userDto.getPhoneNumber());
		}    	 
		edtFirstName.setText(userDto.getFirstName());
		edtLastName.setText(userDto.getLastName());
		if (userDto.getDateOfBirth() != null) {
			edtDateOfBirth.setText(Constants.dateFormat.format(userDto.getDateOfBirth()));
		}	
		edtZipCode.setText(userDto.getZipCode());
		edtCounty.setText(userDto.getCounty());
		edtCity.setText(userDto.getCity());
		edtArea.setText(userDto.getArea());		
	}
	@Override
	public void onClick(View v) {
		if(WSAction.isNetworkConnected(RegisterUserActivity.this)){
			if (v.getId() == R.id.btnActRegUserVerify) {
				layVerify.setVisibility(ViewGroup.GONE);
				layJoin.setVisibility(ViewGroup.VISIBLE);
				Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			} else if (v.getId() == R.id.btnActRegUserReqInv) {
				getUserData();
//				new RegisterUserTask().execute(MODE_REGISTER_USR+"");
				Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			} else if (v.getId() == R.id.btnActRegUserFacebook) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			} else if (v.getId() == R.id.btnActRegUserTwitter) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			} else if (v.getId() == R.id.btnActRegUserJoin) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			} else if (v.getId() == R.id.btnActRegUserForgotInvCode) {
				Utility.showLongToast(this, getResources().getString(R.string.msg_blocked));
			}
		}else{
			ERROR_CODE = Constants.ERROR_NETWORK_UNAVAILABLE;
			showDialog(D_ERROR);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (layJoin.getVisibility() == ViewGroup.VISIBLE) {
			layVerify.setVisibility(ViewGroup.VISIBLE);
			layJoin.setVisibility(ViewGroup.GONE);
		} else {
			super.onBackPressed();
		}
	}
}
