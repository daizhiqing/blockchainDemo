package com.dzq.coin;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.coin
 * @Description : TODO 钱包 使用Elliptic-curve加密来生成KeyPair
 * 区块链钱包不会有余额总数在数值在钱包中，
 * 钱包余额其实是所有发送给你的未使用的交易输出的总和。ps：这里略微有点绕，总之就记住进账和出账这回事情。余额=总进账-总出账
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
