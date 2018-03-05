package com.dzq.transaction;

import com.dzq.utils.DigitalSignatureUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.transaction
 * @Description : TODO 每笔交易将会携带如下数据：
    1、资金发送方的公钥（地址）。
    2、资金接收方的公钥（地址）。
    3、要转移的资金金额。
    4、输入（Inputs）。这个输入是对以前交易的引用，这些交易证明发件人拥有要发送的资金。
    5、输出（Outputs），显示交易中收到的相关地址量。（这些输出作为新交易中的输入引用）
    6、一个加密签名。证明地址的所有者是发起该交易的人，并且数据没有被更改。（例如：防止第三方更改发送的金额）
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午7:18
 * @ModificationHistory Who When What
 */
public class Transaction {

    public String transactionId;  //一笔交易的hash值
    public PublicKey sender;      //资金发送方的公钥地址
    public PublicKey reciepient;  //资金接收方的公钥地址
    public float value;          //交易的金额
    public long timeStamp;       //时间戳
    public byte[] signature;    //交易签名。防止被篡改

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; //交易记录次数


    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {

        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
        this.timeStamp = System.currentTimeMillis();

        transactionId = calulateHash();

    }

    /**
     * 计算一笔交易的hash
     * @return
     */
    private String calulateHash() {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return DigitalSignatureUtil.applySha256(
                DigitalSignatureUtil.getStringFromKey(sender) +
                        DigitalSignatureUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }

    /**
     * 根据私钥来生成交易的数据签名
     * @param privateKey
     */
    public void generateSignature(PrivateKey privateKey) {
        String data = DigitalSignatureUtil.getStringFromKey(sender)
                + DigitalSignatureUtil.getStringFromKey(reciepient)
                + Float.toString(value)
                + Long.toString(timeStamp)
                + Integer.toString(sequence)   ;
        signature = DigitalSignatureUtil.applyECDSASig(privateKey,data);
    }

    /**
     * 签署一些基本信息的签名，真正的区块链应用肯定不止这些，
     * 比如使用的输出（outputs）/输入（inputs）现在我们只签署了最基本的。
     * 将新的交易添加到块中时，矿工将对签名进行验证。
     * @return
     */
    public boolean verifySignature() {
        String data = DigitalSignatureUtil.getStringFromKey(sender)
                + DigitalSignatureUtil.getStringFromKey(reciepient)
                + Float.toString(value)
                + Long.toString(timeStamp)
                + Integer.toString(sequence)    ;
        return DigitalSignatureUtil.verifyECDSASig(sender, data, signature);
    }
}
