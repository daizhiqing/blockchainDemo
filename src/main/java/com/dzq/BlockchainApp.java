package com.dzq;

import com.dzq.chainblock.Block;
import com.dzq.utils.DigitalSignatureUtil;

import java.util.ArrayList;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.chainblock
 * @Description : TODO
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午4:42
 * @ModificationHistory Who When What
 */
public class BlockchainApp {
    //把区块装入数字中组成区块链
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 6; //设置挖矿难度，值越大越难 1-64


    public static void main(String[] args) {
        System.out.println("正在尝试挖掘block 1... ");
        addBlock(new Block("Hi im the first block", "0"));

        System.out.println("正在尝试挖掘block 2... ");
        addBlock(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));

        System.out.println("正在尝试挖掘block 3... ");
        addBlock(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));

        System.out.println("\nBlockchain is Valid: " + DigitalSignatureUtil.isChainValid(blockchain , difficulty));

        String blockchainJson = DigitalSignatureUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
