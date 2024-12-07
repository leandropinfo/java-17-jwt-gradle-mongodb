package com.leandropinfo.java_17_jwt_gradle_mongodb.domain;

import com.leandropinfo.java_17_jwt_gradle_mongodb.enums.NivelacessoEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {

    @Id
    private String id;
    private String nome;
    private String login;
    private String senha;
    private NivelacessoEnum nivelacesso;
    private String ultimotoken;

}
