package com.dzq;

import com.dzq.chainblock.Block;
import com.dzq.coin.Wallet;
import com.dzq.transaction.Transaction;
import com.dzq.utils.DigitalSignatureUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
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


    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
//        POW();
        TRANS();
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    /**
     * 模拟矿工算力挖矿：工作量证明
     */
    public static void POW(){
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

    /**
     * 模拟区块链网络交易部分之交易签名验证
     */
    public static void TRANS(){
        //使用了boncey castle来作为安全实现的提供者
        Security.addProvider(new BouncyCastleProvider());
        //创建钱包
        walletA = new Wallet();
        walletB = new Wallet();
        //Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println("Private："+DigitalSignatureUtil.getStringFromKey(walletA.privateKey));
        System.out.println("Public："+DigitalSignatureUtil.getStringFromKey(walletA.publicKey));
        //创建一笔交易 A钱包 -> B 亲宝
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);
        //
        System.out.println("验证签名是否合法："+transaction.verifySignature());
    }
}
