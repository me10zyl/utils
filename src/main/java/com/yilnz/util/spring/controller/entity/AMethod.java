package com.yilnz.util.spring.controller.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AMethod {
    private String name;
    private AType returnType;
    private String clazz;
    private List<AParam> params;

    public String getParamsJson() {
        Map<String, Object> map = new HashMap<>();
        params.forEach(e -> {
            map.put(e.getName(), e.getType().getJson());
        });
        return JSON.toJSONString(map);
    }
}
