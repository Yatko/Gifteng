package com.venefica.service;

import static junit.framework.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;


import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.junit.Before;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.venefica.auth.Token;
import com.venefica.auth.TokenAuthorizationInterceptor;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.dao.UserDao;
import com.venefica.model.User;

/**
 * Utility class to simplify testing.
 * 
 * @author Sviatoslav Grebenchukov
 * 
 */
public abstract class ServiceTestBase<T> {

	public static final Long FirstUserId = new Long(1);
	public static final Long SecondUserId = new Long(2);
	public static final Long ThirdUserId = new Long(3);

	protected final Class<? extends T> serviceClass;
	protected T client;
	protected Client cxfClient;

	@Inject
	private UserDao userDao;

	private User firstUser;
	private String firstUserAuthToken;

	private User secondUser;
	private String secondUserAuthToken;

	private User thirdUser;
	private String thirdUserAuthToken;

	@Resource(name = "publishedUrl")
	private String endpointAddress;

	@Inject
	private TokenEncryptor tokenEncryptor;

	@Inject
	private PlatformTransactionManager transactionManager;

	public ServiceTestBase(Class<? extends T> serviceClass) {
		this.serviceClass = serviceClass;
	}

	@Before
	@SuppressWarnings("unchecked")
	public void initClientAndProxy() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(getEndpointAddress());
		factory.setServiceClass(serviceClass);

		client = (T) factory.create();
		cxfClient = ClientProxy.getClient(client);
	}

	@Before
	public void initUsersAnAuthTokens() throws TokenEncryptionException {
		firstUser = userDao.get(FirstUserId);
		assertNotNull("First user not found!", firstUser);

		firstUserAuthToken = tokenEncryptor.encrypt(new Token(FirstUserId));

		secondUser = userDao.get(SecondUserId);
		assertNotNull("Second user not found!", secondUser);

		secondUserAuthToken = tokenEncryptor.encrypt(new Token(SecondUserId));

		thirdUser = userDao.get(ThirdUserId);
		assertNotNull(thirdUser);

		thirdUserAuthToken = tokenEncryptor.encrypt(new Token(ThirdUserId));
	}

	/**
	 * Returns the endpoint address of the service.
	 */
	protected String getEndpointAddress() {
		return endpointAddress;
	}

	/**
	 * Returns first-test user.
	 */
	protected User getFirstUser() {
		return firstUser;
	}

	/**
	 * Returns second-test user .
	 */
	protected User getSecondUser() {
		return secondUser;
	}

	/**
	 * Returns third-test user.
	 */
	protected User getThirdUser() {
		return thirdUser;
	}

	/**
	 * Adds authentication token to request headers of the underlying cxf client.
	 */
	protected void authenticateClientWithToken(String token) {
		Map<String, List<?>> headers = new HashMap<String, List<?>>();
		List<String> tokenHeader = new ArrayList<String>();
		tokenHeader.add(token);

		headers.put(TokenAuthorizationInterceptor.AUTH_TOKEN, tokenHeader);
		cxfClient.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
	}

	protected void authenticateClientAsFirstUser() {
		authenticateClientWithToken(firstUserAuthToken);
	}

	protected void authenticateClientAsSecondUser() {
		authenticateClientWithToken(secondUserAuthToken);
	}

	protected void authenticateClientAsThirdUser() {
		authenticateClientWithToken(thirdUserAuthToken);
	}

	protected TransactionStatus beginNewTransaction() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition(
				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return transactionManager.getTransaction(def);
	}

	protected void commitTransaction(TransactionStatus status) {
		transactionManager.commit(status);
	}

	protected void rollbackTransaction(TransactionStatus status) {
		transactionManager.rollback(status);
	}

}
