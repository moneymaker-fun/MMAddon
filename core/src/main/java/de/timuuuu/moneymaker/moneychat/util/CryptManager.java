package de.timuuuu.moneymaker.moneychat.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class CryptManager {

  public static SecretKey createNewSharedKey() {
    try {
      KeyGenerator key = KeyGenerator.getInstance("AES");
      key.init(128);
      return key.generateKey();
    } catch (NoSuchAlgorithmException var1) {
      throw new Error(var1);
    }
  }

  public static KeyPair createNewKeyPair() {
    try {
      KeyPairGenerator keyPair = KeyPairGenerator.getInstance("RSA");
      keyPair.initialize(1024);
      return keyPair.generateKeyPair();
    } catch (NoSuchAlgorithmException var1) {
      var1.printStackTrace();
      return null;
    }
  }

  public static String getServerIdHash(String baseServerId, PublicKey publicKey, SecretKey secretKey) throws Exception {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    messageDigest.update(baseServerId.getBytes("ISO_8859_1"));
    messageDigest.update(secretKey.getEncoded());
    messageDigest.update(publicKey.getEncoded());
    byte[] digestData = messageDigest.digest();
    return new BigInteger(digestData).toString(16);
  }

  private static byte[] digestOperation(byte[]... bytes) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");

      for(byte[] b : bytes) {
        digest.update(b);
      }

      return digest.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static PublicKey decodePublicKey(byte[] p_75896_0_) throws NoSuchAlgorithmException, InvalidKeySpecException {
    X509EncodedKeySpec var1 = new X509EncodedKeySpec(p_75896_0_);
    KeyFactory var2 = KeyFactory.getInstance("RSA");
    return var2.generatePublic(var1);
  }

  public static SecretKey decryptSharedKey(PrivateKey p_75887_0_, byte[] p_75887_1_) {
    return new SecretKeySpec(decryptData(p_75887_0_, p_75887_1_), "AES");
  }

  public static byte[] encryptData(Key p_75894_0_, byte[] p_75894_1_) {
    return cipherOperation(1, p_75894_0_, p_75894_1_);
  }

  public static byte[] decryptData(Key p_75889_0_, byte[] p_75889_1_) {
    return cipherOperation(2, p_75889_0_, p_75889_1_);
  }

  private static byte[] cipherOperation(int direction, Key key, byte[] payload) {
    try {
      return createTheCipherInstance(direction, key.getAlgorithm(), key).doFinal(payload);
    } catch (BadPaddingException | IllegalBlockSizeException var4) {
      ((GeneralSecurityException)var4).printStackTrace();
      return null;
    }
  }

  private static Cipher createTheCipherInstance(int direction, String type, Key key) {
    try {
      Cipher cipher = Cipher.getInstance(type);
      cipher.init(direction, key);
      return cipher;
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException var4) {
      ((GeneralSecurityException)var4).printStackTrace();
      return null;
    }
  }

  public static Cipher createNetCipherInstance(int opMode, Key key) {
    try {
      Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
      cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
      return cipher;
    } catch (GeneralSecurityException generalsecurityexception) {
      throw new RuntimeException(generalsecurityexception);
    }
  }

}
