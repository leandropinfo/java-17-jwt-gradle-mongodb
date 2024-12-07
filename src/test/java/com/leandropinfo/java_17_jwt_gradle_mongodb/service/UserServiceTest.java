package com.leandropinfo.java_17_jwt_gradle_mongodb.service;

import com.leandropinfo.java_17_jwt_gradle_mongodb.domain.User;
import com.leandropinfo.java_17_jwt_gradle_mongodb.dto.UserRegistration;
import com.leandropinfo.java_17_jwt_gradle_mongodb.enums.NivelacessoEnum;
import com.leandropinfo.java_17_jwt_gradle_mongodb.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private Validator validator;

    @Test
    @DisplayName("Deve criar um usuario com sucesso!")
    void deveCriarUmUsuarioComSucesso() {
        // Mock data
        var usuarioNovo = UserRegistration.builder().
                nome("User").
                login("user").
                senha("123").
                build();

        var usuario = new User();
        usuario.setId(null);
        usuario.setNome("User");
        usuario.setLogin("user");
        usuario.setNivelacesso(NivelacessoEnum.ROLE_CLIENT);
        usuario.setSenha("123");

        // Chamada ao método sendo testado
        User result = userService.novo(usuarioNovo,NivelacessoEnum.ROLE_CLIENT);

        // Verificações
        assertNotNull(result);
        assertEquals("User", result.getNome());
        assertEquals("user", result.getLogin());
        assertEquals("123", result.getSenha());
        assertEquals(NivelacessoEnum.ROLE_CLIENT, result.getNivelacesso());

        // Verifica se o método insert foi chamado uma vez
        verify(userRepository, times(1)).insert(any(User.class));
    }

}