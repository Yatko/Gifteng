package com.venefica.service;

import com.venefica.auth.Token;
import com.venefica.auth.TokenEncryptionException;
import com.venefica.auth.TokenEncryptor;
import com.venefica.common.DumpErrorTestExecutionListener;
import com.venefica.config.Constants;
import com.venefica.dao.UserDao;
import com.venefica.model.User;
import java.util.Arrays;
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
import org.apache.cxf.transport.http.HTTPConduit;
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

    public static final Long FIRST_USER_ID = 1L;
    public static final Long SECOND_USER_ID = 2L;
    public static final Long THIRD_USER_ID = 3L;
    
    public static final String FIRST_USER_NAME = "first";
    public static final String SECOND_USER_NAME = "second";
    public static final String THIRD_USER_NAME = "third";
    
    protected T client;
    private final Class<? extends T> serviceClass;
    private Client cxfClient;
    
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
        factory.setAddress(endpointAddress);
        factory.setServiceClass(serviceClass);

        client = (T) factory.create();
        cxfClient = ClientProxy.getClient(client);
        
        //cxfClient.getRequestContext().put("schema-validation-enabled", "true");
        //cxfClient.getResponseContext().put("schema-validation-enabled", "true");
        
        HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();
        httpConduit.getClient().setReceiveTimeout(120000);
        httpConduit.getClient().setConnectionTimeout(120000);
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
        addToHttpHeader(Constants.AUTH_TOKEN, token);
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
     * Inserts a new key/value pair into HTTP headers.
     * 
     * @param key
     * @param value 
     */
    private void addToHttpHeader(String key, Object value) {
        Object headers = cxfClient.getRequestContext().get(Message.PROTOCOL_HEADERS);
        if ( headers == null || !(headers instanceof Map) ) {
            headers = new HashMap<String, List<?>>();
        }
        
        ((Map) headers).put(key, Arrays.asList(value));
        cxfClient.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
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
