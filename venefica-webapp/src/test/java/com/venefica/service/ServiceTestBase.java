package com.venefica.service;

import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.common.DumpErrorTestExecutionListener;
import com.venefica.config.Constants;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.inject.Inject;
import static junit.framework.Assert.assertNotNull;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.junit.Before;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Utility class to simplify testing.
 *
 * @param <T>
 * @author Sviatoslav Grebenchukov
 * @author gyuszi
 */
@TestExecutionListeners({DumpErrorTestExecutionListener.class, TransactionalTestExecutionListener.class})
public abstract class ServiceTestBase<T> {

    public static final Long FIRST_USER_ID = new Long(1);
    public static final Long SECOND_USER_ID = new Long(2);
    public static final Long THIRD_USER_ID = new Long(3);
    
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
    public void initUsersAndAuthTokens() throws TokenEncryptionException {
        firstUser = initUser(FIRST_USER_ID);
        firstUserAuthToken = initAuthToken(FIRST_USER_ID);
        
        secondUser = initUser(SECOND_USER_ID);
        secondUserAuthToken = initAuthToken(SECOND_USER_ID);
        
        thirdUser = initUser(THIRD_USER_ID);
        thirdUserAuthToken = initAuthToken(THIRD_USER_ID);
    }
    
    /**
     * Returns the endpoint address of the service.
     *
     * @return
     */
    protected String getEndpointAddress() {
        return endpointAddress;
    }

    /**
     * Returns first-test user.
     *
     * @return
     */
    protected User getFirstUser() {
        return firstUser;
    }

    /**
     * Returns second-test user .
     *
     * @return
     */
    protected User getSecondUser() {
        return secondUser;
    }

    /**
     * Returns third-test user.
     *
     * @return
     */
    protected User getThirdUser() {
        return thirdUser;
    }

    /**
     * Adds authentication token to request headers of the underlying cxf
     * client.
     *
     * @param token
     */
    protected void authenticateClientWithToken(String token) {
        List<String> tokenHeader = new ArrayList<String>();
        tokenHeader.add(token);

        Map<String, List<?>> headers = new HashMap<String, List<?>>();
        headers.put(Constants.AUTH_TOKEN, tokenHeader);
        
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
        DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionManager.getTransaction(def);
    }

    protected void commitTransaction(TransactionStatus status) {
        transactionManager.commit(status);
    }

    protected void rollbackTransaction(TransactionStatus status) {
        transactionManager.rollback(status);
    }
    
    /**
     * Retrieves the user on the specified id. Method will fail with assertion
     * exception if the user cannot be found.
     * 
     * @param userId unique identifier
     * @return resulted user
     */
    private User initUser(Long userId) {
        User user = userDao.get(userId);
        assertNotNull("User (id = " + userId + ") not found!", user);
        return user;
    }
    
    /**
     * Generates an encrypted string for the Token of the given user id.
     * 
     * @param userId
     * @return
     * @throws TokenEncryptionException 
     */
    private String initAuthToken(Long userId) throws TokenEncryptionException {
        String authToken = tokenEncryptor.encrypt(new Token(userId));
        return authToken;
    }
}
