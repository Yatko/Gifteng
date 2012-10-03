package org.ksoap2.transport;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.ksoap2.HeaderProperty;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * HttpsServiceConnectionSE is a service connection that uses a https url
 * connection and requires explicit setting of host, port and file.
 * 
 * The explicit setting is necessary since pure url passing and letting the Java
 * URL class parse the string does not work properly on Android.
 * 
 * Links for reference:
 * 
 * @see "http://stackoverflow.com/questions/2820284/ssl-on-android-strange-issue"
 * @see "http://stackoverflow.com/questions/2899079/custom-ssl-handling-stopped-working-on-android-2-2-froyo"
 * @see "http://code.google.com/p/android/issues/detail?id=2690"
 * @see "http://code.google.com/p/android/issues/detail?id=2764"
 * 
 * @see "https://gist.github.com/908048" There can be problems with the
 *      certificate of theof the server on older android versions. You can
 *      disable SSL for the versions only e.g. with an approach like this.
 * 
 * @author Manfred Moser <manfred@simpligility.com>
 */
public class HttpsServiceConnectionSE implements ServiceConnection
{

	private HttpsURLConnection connection;

	public static class _FakeX509TrustManager implements javax.net.ssl.X509TrustManager
	{
		private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]
		{};

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
		{
		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
		{
		}

		public boolean isClientTrusted(X509Certificate[] chain)
		{
			return (true);
		}

		public boolean isServerTrusted(X509Certificate[] chain)
		{
			return (true);
		}

		public X509Certificate[] getAcceptedIssuers()
		{
			return (_AcceptedIssuers);
		}
	}

	private TrustManager[] trustAllCerts = new TrustManager[]
	{ new _FakeX509TrustManager() };

	/**
	 * Create the transport with the supplied parameters.
	 * 
	 * @param host
	 *            the name of the host e.g. webservices.somewhere.com
	 * @param port
	 *            the http port to connect on
	 * @param file
	 *            the path to the file on the webserver that represents the
	 *            webservice e.g. /api/services/myservice.jsp
	 * @param timeout
	 *            the timeout for the connection in milliseconds
	 * @throws IOException
	 */
	public HttpsServiceConnectionSE(String host, int port, String file, int timeout) throws IOException
	{
		
		//TODO WARNING!!! SSL сертификат не проверяется, вместо него фейковый
		try
		{
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			//-----
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
			{
				public boolean verify(String hostname, SSLSession session)
				{
					return true;
				}
			});

			javax.net.ssl.SSLContext context = null;

			if (trustAllCerts == null)
			{
				trustAllCerts = new javax.net.ssl.TrustManager[]
				{ new _FakeX509TrustManager() };
			}

			try
			{
				context = javax.net.ssl.SSLContext.getInstance("TLS");
				context.init(null, trustAllCerts, new SecureRandom());
			}
			catch (NoSuchAlgorithmException e)
			{
				Log.e("allowAllSSL", e.toString());
			}
			catch (KeyManagementException e)
			{
				Log.e("allowAllSSL", e.toString());
			}
			//-----
		}
		catch (Exception e)
		{
			e.getMessage();
		}

		connection = (HttpsURLConnection)new URL(HttpsTransportSE.PROTOCOL, host, port, file).openConnection();
		((HttpsURLConnection)connection).setHostnameVerifier(new AllowAllHostnameVerifier());
		updateConnectionParameters(timeout);
	}

	private void updateConnectionParameters(int timeout)
	{
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout); // even if we connect fine we want to time out if we cant read anything..
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setDoInput(true);
	}

	public void connect() throws IOException
	{
		connection.connect();
	}

	public void disconnect()
	{
		connection.disconnect();
	}

	public List<HeaderProperty> getResponseProperties()
	{
		Map<?, ?> properties = connection.getHeaderFields();
		Set<?> keys = properties.keySet();
		List<HeaderProperty> retList = new LinkedList<HeaderProperty>();

		for (Iterator<?> i = keys.iterator(); i.hasNext();)
		{
			String key = (String)i.next();
			List<?> values = (List<?>)properties.get(key);

			for (int j = 0; j < values.size(); j++)
			{
				retList.add(new HeaderProperty(key, (String)values.get(j)));
			}
		}

		return retList;
	}

	public void setRequestProperty(String key, String value)
	{
		connection.setRequestProperty(key, value);
	}

	public void setRequestMethod(String requestMethod) throws IOException
	{
		connection.setRequestMethod(requestMethod);
	}

	public OutputStream openOutputStream() throws IOException
	{
		return connection.getOutputStream();
	}

	public InputStream openInputStream() throws IOException
	{
		return connection.getInputStream();
	}

	public InputStream getErrorStream()
	{
		return connection.getErrorStream();
	}

	public String getHost()
	{
		return connection.getURL().getHost();
	}

	public int getPort()
	{
		return connection.getURL().getPort();
	}

	public String getPath()
	{
		return connection.getURL().getPath();
	}

	public void setSSLSocketFactory(SSLSocketFactory sf)
	{
		connection.setSSLSocketFactory(sf);
	}
}
