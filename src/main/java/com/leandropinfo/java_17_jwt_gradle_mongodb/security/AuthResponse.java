package com.leandropinfo.java_17_jwt_gradle_mongodb.security;

import com.leandropinfo.java_17_jwt_gradle_mongodb.enums.NivelacessoEnum;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;
	private String nome;
	private String login;
    private NivelacessoEnum nivelacesso;

}

