package com.undie.crash.demo.base;

import java.util.List;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/7 22:52
 **/
public class Transaction {
    private long version;
    private String hash;
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;
    private long lockTime;

    public Transaction() {
    }

    public Transaction(long version, String hash, List<TransactionInput> inputs, List<TransactionOutput> outputs, long lockTime) {
        this.version = version;
        this.hash = hash;
        this.inputs = inputs;
        this.outputs = outputs;
        this.lockTime = lockTime;
    }
}
