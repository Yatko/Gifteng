package com.venefica.auth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Encrypts the token using the secret key.
 *
 * @author Sviatoslav Grebenchukov
 */
public class TokenEncryptor {

    public static final String ALGORITHM = "AES";
    public static final String TRANSFORMATION = ALGORITHM + "/CBC/PKCS5Padding";
    
    private final Log log = LogFactory.getLog(TokenEncryptor.class);
    
    // @formatter:off
    private SecretKeySpec secretKeySpec;
    // FIXME: Create proper iv vector
    private IvParameterSpec ivParamSpec = new IvParameterSpec(
            new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    private Cipher cipher;
    // @formatter:on

    public TokenEncryptor(String secretKey) throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (secretKey == null) {
            throw new NullPointerException("secretKey is null!");
        }

        byte[] secretKeyBytes = Base64.decodeBase64(secretKey);
        secretKeySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);

        // create and init cipher
        cipher = Cipher.getInstance(TRANSFORMATION);
    }

    public String generate() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
        generator.init(128);
        SecretKey sk = generator.generateKey();
        return Base64.encodeBase64String(sk.getEncoded());
    }

    public String encrypt(Token token) throws TokenEncryptionException {
        if (token == null) {
            throw new NullPointerException("token is null!");
        }

        try {
            // serialize token using standard java binary serialization algorithm
            ByteArrayOutputStream memOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(memOutputStream);

            out.writeObject(token);
            out.close();

            byte[] serializedToken = memOutputStream.toByteArray();

            // prepare cipher for encryption
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParamSpec);
            // encrypt token
            byte[] encryptedTokenBytes = cipher.doFinal(serializedToken);

            return Base64.encodeBase64String(encryptedTokenBytes);
        } catch (Exception e) {
            log.error("Unable to encrypt token!", e);
            throw new TokenEncryptionException(e);
        }
    }

    public Token decrypt(String encryptedToken) throws TokenDecryptionException {
        if (encryptedToken == null) {
            throw new NullPointerException("encryptedToken is null!");
        }

        byte[] encryptedTokenBytes = Base64.decodeBase64(encryptedToken);

        try {
            // prepare cipher for decryption
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParamSpec);
            // decrypt token
            byte[] decryptedTokenBytes = cipher.doFinal(encryptedTokenBytes);

            // deserialize token
            ByteArrayInputStream memInputStream = new ByteArrayInputStream(decryptedTokenBytes);
            ObjectInputStream in = new ObjectInputStream(memInputStream);

            Token token = (Token) in.readObject();
            in.close();

            return token;
        } catch (Exception e) {
            log.error("Unable to decrypt token!", e);
            throw new TokenDecryptionException(e);
        }
    }
}
