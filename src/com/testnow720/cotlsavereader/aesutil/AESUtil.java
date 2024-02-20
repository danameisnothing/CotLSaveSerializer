package com.testnow720.cotlsavereader.aesutil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.lang.StringBuilder;

public class AESUtil {
	/**
	 * Converts an array of bytes to it's hex form
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @param bytes The array of bytes
	 * @return The hex form in String
	 * */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder hexStr = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
            	hexStr.append('0');
            }
            hexStr.append(hex);
        }
        return hexStr.toString();
	}
	
	/**
	 * Generates a random IV
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @return The IV in an array of bytes
	 * */
	public static byte[] generateIV() {
		SecureRandom sr = new SecureRandom();
		byte[] iv = new byte[16];
		sr.nextBytes(iv);
		return iv;
	}
	
	/**
	 * Generates a random key
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @return The key in an array of bytes
	 * */
	public static byte[] generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecretKey key = keyGen.generateKey();
		return key.getEncoded();
	}
	
	/**
	 * Decrypts the bytes using the AES encryption algorithm
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @param bytes The data to be decoded
	 * @param key The AES key
	 * @param iv The AES IV
	 * @return The decoded data
	 * */
	public static byte[] decode(byte[] bytes, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
		return aes.doFinal(bytes);
	}
	
	/**
	 * Encrypts the bytes using the AES encryption algorithm
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @param bytes The data to be encrypted
	 * @param key The AES key
	 * @param iv The AES IV
	 * @return The encrypted data
	 * */
	public static byte[] encode(byte[] bytes, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
		return aes.doFinal(bytes);
	}
}
