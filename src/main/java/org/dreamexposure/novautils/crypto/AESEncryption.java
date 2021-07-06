package org.dreamexposure.novautils.crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class AESEncryption {
    private final IvParameterSpec ivParameterSpec;
    private final SecretKeySpec secretKeySpec;
    private final Cipher cipher;

    /**
     * @param key1 Secret key one
     * @param key2 Secret key two
     * @throws IllegalArgumentException if key one and/or key two are longer than 16 characters.
     * @throws NoSuchPaddingException   If the Cipher instance fails
     * @throws NoSuchAlgorithmException If the Cipher instance fails.
     */
    public AESEncryption(String key1, String key2) throws IllegalArgumentException, NoSuchPaddingException, NoSuchAlgorithmException {
        if (!(key1.length() == 16 && key2.length() == 16))
            throw new IllegalArgumentException("Keys must both be 16 characters each!");

        ivParameterSpec = new IvParameterSpec(key1.getBytes(StandardCharsets.UTF_8));
        secretKeySpec = new SecretKeySpec(key2.getBytes(StandardCharsets.UTF_8), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    /**
     * Encrypt the Data with the secret key.
     * **WARNING** Can only be decrypted by this class!!!
     *
     * @param data The data to encrypt.
     * @return The encrypted, unreadable data.
     * @throws Exception If something fails.
     */
    public String encrypt(String data) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Decrypt the Data with the secret key.
     * **WARNING** Can only be encrypted with this class!!!
     * **WARNING** Decrypting of data can be a security risk! Treat with care!!
     *
     * @param encryptedData The data to decrypt.
     * @return The data, decrypted.
     * @throws Exception If something fails.
     */
    public String decrypt(String encryptedData) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedData));
        return new String(decryptedBytes);
    }
}
