package com.leandropinfo.java_17_jwt_gradle_mongodb.security;

import com.leandropinfo.java_17_jwt_gradle_mongodb.domain.User;
import com.leandropinfo.java_17_jwt_gradle_mongodb.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JwtTokenUtil jwtUtil;
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil, UserService userService, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			String username = jwtUtil.getUsername(token);
			
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			if (auth != null) {
				User user = userService.pesquisarPorLogin(username);

				if (user !=null){
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}else{
				String mensagem = "{";
						mensagem += "	\"timestamp\": null,";
						mensagem += "    \"status\": 401,";
						mensagem += "    \"error\": \"Unauthorized\",";
						mensagem += "    \"message\": \"Token Expirado!\",";
						mensagem += "    \"path\": null";
						mensagem += "}";	                	
					
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setCharacterEncoding("UTF-8");
				response.setHeader("Content-Type", "application/json");
				response.getWriter().write(mensagem);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());					
		}
		return null;
	}
	
}

