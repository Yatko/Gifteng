package com.venefica.service;

import com.venefica.service.fault.AuthenticationException;
import com.venefica.service.fault.AuthorizationException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.commons.codec.binary.Base64;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = "/AuthServiceTest-context.xml")
public class AuthServiceTest extends ServiceTestBase<AuthService> {

    private static final String FIRST_USER_NAME = "first";
    
    private static final String UNEXISTING_USER_NAME = "unexistingTestUser";
    
    private static final String RIGHT_PASSWORD = "12345";
    private static final String WRONG_PASSWORD = "wrong";

    public AuthServiceTest() {
        super(AuthService.class);
    }

    @Test
    public void secretKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(192);

        SecretKey key = kgen.generateKey();
        byte[] keyBytes = key.getEncoded();

        String encodedKey = Base64.encodeBase64String(keyBytes);
        System.out.printf("Secret key: %s\n", encodedKey);
    }

    @Test
    public void correctCredentialsTest() throws AuthenticationException {
        String token = client.authenticate(FIRST_USER_NAME, RIGHT_PASSWORD);
        assertNotNull("Token is null!", token);
    }

    @Test(expected = AuthenticationException.class)
    public void incorrectCredentialsTest() throws AuthenticationException {
        client.authenticate(FIRST_USER_NAME, WRONG_PASSWORD);
    }

    @Test(expected = AuthenticationException.class)
    public void unexistingUserTest() throws AuthenticationException {
        client.authenticate(UNEXISTING_USER_NAME, RIGHT_PASSWORD);
    }

    @Test(expected = AuthenticationException.class)
    public void nullUserNameTest() throws AuthenticationException {
        client.authenticate(null, RIGHT_PASSWORD);
    }

    @Test(expected = AuthenticationException.class)
    public void nullPasswordTest() throws AuthenticationException {
        client.authenticate(FIRST_USER_NAME, null);
    }

    @Test(expected = AuthenticationException.class)
    public void emptyPasswordTest() throws AuthenticationException {
        client.authenticate(FIRST_USER_NAME, "");
    }

    @Test(expected = SOAPFaultException.class)
    public void unauthorizedChangePasswordTest() throws AuthorizationException {
        client.changePassword(RIGHT_PASSWORD, "new password");
    }

    @Test(expected = AuthorizationException.class)
    public void wrongChangePasswordTest() throws AuthorizationException {
        authenticateClientAsFirstUser();
        client.changePassword(WRONG_PASSWORD, "new password");
    }

    @Test
    public void changePasswordTest() throws AuthorizationException {
        authenticateClientAsFirstUser();
        client.changePassword(RIGHT_PASSWORD, "new password");
    }
}
