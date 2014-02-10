/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.auth;

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
        Token token = tokenEncryptor.decrypt("eneGKi4Soqsw+1gvL7DjNIYuW02B867KmXGwE51Ubd+jurflJqquNQjciZvEf01raCD3au231wmQaoIlSXtIY5GL7aHAF6ue2bYrrgLOIZYYOprbOTcP6mPgptJtCoiqKurReT1m6zlu22D/1LY8dIxk9+Nlqfvvd2kaDnTz8Y3zD104g1knws/V1YQjsIuCpO/unyb5a7jn86KN1+Di7jh8Um5msCVOn+O2p/790sjaqX+uU8AxlZ637bxUgUsdXrTCKeJ81WMj4y1bcqbgwskkGVm/2vUrCAgMkV1Kg8Y=");
        System.out.println("userId=" + token.getUserId());
        System.out.println("expiresAt=" + token.getExpiresAt());
    }
}
