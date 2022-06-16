package com.example.demo.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECDSAUtil {

    public static final String KEY_ALGORITHM = "EC";
    public static final String ELLIPTIC_CURVE_STANDARD_NAME = "secp256r1";
    public static final String ECDSA_ALGORITHM = "SHA256withECDSA";

    public static KeyPair initKey() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(new ECGenParameterSpec(ELLIPTIC_CURVE_STANDARD_NAME)); //key长度设置
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static ECPublicKey transferToECPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = HexStringUtil.toBytes(publicKeyString);

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        return (ECPublicKey) publicKey;
    }

    public static ECPrivateKey transferToECPrivateKey(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = HexStringUtil.toBytes(privateKeyString);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        return (ECPrivateKey) privateKey;
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = initKey();
        ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        System.out.println(ecPublicKey.toString());
        System.out.println(ecPublicKey.getEncoded().length);
        System.out.println("Private S=" + ecPrivateKey.getS());
        System.out.println("Private length=" + ecPrivateKey.getEncoded().length);

        String publicKeyStr = HexStringUtil.toString(ecPublicKey.getEncoded());
        String privateKeyStr = HexStringUtil.toString(ecPrivateKey.getEncoded());

        System.out.println("Public=" + publicKeyStr);
        System.out.println("Private=" + privateKeyStr);

        ECPublicKey ecTPublicKey = transferToECPublicKey(publicKeyStr);
        ECPrivateKey ecTPrivateKey = transferToECPrivateKey(privateKeyStr);

        System.out.println(ecTPublicKey.toString());
        System.out.println(ecTPublicKey.getEncoded().length);
        System.out.println("Private S=" + ecTPrivateKey.getS());
        System.out.println("Private length=" + ecTPrivateKey.getEncoded().length);
    }
}
