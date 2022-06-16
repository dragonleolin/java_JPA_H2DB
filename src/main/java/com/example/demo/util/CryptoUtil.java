package com.example.demo.util;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtil {

    public static KeyPair generateKeyPair(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator= KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keyLength);
        return keyPairGenerator.generateKeyPair();
    }

    public static String rsaEncryptByPublicKey(String content, String  publicKeyStr) throws Exception {
        byte[] contentBytes = content.getBytes();
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher=Cipher.getInstance("RSA");    //java默認"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return HexStringUtil.toString(cipher.doFinal(contentBytes));
    }

    public static String rsaDecryptByPrivateKey(String content, String privateKeyStr) throws Exception {
        byte[] contentBytes = HexStringUtil.toBytes(content);
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(contentBytes));
    }

    private static PublicKey getPublicKey(String keyStr) throws Exception {
        byte[] publicBytes = HexStringUtil.toBytes(keyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        return pubKey;
    }

    private static PrivateKey getPrivateKey(String keyStr) throws Exception {
        byte[] privateBytes = HexStringUtil.toBytes(keyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(keySpec);

        return priKey;
    }

    public static void main(String[] args) throws Exception {
        String input = "1";

        KeyPair keyPair = generateKeyPair(1024);

        PublicKey publicKey = keyPair.getPublic();
        System.out.println("Public key：" + HexStringUtil.toString(publicKey.getEncoded()));

        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Private key：" + HexStringUtil.toString((privateKey.getEncoded())));

        String encrypted = rsaEncryptByPublicKey(input, "30819F300D06092A864886F70D010101050003818D0030818902818100E500EF0E91395424D4F21542F7AA922CE49CBD553FAD1712FA9A2F4432CCAC3E6B11852FA8A9CCF18DA8371B00C4CACEE74CBE243860043CD9B92EEE85D63F030A4DBA38477BB6F0FD487B03D15978719DD605419304BBF0E3D773687405FD61F716141FA8015DF6D50BADD38255C5A8B6CA5089520B3F2940C5DC75A9B088110203010001");
        System.out.println("Encrypted: " + encrypted);

        String decrypted = rsaDecryptByPrivateKey(encrypted, "30820277020100300D06092A864886F70D0101010500048202613082025D02010002818100E500EF0E91395424D4F21542F7AA922CE49CBD553FAD1712FA9A2F4432CCAC3E6B11852FA8A9CCF18DA8371B00C4CACEE74CBE243860043CD9B92EEE85D63F030A4DBA38477BB6F0FD487B03D15978719DD605419304BBF0E3D773687405FD61F716141FA8015DF6D50BADD38255C5A8B6CA5089520B3F2940C5DC75A9B0881102030100010281804287E2EBF8850F4D2410B0DCA3D78A60408AC8F6DB690E2B15637617FA2A3C6065FAD116E7896D6140267EDBF97D32EEA984423983A76193CC2D60D52B4CF34102253AAD6BCE353DF843BE9946C1BBA74F0CC4C87AAF14FCCFA9AC708F332E0E1AC75442D2241F187879E0EBF62AE39603BDE79CC37AD48A601F8A35B8F39A01024100F73DCE4B25C8CF09FE089A58EB29B26D565564BF725800B8F3E674AD3BF25C12C81F4144CA051532FD9762791F2E495794D985EF0A8F63C00785591C0DCB46F5024100ED1DBB7CBFA8FEF8E2D46FFB48C80CE301881AD82F6AB129AD2A455EC60274F9DFF0991090043A2EA84F61A5CD1E88C836C14F9F4B09F2931C4AB3409CA1732D024023A0C06D8BD05825747A00E3F75F56DAF2A9F38EB307837323927E15C8A1B02AF4B3AC50215C82258AED99BF228325067DE8AAA83BB956028212F1385DB48151024100DAD49CCA758B7EBCAD8287526E8A09B29B20443E0E0CB632135936AA4FADC6CD310A38091C107AADC2B17819EF62E5B3792D10D19F0C302DEA675A651FE8B485024100CD0766168D5F44ECB7A0EF06B8FEDF645324DEF391B7B19343E96E794C607BF99A480A36F885954FD5DD8AC3364665A58E4E7273CDB145B79D63F86F1B0313A7");
        System.out.println("Decrypted: " + decrypted);
    }
}