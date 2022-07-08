package com.undie.crash.demo.base;

import java.math.BigInteger;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/8 10:30
 **/
public class UTXO {
    //The value available
    private BigInteger value;
    //script of this
    private Script script;
    //hash of transaction which contiains this
    private String hash;
    //the bolck height of boleck which contaiins this
    private long height;
    //is this is a coinbase
    private boolean coninbase;
    //the address lock the coin in this
    private String address;

    public UTXO() {
    }

    public UTXO(BigInteger value, Script script, String hash, long height, boolean coninbase, String address) {
        this.value = value;
        this.script = script;
        this.hash = hash;
        this.height = height;
        this.coninbase = coninbase;
        this.address = address;
    }
}
