package com.leandropinfo.java_17_jwt_gradle_mongodb.service;

import com.leandropinfo.java_17_jwt_gradle_mongodb.domain.User;
import com.leandropinfo.java_17_jwt_gradle_mongodb.dto.UserRegistration;
import com.leandropinfo.java_17_jwt_gradle_mongodb.enums.NivelacessoEnum;
import com.leandropinfo.java_17_jwt_gradle_mongodb.repository.UserRepository;
import com.leandropinfo.java_17_jwt_gradle_mongodb.security.AuthResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User novo(UserRegistration usuarioNovo, NivelacessoEnum nivelacesso) {

        User user = new User();

        try {
            user.setId(null);
            user.setNome(usuarioNovo.getNome());
            user.setLogin(usuarioNovo.getLogin());
            user.setSenha(pe.encode(usuarioNovo.getSenha()));
            user.setNivelacesso(nivelacesso);

            this.repo.insert(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User salvar(User User){
        return this.repo.save(User);
    }

    public User listarPorId(String UserId) {
        return this.repo.findById(UserId).orElse(null);
    }

    public User pesquisarPorLogin(String login){
        return this.repo.findTop1ByLoginIgnoreCase(login);
    }

    public AuthResponse preencheAuthResponse(User dto) {
        AuthResponse authResponse = new AuthResponse();

        authResponse.setNome(dto.getNome());
        authResponse.setLogin(dto.getLogin());
        authResponse.setNivelacesso(dto.getNivelacesso());
        authResponse.setToken(dto.getUltimotoken());

        return authResponse;
    }


    public void verificaUsuarioMaster(){
        User usuario = pesquisarPorLogin("adm");

        if (usuario==null){
            var usuarioNovo = UserRegistration.builder().
                    nome("Adminstrador").
                    login("adm").
                    senha("123").
                    build();

            novo(usuarioNovo, NivelacessoEnum.ROLE_ADMINISTRATOR);
        }

        usuario = pesquisarPorLogin("client");

        if (usuario==null){
            var usuarioNovo = UserRegistration.builder().
                    nome("Client").
                    login("client").
                    senha("123").
                    build();

            novo(usuarioNovo, NivelacessoEnum.ROLE_CLIENT);
        }

    }


}
