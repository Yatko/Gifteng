/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.auth;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used for message signature generation.
 * 
 * @author gyuszi
 */
public class MessageEncryptor {
    
    private static final Log logger = LogFactory.getLog(MessageEncryptor.class);
    
    private static final String TRANSFORMATION = "AES/CFB8/NoPadding";
    private static final String SECRETKEY_ALGORITHM = "AES";
    private static final String MESSAGEDIGEST_ALGORITHM = "MD5";
    private static final String ENCODING = "UTF8";
    
    private static final String INITIAL_VECTOR = "----venefica----";
    private static final String SECRET_KEY = "veneficalabs";
    
    private IvParameterSpec initialVector = new IvParameterSpec(INITIAL_VECTOR.getBytes());
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;
    
    public MessageEncryptor() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this(SECRET_KEY);
    }
    
    public MessageEncryptor(String secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] secretKeyBytes = md5(secretKey).getBytes();
        secretKeySpec = new SecretKeySpec(secretKeyBytes, SECRETKEY_ALGORITHM);
        cipher = Cipher.getInstance(TRANSFORMATION);
    }
    
    /**
     * Creates a message signature entry in the SOAP header.
     * 
     * @param context
     */
    public String generateMessageSignature(String signature) throws UnsupportedEncodingException {
        /**
        //TODO: needs signature generation
        
        String signature = getRequestMessageSignature(context);
        SOAPMessage message = context.getMessage();
        SOAPBody body = message.getSOAPBody();

        Node webMethodNode = body.getFirstChild();
        String methodName = webMethodNode.getNodeName();
        /**/
        String encryptedSignature = encrypt(signature);
        encryptedSignature = soapEncrypt(encryptedSignature);
        
        logger.debug("The generated decrypted signature: " + signature);
        logger.debug("The generated encrypted signature: " + encryptedSignature);
        
        return encryptedSignature;
    }
    
//    /**
//     * Extracts the request message signature value from the SOAP headers.
//     * 
//     * @param context
//     */
//    public void verifyMessageSignature(String signature) throws UnsupportedEncodingException {
//        logger.debug("The received signature: " + signature);
//        signature = soapDecrypt(signature);
//        
//        String decryptedSignature = decrypt(signature);
//        //TODO: needs signature verification
//        
//        logger.debug("The received decrypted signature: " + decryptedSignature);
//    }
    
    private static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(MESSAGEDIGEST_ALGORITHM);
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        return String.format("%032x", number);
    }
    
    private String soapEncrypt(String encryptedData) throws UnsupportedEncodingException {
        return Base64.encodeBase64String(encryptedData.getBytes(ENCODING));
    }
    
//    private String soapDecrypt(String encryptedData) throws UnsupportedEncodingException {
//        return new String(Base64.decodeBase64(encryptedData), ENCODING);
//    }
    
//    private String decrypt(String encryptedData) {
//        String decryptedData = null;
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initialVector);
//            byte[] encryptedByteArray = Base64.decodeBase64(encryptedData.getBytes());
//            byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
//            decryptedData = new String(decryptedByteArray, ENCODING);
//        } catch (Exception e) {
//            logger.error("Problem decrypting the data", e);
//        }
//        return decryptedData;
//    }
    
    private String encrypt(String decryptedData) {
        String encryptedData = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initialVector);
            byte[] decryptedByteArray = Base64.encodeBase64(decryptedData.getBytes());
            byte[] encryptedByteArray = cipher.doFinal(decryptedByteArray);
            encryptedData = new String(encryptedByteArray, ENCODING);
        } catch (Exception e) {
            logger.error("Problem encrypting the data", e);
        }
        return encryptedData;
    }
}
