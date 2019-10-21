package com.yilnz.util.spring.controller.entity;

import lombok.Data;

@Data
public class AParam {
	private String name;
	private AType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AType getType() {
		return type;
	}

	public void setType(AType type) {
		this.type = type;
	}
}
