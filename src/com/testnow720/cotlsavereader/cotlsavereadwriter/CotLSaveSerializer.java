package com.testnow720.cotlsavereader.cotlsavereadwriter;

import com.testnow720.cotlsavereader.aesutil.AESUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CotLSaveSerializer {
	/**
	 * Reads and decodes the encoded save file.
	 * This will NOT work with the decrypted JSON file.
	 * If this fails, either because of the magic value mismatch, or other errors, it will be logged into the standard output.
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @param fileName The path of the file
	 * @return The decoded save file data in UTF-8
	 * */
	public String read(String fileName) {
		File saveFile = new File(fileName);
		if (!saveFile.exists()) {
			System.out.println("File " + fileName + " not found");
			return null;
		}
		
		if (!saveFile.canRead()) {
			System.out.println("File " + fileName + " doesn't have read permission");
			return null;
		}
		
		FileInputStream saveReader = null;
		try {
			saveReader = new FileInputStream(saveFile);
			byte[] magicByte = new byte[1];
			saveReader.read(magicByte, 0, 1);
			// This is 'E' in ASCII, The magic value in the code is '69'
			if (/*Character.toString(magicByte[0]).equals("E")*/magicByte[0] != 69) {
				System.out.println("File " + fileName + " doesn't have magic byte '69'");
				return null;
			}
			
			byte[] key = new byte[16];
			byte[] iv = new byte[16];
			
			saveReader.read(key, 0, 16); // Read key
			saveReader.read(iv, 0, 16); // Read IV
			
			byte[] content = new byte[saveReader.available()];
			saveReader.read(content, 0, saveReader.available()); // Read actual content
			
			System.out.println("Decoded AES Key : " + AESUtil.bytesToHex(key));
			System.out.println("Decoded AES IV : " + AESUtil.bytesToHex(iv));
			
			return new String(AESUtil.decode(content, key, iv), StandardCharsets.UTF_8);
		} catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Error reading file : " + e.getMessage());
		} finally {
			try {
				if (saveReader != null) {
					saveReader.close();
				}
			} catch (IOException e) {}
		}
		return null;
	}
	/**
	 * Writes the data in a JSON file.
	 * 
	 * @author testnow720
	 * @since 1.0
	 * @param fileName The path of the target file
	 * @param data The data to be encoded (This just calls .toString() on the object!)
	 * @param isEncrypted Whether or not to use AES encryption for the output file
	 * @return Status code of the save process
	 * */
	public boolean write(String fileName, Object data, boolean isEncrypted) {
		File save = new File(fileName);
		if (save.exists()) {
			System.out.println("File " + fileName + " already exists");
			return false;
		}
		
		FileOutputStream saveStream = null;
		FileWriter saveWriter = null;
		
		try {
			if (isEncrypted) {
				saveStream = new FileOutputStream(save); // We use FileOutputStream to write raw data
				
				byte[] key = AESUtil.generateKey();
				byte[] iv = AESUtil.generateIV();
				byte[] encodedData = AESUtil.encode(data.toString().getBytes(), key, iv);
				
				System.out.println("AES Key encode : " + AESUtil.bytesToHex(key));
				System.out.println("AES IV encode : " + AESUtil.bytesToHex(iv));
				
				saveStream.write(69); // Magic value
				saveStream.write(key);
				saveStream.write(iv);
				saveStream.write(encodedData);
				
				saveStream.flush();
			} else {
				saveWriter = new FileWriter(save);
				
				saveWriter.write(data.toString());
				
				saveWriter.flush();
			}
			return true;
		} catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Error writing to file : " + e.getMessage());
		} finally {
			try {
				if (saveStream != null) {
					saveStream.close();
				}
				if (saveWriter != null) {
					saveWriter.close();
				}
			} catch (IOException e) {}
		}
		return false;
	}
}
