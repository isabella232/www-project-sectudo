package com.sr.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

	public static final String PRIVATE_KEY_FILE = "/opt/tomcat/keys/private.key";
	public static final String PUBLIC_KEY_FILE = "/opt/tomcat/keys/public.key";
	
	//public static final String PRIVATE_KEY_FILE = "H:/Keys/sk/type3/private.key";
	//public static final String PUBLIC_KEY_FILE = "H:/Keys/sk/type3/public.key";

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

	public static String symEncryp(String plainText, String key) {
		String cipherStr = "";
		try {
			byte[] plaintTextByteArray = plainText.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] cipherbytes = cipher.doFinal(plaintTextByteArray);
			Base64.Encoder en = Base64.getEncoder();
			cipherStr = en.encodeToString(cipherbytes);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return cipherStr;
	}

	public static String symDecrypt(String cipherText, String key) {
		String plaintext = "";

		Base64.Decoder de = Base64.getDecoder();
		byte[] cipherbytes = de.decode(cipherText);

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

	public static void generateKey() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance("RSA");
			keyGen.initialize(1024);

			final KeyPair keyPair = keyGen.generateKeyPair();

			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_FILE);

			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			ObjectOutputStream publicKeyOS = new ObjectOutputStream(
					new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(keyPair.getPublic());
			
			PublicKey pub = keyPair.getPublic();
			PrivateKey priv = keyPair.getPrivate();
			
			byte[] pubkeybytes = pub.getEncoded();
			FileOutputStream pubkeyfos = new FileOutputStream(PUBLIC_KEY_FILE);
			pubkeyfos.write(pubkeybytes);
			pubkeyfos.close();
			
			
			byte[] privkeybytes = priv.getEncoded();
			FileOutputStream privkeyfos = new FileOutputStream(PRIVATE_KEY_FILE);
			privkeyfos.write(privkeybytes);
			privkeyfos.close();
			
		/*	
			
			publicKeyOS.close();

			ObjectOutputStream privateKeyOS = new ObjectOutputStream(
					new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(keyPair.getPrivate());
			privateKeyOS.close();
			
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String decode(String ipstr) {
		// TODO Auto-generated method stub

		String decodedStr = "";
		Base64.Decoder de = Base64.getDecoder();
		byte[] op = de.decode(ipstr.getBytes());
		decodedStr = new String(op);

		return decodedStr;
	}

	public static String encode(String ipstr) {
		// TODO Auto-generated method stub

		String encodedStr = "";
		Base64.Encoder en = Base64.getEncoder();
		encodedStr = en.encodeToString(ipstr.getBytes());
		return encodedStr;
	}

	public static String decryptAsym(String encryptedStr) throws Exception {
		// TODO Auto-generated method stub


		Base64.Decoder de = Base64.getDecoder();
		
		byte[] enprivatekeybytes = de.decode(Constants.privatekeyStr.getBytes());
		PKCS8EncodedKeySpec privKeyspec = new PKCS8EncodedKeySpec(enprivatekeybytes);
		PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(privKeyspec);

		
		byte[] encryptedbytes = de.decode(encryptedStr);

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
		
		  		
		Base64.Decoder de = Base64.getDecoder();
		byte[] enpubkeybytes = de.decode(Constants.publickeyStr.getBytes());
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(enpubkeybytes));


		byte[] cipherText = null;
		String encryptedText = "";
		try {

			final Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherText = cipher.doFinal(text.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Base64.Encoder en = Base64.getEncoder();
		encryptedText = en.encodeToString(cipherText);
		return encryptedText;
	}

	public static void main(String[] args) {
		try {
			//generateKey();
			String originalText = "{\"username\":\"adam\", \"password\":\"adam123\", \"token\":\"12345678999\"}";

			String cipherText = AsymEncrypt(originalText);

			String plainText = decryptAsym(cipherText);

			System.out.println("Original Data: " + originalText);
			System.out.println("Encrypted Data: " + cipherText);
			System.out.println("Decrypted Data: " + plainText);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
