package com.dzq.coin;

import com.dzq.BlockchainApp;
import com.dzq.transaction.Transaction;
import com.dzq.transaction.TransactionInput;
import com.dzq.transaction.TransactionOutput;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public HashMap<String,TransactionOutput> mineUTXOs = new HashMap<String,TransactionOutput>();


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

    /**
     * 计算余额
     * @return
     */
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: BlockchainApp.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                mineUTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
                total += UTXO.value ;
            }
        }
        return total;
    }

    /**
     * 发起一笔资金转移
     * @param _recipient
     * @param value
     * @return
     */
    public Transaction sendFunds(PublicKey _recipient, float value ) {
        if(getBalance() < value) {
            System.out.println("当前余额不足，无法发起此笔交易");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: mineUTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            mineUTXOs.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}
