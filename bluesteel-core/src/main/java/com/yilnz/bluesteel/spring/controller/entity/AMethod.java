package com.yilnz.bluesteel.spring.controller.entity;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AType getReturnType() {
        return returnType;
    }

    public void setReturnType(AType returnType) {
        this.returnType = returnType;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<AParam> getParams() {
        return params;
    }

    public void setParams(List<AParam> params) {
        this.params = params;
    }

    public String getParamsJson() {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder("{");
        params.forEach(e -> {
            sb.append(e.getName() + ":" + e.getType().getJson());
        });
        sb.append("}");
        return sb.toString();
    }
}
