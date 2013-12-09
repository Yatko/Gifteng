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
    
}
