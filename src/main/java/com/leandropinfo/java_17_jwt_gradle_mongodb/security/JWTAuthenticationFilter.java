package com.leandropinfo.java_17_jwt_gradle_mongodb.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandropinfo.java_17_jwt_gradle_mongodb.domain.User;
import com.leandropinfo.java_17_jwt_gradle_mongodb.service.UserService;
import com.leandropinfo.java_17_jwt_gradle_mongodb.util.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtUtil;

    @Autowired
    private UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil, UserService userService) {
    	setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            UserSS usuario = new ObjectMapper().readValue(req.getInputStream(), UserSS.class);
            
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(usuario.getLogin(),usuario.getSenha(), new ArrayList<>());
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
        	System.out.println("erro: " + e.getMessage());
            throw new RuntimeException(e);
        }
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User)auth.getPrincipal();

		String username =userDetails.getUsername();
        String token = jwtUtil.generateToken(username);
        
        res.addHeader("Authorization", "Bearer " + token);
        res.addHeader("access-control-expose-headers", "Authorization");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Request-Width, Content-Type, Accept");

        User user = userService.pesquisarPorLogin(userDetails.getUsername());
        
        if (user !=null) {
            Response<AuthResponse> retornoFinal = new Response<>();

            user.setUltimotoken(token);
            User retornoUser = userService.salvar(user);
            AuthResponse authResponse = userService.preencheAuthResponse(retornoUser);
            retornoFinal.setData(authResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(retornoFinal);

            res.setContentType("application/json"); 
            res.getWriter().append(json);
        }
	}
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }
}
