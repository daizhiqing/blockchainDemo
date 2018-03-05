package com.dzq.chainblock;

import com.dzq.utils.DigitalSignatureUtil;

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
    private String data; //数据块所持有的数据
    private long timeStamp; //时间戳

    private int nonce;//随机数

    //构造函数，由前一个区块后产生
    public Block(String data,String previousHash ) {
        this.data = data;
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
                        data
        );
        return calculatedhash;
    }

    public void mineBlock(int difficulty) {
        String target = DigitalSignatureUtil.getDifficultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block已挖到!!! : " + hash);
    }
}
