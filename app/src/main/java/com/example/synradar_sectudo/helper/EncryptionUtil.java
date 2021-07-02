package com.example.synradar_sectudo.helper;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;


import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

	public static final String PRIVATE_KEY_FILE = "";
	public static final String PUBLIC_KEY_FILE = "";


	public static void main(String[] args) {
		String test = "This is to be encrypted";

		SecureRandom sr;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] b = new byte[16];
			sr.nextBytes(b);
			String key = new String(b);
			System.out.println(key);


			System.out.println(EncryptionUtil.symEncryp(test, key));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getRandomToken() {
		int n = 16;


		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";


		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {


			int index = (int) (AlphaNumericString.length() * Math.random());


			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}


	public static String getSecureToken() throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub

		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] b = new byte[16];
		sr.nextBytes(b);

		String token = new String(b);
		return token;
	}

	public static String generateMessageDigest(String input) {

		String messageDigestValue = "";
		try {
			byte[] inputData = input.getBytes();
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			digest.update(inputData, 0, input.length());

			byte[] generatedDigest = digest.digest();

			StringBuilder sb = new StringBuilder();
			for (byte b : generatedDigest) {
				sb.append(String.format("%02x", b & 0xff));
			}

			messageDigestValue = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return messageDigestValue;

	}


	public static String symEncrypwithKey(String plainText, SecretKey key) {
		String cipherStr = "";
		try {
			byte[] plaintTextByteArray = plainText.getBytes();

			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherbytes = cipher.doFinal(plaintTextByteArray);

			cipherStr = android.util.Base64.encodeToString(cipherbytes, android.util.Base64.NO_WRAP);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return cipherStr;
	}





	public static String symEncryp(String plainText, String key) {
		String cipherStr = "";
		try {
			byte[] plaintTextByteArray = plainText.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] cipherbytes = cipher.doFinal(plaintTextByteArray);

			cipherStr = android.util.Base64.encodeToString(cipherbytes, android.util.Base64.NO_WRAP);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return cipherStr;
	}


	public static String symDecrypt(String cipherText, String key) {
		String plaintext = "";



		byte[] cipherbytes = android.util.Base64.decode(cipherText, android.util.Base64.NO_WRAP);

		try {

			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] original = cipher.doFinal(cipherbytes);

			plaintext = new String(original);
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		return plaintext;
	}


	public static String decode(String ipstr) {
		// TODO Auto-generated method stub

		String decodedStr = "";

		byte[] op = android.util.Base64.decode(ipstr.getBytes() , android.util.Base64.NO_WRAP);
		decodedStr = new String(op);

		return decodedStr;
	}

	public static String encode(String ipstr) {
		// TODO Auto-generated method stub

		String encodedStr = "";

		encodedStr = android.util.Base64.encodeToString(ipstr.getBytes(), android.util.Base64.NO_WRAP);
		return encodedStr;
	}

	public static String decryptAsym(String encryptedStr) throws Exception {
		// TODO Auto-generated method stub




		byte[] enprivatekeybytes = Base64.decode(Constants.privatekeyStr.getBytes(), Base64.NO_WRAP);
		PKCS8EncodedKeySpec privKeyspec = new PKCS8EncodedKeySpec(enprivatekeybytes);
		PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(privKeyspec);


		byte[] encryptedbytes = Base64.decode(encryptedStr, Base64.NO_WRAP);

		byte[] dectyptedbytes = null;
		String decryptedStr = "";
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			dectyptedbytes = cipher.doFinal(encryptedbytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		decryptedStr = new String(dectyptedbytes);
		return decryptedStr;
	}

	public static String AsymEncrypt(String text) throws Exception {


		byte[] enpubkeybytes = Base64.decode(Constants.publickeyStr.getBytes(), Base64.NO_WRAP);
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(enpubkeybytes));


		byte[] cipherText = null;
		String encryptedText = "";
		try {

			final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherText = cipher.doFinal(text.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}


		encryptedText = Base64.encodeToString(cipherText, Base64.NO_WRAP);
		return encryptedText;
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public static String getDBKey() {
		String op = "pass";
		String AndroidKeyStore = "AndroidKeyStore";
		String AES_MODE = "AES/GCM/NoPadding";
		try {
			KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
			keyStore.load(null);
			if(keyStore.containsAlias("key1")) {
				SecretKey storedkey = (SecretKey) keyStore.getKey("key1", null);
				op = storedkey.toString();
		} else{
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", AndroidKeyStore);
			keyGenerator.init(
					new KeyGenParameterSpec.Builder("key1",
							KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
							.setBlockModes(KeyProperties.BLOCK_MODE_GCM)                   .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
							.setRandomizedEncryptionRequired(false)
							.build());
			keyGenerator.generateKey();
			SecretKey storedkey2 = (SecretKey) keyStore.getKey("key1", null);

				op = storedkey2.toString();
		}
	} catch (Exception e) {
			e.printStackTrace();
		}
		return op;
	}


		}
