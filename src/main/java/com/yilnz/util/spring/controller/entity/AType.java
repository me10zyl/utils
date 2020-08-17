package com.yilnz.util.spring.controller.entity;

import com.yilnz.util.base.StringUtil;
import lombok.Data;

@Data
public class AType {
	private String name;
	private Class clazz;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getJson(){
		return StringUtil.classToJsonString(this.clazz);
	}
}
