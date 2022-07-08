package com.undie.crash.demo;

import com.undie.crash.demo.base.MerkleTree;
import com.undie.crash.demo.util.SharUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author glant
 * @Date 2022/6/19 16:21
 **/
public class SharTest {
    public static void main(String[] args) {
        try {
//            f1();
            merkleTreeTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void f1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String msg = "glant!!!王大锤";
        String msg_hex1 = SharUtil.bytesToHexString(msg.getBytes(StandardCharsets.UTF_8));
        byte[] bytes2 = SharUtil.hexStringToBytes(msg_hex1);
        String msg_new = new String(bytes2, StandardCharsets.UTF_8);
        System.err.println(msg_new);


        //eccode
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(msg.getBytes("UTF-8"));

        byte[] digest = messageDigest.digest();
        String msg_hex = SharUtil.bytesToHexString(digest);
        byte[] bytes1 = SharUtil.hexStringToBytes(msg_hex);

        String sha256StrJava = SharUtil.shar256Str(msg);
        byte[] bytes = SharUtil.hexStringToBytes(sha256StrJava);

        System.err.println(msg_new);
    }

    public static void merkleTreeTest() {
        //init tx list
        List<String> txList = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            String tx = i + "";
            txList.add(tx);
        }

        //build merkle tree
        if (txList.size() > 0) {
            //append if size % 2 != 0
            if (txList.size() % 2 != 0) {
                txList.add(txList.get(txList.size() - 1));
            }
            List<MerkleTree> baseNodes = txList.stream().map(e -> new MerkleTree(SharUtil.shar256Str(e))).collect(Collectors.toList());
            MerkleTree root = buildMerkleTree(baseNodes);
            System.err.println(root);
        }
    }

    public static MerkleTree buildMerkleTree(List<MerkleTree> nodes) {
        if (nodes.size() == 0) return null;
        MerkleTree root = null;
        List<MerkleTree> treeList = new ArrayList<>();
        if (nodes.size() % 2 != 0) {
            nodes.add(nodes.get(nodes.size() - 1));
        }

        for (int i = 0; i < nodes.size(); i+=2) {
            //get one and tow tx
            MerkleTree left = nodes.get(i);
            MerkleTree right = nodes.get(i + 1);

            String uppder_shar = SharUtil.shar256Str(left.getHash() + right.getHash());

            MerkleTree upperbRoot = new MerkleTree(uppder_shar, left, right);
            treeList.add(upperbRoot);
        }

        if (treeList.size() == 1) {
            return treeList.get(0);
        } else {
            root = buildMerkleTree(treeList);
        }

        return root;
    }
}
