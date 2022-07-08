package com.undie.crash.demo.proxy;

/**
 * @Description
 * @Author glant
 * @Date 2022/6/5 23:54
 **/
public class Man implements Persion {
    @Override
    public void speak(String msg) {
        System.err.println("i am man, msg:" + msg);
    }
}
