package com.dzq.coin;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.coin
 * @Description : TODO 钱包 使用Elliptic-curve加密来生成KeyPair
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午6:13
 * @ModificationHistory Who When What
 */
public class Wallet {

    public PrivateKey privateKey; //私钥

    public PublicKey publicKey;   //公钥

    public Wallet() {
        generateKeyPair();
    }

    /**
     * 生成私钥和公钥
     */
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
