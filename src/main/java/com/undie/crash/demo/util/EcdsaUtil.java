package com.undie.crash.demo.util;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/1 23:46
 **/
public class EcdsaUtil {
    private static final String SIGNALGORITHMS = "SHA256withECDSA";
    private static final String ALGORITHM = "EC";
    private static final String SECP256K1 = "secp256k1";

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
//            KeyPair keyPair = getKeyPair();
//            String privateKey = keyPair.getPrivate().toString();
//            String publicKey = keyPair.getPublic().toString();
//            System.err.println("pri=" + HexUtil.encodeHexString(keyPair.getPrivate().getEncoded()) + ", pub=" + HexUtil.encodeHexString(keyPair.getPublic().getEncoded()));

//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
//            keyPairGenerator.initialize(256);
//            KeyPair keyPair = keyPairGenerator.generateKeyPair() ;
//            ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic() ;
//            ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate() ;


            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            KeyPair keyPair = keyPairGenerator.generateKeyPair() ;
            ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic() ;
            ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate() ;
            System.err.println("privateKey:" + HexUtil.encodeHexString(ecPrivateKey.getEncoded()) + ",\n publicKey:" + HexUtil.encodeHexString(ecPublicKey.getEncoded()));


        }
//        if (1 == 1) return;

//        生成公钥私钥
        KeyPair keyPair1 = getKeyPair();
        PublicKey publicKey1 = keyPair1.getPublic();
        PrivateKey privateKey1 = keyPair1.getPrivate();
        //密钥转16进制字符串
        String publicKey = HexUtil.encodeHexString(publicKey1.getEncoded());
        String privateKey = HexUtil.encodeHexString(privateKey1.getEncoded());
        System.out.println("生成公钥："+publicKey);
        System.out.println("生成私钥："+privateKey);
        //16进制字符串转密钥对象
        PrivateKey privateKey2 = getPrivateKey(privateKey);
        PublicKey publicKey2 = getPublicKey(publicKey);
        //加签验签
        String data="需要签名的数据";
        String signECDSA = signECDSA(privateKey2, data);
        boolean verifyECDSA = verifyECDSA(publicKey2, signECDSA, data);
        System.out.println("验签结果："+verifyECDSA);

    }

    /**
     * 加签
     * @param privateKey 私钥
     * @param data 数据
     * @return
     */
    public static String signECDSA(PrivateKey privateKey, String data) {
        String result = "";
        try {
            //执行签名
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] sign = signature.sign();
            return HexUtil.encodeHexString(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验签
     * @param publicKey 公钥
     * @param signed 签名
     * @param data 数据
     * @return
     */
    public static boolean verifyECDSA(PublicKey publicKey, String signed, String data) {
        try {
            //验证签名
            Signature signature = Signature.getInstance(SIGNALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] hex = HexUtil.decode(signed);
            boolean bool = signature.verify(hex);
            // System.out.println("验证：" + bool);
            return bool;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从string转private key
     * @param key 私钥的字符串
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] bytes = DatatypeConverter.parseHexBinary(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从string转publicKey
     * @param key 公钥的字符串
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {

        byte[] bytes = DatatypeConverter.parseHexBinary(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }




    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception {

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SECP256K1);
        KeyPairGenerator kf = KeyPairGenerator.getInstance(ALGORITHM);
        kf.initialize(ecSpec, new SecureRandom());
        KeyPair keyPair = kf.generateKeyPair();
        return keyPair;
    }

    public static void checkAll() {
        //0. 随机字符串
        String src = UUID.randomUUID().toString();
        System.err.println("source: " + src);

        // 1.初始化密钥
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            KeyPair keyPair = keyPairGenerator.generateKeyPair() ;
            ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic() ;
            ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate() ;
            // 执行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("EC") ;
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec) ;
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte []arr = signature.sign();
//            System.out.println("jdk ecdsa sign :"+ HexBin.encode(arr));
            // 验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("SHA1withECDSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean bool = signature.verify(arr);
            System.out.println("jdk ecdsa verify:"+bool);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
