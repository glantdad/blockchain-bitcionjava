package com.undie.crash.demo.base;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author glant
 * @Date 2022/7/8 10:31
 **/
@Data
public class Script {
    private String scriptType;
    List<String> operations;

    public Script() {
    }

    public Script(String scriptType, List<String> operations) {
        this.scriptType = scriptType;
        this.operations = operations;
    }
}
