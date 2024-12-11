package com.example.demo.config.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.config.JWTUtil;
import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
	    return path.equals("/login") || path.equals("/join");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");
		log.info("authorization : {}" , authorization);
		// Authorization 헤더 검증 
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.getWriter().write("no token");
		    return;
		}

		String token = authorization.split(" ")[1];
		if (jwtUtil.isExpired(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.getWriter().write("expried");
		    return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(username);
		userEntity.setPassword("temppassword");
		userEntity.setRole(role);

		CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
				customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}