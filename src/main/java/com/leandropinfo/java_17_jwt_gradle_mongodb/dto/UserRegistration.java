package com.leandropinfo.java_17_jwt_gradle_mongodb.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserRegistration {

    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe o Login")
    private String login;

    @NotBlank(message = "Informe a senha")
    @Size(min = 6, max = 20, message = "A senha deve ter de 6 a 20 caracteres")
    private String senha;
}
