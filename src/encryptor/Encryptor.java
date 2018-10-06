package encryptor;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    // Used for encryption
    public static String encrypt(String plaintext, String password) {
        try {
            // Generate an AES key from the sha256 hash of the password
            SecretKeySpec keySpec = new SecretKeySpec(sha256(password), "AES");

            // Create an AES CBC cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");

            // Generate a random initialization vector
            SecureRandom randomSecureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] iv = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv);
            GCMParameterSpec ivParams = new GCMParameterSpec(128, iv);

            // Initialize the cipher in encrypt mode using the given key and IV
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParams);

            // Return the IV and the encrypted text
            return Base64.getEncoder().encodeToString(iv)
                    + Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Used for decryption
    public static String decrypt(String ciphertext, String password) {
        try {
            // Generate an AES key from the sha256 hash of the password
            SecretKeySpec keySpec = new SecretKeySpec(sha256(password), "AES");

            // Create an AES CBC cipher
            Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");

            // Separate the ciphertext from the IV (IV should end in two equal signs)
            String iv = ciphertext.split("==")[0] + "==";
            String toDecrypt = ciphertext.substring(iv.length());

            // Generate the IV params object from the IV string
            GCMParameterSpec ivParams = new GCMParameterSpec(128, Base64.getDecoder().decode(iv));

            // Initialize the cipher in decrypt mode using the given key and IV
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParams);

            // Return the decrypted text
            return new String(cipher.doFinal(Base64.getDecoder().decode(toDecrypt)), "utf-8");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generates hash from password using SHA256
    public static byte[] sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
