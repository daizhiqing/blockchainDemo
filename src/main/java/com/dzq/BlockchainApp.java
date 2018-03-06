package com.dzq;

import com.dzq.chainblock.Block;
import com.dzq.coin.Wallet;
import com.dzq.transaction.Transaction;
import com.dzq.transaction.TransactionOutput;
import com.dzq.utils.DigitalSignatureUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

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
    public static int difficulty = 5; //设置挖矿难度，值越大越难 1-64

    /**
     * 来保存所有未使用的可被作为输入（inputs）的交易
     */
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public static float minimumTransaction = 0.1f;

    public static Transaction genesisTransaction;

    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args) {
//        POW();
//        TRANS();
        testMain();
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    /**
     * 模拟矿工算力挖矿：工作量证明
     */
//    public static void POW(){
//        System.out.println("正在尝试挖掘block 1... ");
//        addBlock(new Block("Hi im the first block", "0"));
//
//        System.out.println("正在尝试挖掘block 2... ");
//        addBlock(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
//
//        System.out.println("正在尝试挖掘block 3... ");
//        addBlock(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
//
//        System.out.println("\nBlockchain is Valid: " + DigitalSignatureUtil.isChainValid(blockchain , difficulty));
//
//        String blockchainJson = DigitalSignatureUtil.getJson(blockchain);
//        System.out.println("\nThe block chain: ");
//        System.out.println(blockchainJson);
//    }

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

    public static void testMain(){
        boolean flag = false;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        walletA = new Wallet();
        walletB = new Wallet();
        /**
         * 初始钱包
         */
        Wallet coinbase = new Wallet();

        //创建一个创世交易，我们往A钱包中充值100个币
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        //对创世交易进行数字签名
        genesisTransaction.generateSignature(coinbase.privateKey);
        //设置交易ID
        genesisTransaction.transactionId = "0";
        //手动创建一个交易输出
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId));
        //其重要的存储我们的第一次交易在utxos列表
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("创建第一个初始区块");
        Block genesis = new Block("0");
        flag = genesis.addTransaction(genesisTransaction);

        addBlock(genesis);


        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA 的余额为: " + walletA.getBalance());
        System.out.println("\nWalletA 给 WalletB 转账40个币...");
        flag = block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));

        addBlock(block1);

        System.out.println("\nWalletA 的余额变为: " + walletA.getBalance());
        System.out.println("WalletB的余额变为: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA 尝试转账1000个币给 Wallet");
        flag = block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));

        addBlock(block2);

        System.out.println("\nWalletA 的余额变为: " + walletA.getBalance());
        System.out.println("WalletB 的余额变为: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB 尝试转账20个币给 WalletA");
        flag = block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));

        addBlock(block3);

        System.out.println("\nWalletA's 的余额为: " + walletA.getBalance());
        System.out.println("WalletB's 的余额为: " + walletB.getBalance());

        DigitalSignatureUtil.isChainValid(blockchain ,  genesisTransaction , difficulty);
    }
}
