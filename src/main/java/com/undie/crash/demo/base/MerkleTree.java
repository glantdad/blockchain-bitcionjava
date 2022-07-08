package com.undie.crash.demo.base;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author glant
 * @Date 2022/6/19 16:04
 **/
@Data
public class MerkleTree {
    private String hash;
    private MerkleTree left;
    private MerkleTree right;

    public MerkleTree() {
    }

    public MerkleTree(String hash) {
        this.hash = hash;
    }

    public MerkleTree(String hash, MerkleTree left, MerkleTree right) {
        this.hash = hash;
        this.left = left;
        this.right = right;
    }
}
