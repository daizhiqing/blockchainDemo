package com.dzq.chainblock;

import com.dzq.transaction.Transaction;
import com.dzq.utils.DigitalSignatureUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.chainblock
 * @Description : TODO 定义一个区块链中的块
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午4:22
 * @ModificationHistory Who When What
 */
public class Block {
    public String hash; //数据块hash值
    public String previousHash; //前一个数据块哈希值
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); //our data will be a simple message.

    private long timeStamp; //时间戳

    private int nonce;//随机数

    public String merkleRoot; //默克尔根的值


    //构造函数，由前一个区块后产生
    public Block(String previousHash ) {
//        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        //其他所有数据完成之后再计算hash
        this.hash = calculateHash();
    }

    /**
     * 计算数据区块的hash
     * @return
     */
    public String calculateHash() {
        String calculatedhash = DigitalSignatureUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }


    public void mineBlock(int difficulty) {
        merkleRoot = DigitalSignatureUtil.getMerkleRoot(transactions);
        String target = DigitalSignatureUtil.getDifficultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block已挖到!!! : " + hash);
    }

    /**
     * 往这个区块中增加交易信息
     * @param transaction
     * @return
     */
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((previousHash != "0")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("交易信息添加入区块失败，弃用");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("交易信息成功添加入区块");
        return true;
    }

}
