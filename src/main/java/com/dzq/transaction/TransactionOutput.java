package com.dzq.transaction;

import com.dzq.utils.DigitalSignatureUtil;

import java.security.PublicKey;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.transaction
 * @Description : TODO 交易输出，用于下一次的交易输入
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午7:21
 * @ModificationHistory Who When What
 */
public class TransactionOutput {
    public String id;
    public PublicKey reciepient; //此次交易接收的公钥.
    public float value; //交易的币数额
    public String parentTransactionId; //此输出创建的交易的ID

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = DigitalSignatureUtil.applySha256(DigitalSignatureUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
    }

    //检查改交易是否属于你的
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }
}
