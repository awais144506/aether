package com.ocp.aether.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityUtility {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

//    public static void main(String[] args) {
//        try {
//            String originalToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.p7dVo7yegEYp1Y39aPmzM6xIXUzCT3H2g2BwcNbBoh0";
//
//            byte[] masterKey = AppConfig.getMasterKey();
//
//            String encrypted = encrypt(originalToken, masterKey);
//            System.out.println("Encrypted (Base64): " + encrypted);
//
//            String decrypted = decrypt(encrypted, masterKey);
//            System.out.println("Decrypted Result: " + decrypted);
//
//            if (originalToken.equals(decrypted)) {
//                System.out.println("SUCCESS: The token matches the original!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String encrypt(String plainText, byte[] key) throws Exception {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        byte[] packedMessage = ByteBuffer.allocate(iv.length + cipherText.length)
                .put(iv)
                .put(cipherText)
                .array();

        return Base64.getEncoder().encodeToString(packedMessage);
    }

    public static String decrypt(String base64PackedMessage, byte[] key) throws Exception {
        byte[] packedMessage = Base64.getDecoder().decode(base64PackedMessage);
        ByteBuffer byteBuffer = ByteBuffer.wrap(packedMessage);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }

//    public static byte[] generateAES256Key() throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(256); // 256 bits = 32 bytes
//        SecretKey secretKey = keyGen.generateKey();
//        return secretKey.getEncoded();
//    }

}
