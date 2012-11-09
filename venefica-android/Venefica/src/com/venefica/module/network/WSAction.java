package com.venefica.module.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.xmlpull.v1.XmlPullParserException;

import com.venefica.module.user.UserDto;
import com.venefica.module.user.UserRegistrationResultWrapper;
import com.venefica.services.ImageDto;
import com.venefica.services.User;
import com.venefica.services.ServicesManager.AuthenticateResult;
import com.venefica.services.ServicesManager.GetUserResult;
import com.venefica.services.ServicesManager.IsUserCompleteResult;
import com.venefica.services.ServicesManager.RegisterUserResult;
import com.venefica.services.ServicesManager.RegisterUserReturn;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.services.ServicesManager.UpdateUserResult;
import com.venefica.services.ServicesManager.UpdateUserReturn;
import com.venefica.utils.Constants;
import com.venefica.utils.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author avinash
 * Class to perform webservice operations.
 */
public class WSAction {
	/**
	 * Web method name constants
	 */
	private static final String WS_METHOD_AUTHENTICATE = "Authenticate";
	private static final String WS_METHOD_IS_USER_REGISTERED = "IsUserComplete";
	private static final String WS_METHOD_GET_USER = "GetUser";
	private static final String WS_METHOD_UPDATE_USER = "UpdateUser";
//	private static final String IS_USER_COMPLETE_METHOD = "IsUserComplete";
	private static final String WS_METHOD_REGISTER_USER = "RegisterUser";
	
	/**
	 * TO hold soap action
	 */
	private String soapAction;
	/**
	 * Method to check network connection's availability
	 * @param context
	 * @return connection state
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	private static String getStringFromProperty(Object responseObj, final String defaultValue){
		String result = defaultValue;
		try{
			if (responseObj.getClass() == SoapPrimitive.class)
				result = responseObj.toString();
		}catch (Exception ex){
			Log.d("WSAction::getStringFromProperty :", ex.getLocalizedMessage());
		}
		return result;
	}
	/**
	 * Method to authenticate user
	 * @param user
	 * @param password
	 * @return result
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public UserRegistrationResultWrapper authenticateUser(String user, String password) throws IOException, XmlPullParserException{
		UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
		try {			
			soapAction = Constants.SERVICES_NAMESPACE + WS_METHOD_AUTHENTICATE ;
			
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_AUTHENTICATE);

			request.addProperty("name", user);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_AUTH_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(soapAction, envelope, headerList);

			Object obj = envelope.getResponse();
			if (obj.toString().indexOf("Fault") > -1){
				throw(new SoapFault());
			}else{
				//Parse response
				wrapper.data = getStringFromProperty(obj,null);
				wrapper.result = Constants.RESULT_USER_AUTHORISED;
			}
		} catch (SoapFault e){
			String message = e.getMessage();
			if (message.contains("Wrong user name or password!")){
				wrapper.result = Constants.ERROR_USER_UNAUTHORISED;
			}
		}
		return wrapper;
	}
	
	/**
	 * Method to check if user is already registered.
	 * @param data validation data from social network.
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public UserRegistrationResultWrapper checkUserRegistration(String token) throws IOException, XmlPullParserException{
		UserRegistrationResultWrapper wrapper = new UserRegistrationResultWrapper();
		Object obj;
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_IS_USER_REGISTERED;
		SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_IS_USER_REGISTERED);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);

		List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
		headerList.add(new HeaderProperty("authToken", token));

		androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
		obj = envelope.getResponse();
		wrapper.data = getStringFromProperty(obj, "false");
		wrapper.result = Constants.RESULT_IS_USER_EXISTS_SUCCESS;
		return wrapper;
	}

	/**
	 * Method to update user profile details
	 * @param data
	 * @param user
	 * @return result
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public int updateUser(String token, UserDto user) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_UPDATE_USER;
		int result = -1;

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_UPDATE_USER);

			request.addProperty("user", user);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);
			new ImageDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;

			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
			Object obj = envelope.getResponse();

			boolean res = Boolean.parseBoolean(getStringFromProperty(obj, "false"));
			if (res){
				result = Constants.RESULT_UPDATE_USER_SUCCESS;
			}else{
				result = Constants.ERROR_RESULT_UPDTAE_USER;
			}
		}catch (SoapFault e){
			String message = e.getMessage();
			if (message.equalsIgnoreCase("EMAIL")){
				result = Constants.RESULT_REGISTER_USER_DUP_EMAIL;
			}else if (message.equalsIgnoreCase("PHONE")){
				result = Constants.RESULT_REGISTER_USER_DUP_PHONE;
			}else{
				result = Constants.ERROR_RESULT_UPDTAE_USER;
			}
		}
		return result;
	}
	
	/**
	 * Method to register new user
	 * @param password
	 * @param user
	 * @return result
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	
	public int registerUser(String password, UserDto user)
			throws IOException, XmlPullParserException {
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_REGISTER_USER;
		int result = -1;

		try {
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_REGISTER_USER);

			request.addProperty("user", user);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().register(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", ""));

			HttpTransportSE androidHttpTransport = Utils
					.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			Object obj = envelope.getResponse();

			if (obj != null) {
				result = Constants.RESULT_REGISTER_USER_SUCCESS;
			}
		} catch (SoapFault e) {
			if (e.detail != null) {
				Element UserAlreadyExists = (Element) ((Element) e.detail.getChild(0)).getChild(0);
				if (UserAlreadyExists.getName().equalsIgnoreCase("UserAlreadyExists")) {
					Element duplicatedField = (Element) UserAlreadyExists
							.getChild(0);
					if (duplicatedField.getName().equalsIgnoreCase("duplicatedField")) {
						String message = (String) duplicatedField.getChild(0);
						if (message.equalsIgnoreCase("EMAIL")) {
							result = Constants.RESULT_REGISTER_USER_DUP_EMAIL;
						} else if (message.equalsIgnoreCase("PHONE")) {
							result = Constants.RESULT_REGISTER_USER_DUP_PHONE;
						} else if (message.equalsIgnoreCase("NAME")) {
							result = Constants.RESULT_REGISTER_USER_DUP_LOGIN;
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method to get user data for specified data
	 * @param data
	 * @return User
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public User getUser(String token) throws IOException, XmlPullParserException{
		String SOAP_ACTION = Constants.SERVICES_NAMESPACE + WS_METHOD_GET_USER;
		User user = null;

		try{
			SoapObject request = new SoapObject(Constants.SERVICES_NAMESPACE, WS_METHOD_GET_USER);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.encodingStyle = SoapEnvelope.ENC;
			envelope.setOutputSoapObject(request);
			new UserDto().registerRead(envelope);
			new ImageDto().registerRead(envelope);

			List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
			headerList.add(new HeaderProperty("authToken", token));

			HttpTransportSE androidHttpTransport = Utils.getServicesTransport(Constants.SERVICES_USER_URL);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(SOAP_ACTION, envelope, headerList);

			if (envelope.getResponse() instanceof UserDto){
				user = new User((UserDto)envelope.getResponse());
			}else{
				Log.e("WSAction getUser: ", "UserDto not found.");
				user = null;
			}
		}catch (SoapFault e){
			Log.e("WSAction getUser: ", e.getMessage());
			user = null;
		}
		return user;
	}
}
