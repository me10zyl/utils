package com.yilnz.util.spring.controller.entity;

import com.alibaba.fastjson.JSONObject;
import com.yilnz.util.DateUtil;
import com.yilnz.util.reflection.ReflectionUtil;
import lombok.Data;

import java.util.Date;

@Data
public class AType {
	private String name;
	private Class clazz;

	@Override
	public String toString() {
		return name;
	}

	public String getJson(){
		Object value = "";
		final Class clazz = this.clazz;
		if (ReflectionUtil.isChildOfType(clazz, Number.class)) {
			value = 0;
		} else if (ReflectionUtil.isChildOfType(clazz, Date.class)) {
			value = DateUtil.formatDate(new Date());
		}
		return JSONObject.toJSONString(value);
	}
}
