package com.example.synradar_sectudo.helper;


import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class DBUtils {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void generateDBKey()  {
        String alias= "DBKey";
        final KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();
            keyGenerator.init(keyGenParameterSpec);
            final SecretKey secretKey = keyGenerator.generateKey();
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyStore.SecretKeyEntry skEntry =
                    new KeyStore.SecretKeyEntry(secretKey);
            keyStore.setEntry(alias, skEntry, null);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    public static String getDBKey() {
        String alias = "DBKey";
        String op = "";
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore
                    .getEntry(alias, null);
            final SecretKey secretKey = secretKeyEntry.getSecretKey();
             op = secretKey.getEncoded().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return op;
    }
}

