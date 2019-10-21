package com.yilnz.util.spring.controller.entity;

import lombok.Data;

@Data
public class AAPI {
	private String name;
	private String fullClassName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
}
