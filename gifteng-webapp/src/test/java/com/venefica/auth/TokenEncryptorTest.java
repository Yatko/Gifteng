/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.auth;

import com.venefica.config.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.junit.Before;
import org.junit.Test;

/**
 * Basically it's not a test !
 * @author gyuszi
 */
public class TokenEncryptorTest {
    
    private String secretKey = "9F4i0ncwFArv4iAvinfh9Q==";
    private TokenEncryptor tokenEncryptor;
    
    @Before
    public void init() throws NoSuchAlgorithmException, NoSuchPaddingException {
        tokenEncryptor = new TokenEncryptor(secretKey);
    }
    
    @Test
    public void testGenerate() throws NoSuchAlgorithmException {
        String res = tokenEncryptor.generate();
        System.out.println("res=" + res);
    }
    
    @Test
    public void testEncrypt() throws TokenEncryptionException {
        Token token = new Token(1L);
        String res = tokenEncryptor.encrypt(token);
        System.out.println("res=" + res);
    }
    
    @Test
    public void testDecrypty() throws TokenDecryptionException {
        Token token = tokenEncryptor.decrypt("eneGKi4Soqsw+1gvL7DjNIYuW02B867KmXGwE51Ubd+jurflJqquNQjciZvEf01raCD3au231wmQaoIlSXtIY5GL7aHAF6ue2bYrrgLOIZYYOprbOTcP6mPgptJtCoiqKurReT1m6zlu22D/1LY8dIxk9+Nlqfvvd2kaDnTz8Y3fd64as9+7o1vxOQmcXn/gijAKIfpiZjPkQ8+azOZLwqOT0eIC7ZWlHU4ITQUDKhVef0kVn1CC+3HDzs0q4GtiNL3Jd7hs0/r+dWVwLbZzzc9FnFsTu6nDMz75k6nNICE=");
        System.out.println("userId=" + token.getUserId());
        System.out.println("expiresAt=" + token.getExpiresAt());
    }
    
    @Test
    public void testUrlEncryptToken() throws UnsupportedEncodingException {
        String encryptedToken = "eneGKi4Soqsw+1gvL7DjNIYuW02B867KmXGwE51Ubd+jurflJqquNQjciZvEf01raCD3au231wmQaoIlSXtIY5GL7aHAF6ue2bYrrgLOIZYYOprbOTcP6mPgptJtCoiqKurReT1m6zlu22D/1LY8dIxk9+Nlqfvvd2kaDnTz8Y2QMKP7QkADXLrjRwhgAWYESsX8UQ7XSjMKBfjkk/Ct4lIIK+/vY8kJ8Jgf6JnTmtE5PK3bk/d919Z5oUUuVokCBk6Hpvm4vCcaIx+aAUgbE7AUOo175wUEeSDKPuVzWic=";
        String urlEncoded = URLEncoder.encode(encryptedToken, Constants.DEFAULT_CHARSET);
        System.out.println("encoded=" + urlEncoded);
    }
}
