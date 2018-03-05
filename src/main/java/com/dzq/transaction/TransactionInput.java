package com.dzq.transaction;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.transaction
 * @Description : TODO 定义一个交易输入，主要是对以前交易输出的引用
 * 这个类将用于引用尚未使用的TransactionOutputs的值。
 * transactionOutputId将用于查找相关的TransactionOutput，
 * 从而允许矿工检查你的所有权
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午7:20
 * @ModificationHistory Who When What
 */
public class TransactionInput {
    public String transactionOutputId; //关联上一个交易输出的引用
    public TransactionOutput UTXO; //调用未使用的交易输出

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
