package com.dzq.chainblock;

import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project : blockchainDemo
 * @Package Name : com.dzq.chainblock
 * @Description : TODO 数字签名工具，这里选择SHA256
 * @Author : daizhiqing@xiaochong.com
 * @Creation Date : 2018年03月05日下午4:26
 * @ModificationHistory Who When What
 */
public class DigitalSignatureUtil {

    /**
     * 选择SHA256算法来对字符创数据进行加密
     * @param input
     * @return
     */
    public static String applySha256(String input){

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转换为json格式字符串
     * @param o
     * @return
     */
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }

    /**
     * 返回默认长度的字符串，匹配hash合法的值
     * @param difficulty
     * @return
     */
    public static String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    /**
     * 验证码区块的合法性
     * @param blockchain
     * @param difficulty
     * @return
     */
    public static Boolean isChainValid(List<Block> blockchain , int difficulty) {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = getDifficultyString(difficulty);

        //循环遍历每个块检查hash
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //比较注册的hash和计算的hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //比较上一个块的hash和注册的上一个hash（也就是previousHash）
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //检查hash是否被处理
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

        }
        return true;
    }
}
