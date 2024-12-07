package com.leandropinfo.java_17_jwt_gradle_mongodb.security;

import com.leandropinfo.java_17_jwt_gradle_mongodb.enums.NivelacessoEnum;
import com.leandropinfo.java_17_jwt_gradle_mongodb.domain.User;
import com.leandropinfo.java_17_jwt_gradle_mongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.pesquisarPorLogin(username);

        if (user==null){
            throw new UsernameNotFoundException("Usuário não encontrado com o login: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getSenha(),
            mapToGrantedAuthorities(user.getNivelacesso())
        );
    }

	private static List<GrantedAuthority> mapToGrantedAuthorities(NivelacessoEnum profileEnum) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;
	}
}