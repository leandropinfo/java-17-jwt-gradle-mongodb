package com.leandropinfo.java_17_jwt_gradle_mongodb.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Response<T> {

    private T data;
	private List<String> errors;
	private List<String> avisos;

	public Response() {
		this.errors = new ArrayList<>(); // Inicializa a lista para evitar NullPointerException
	}
}
