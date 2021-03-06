package com.jwtToken.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;

	@Value("${jwt.secret}")
	private String secret;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		System.out.println(roles);
		if(roles.contains(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))) {
			claims.put("role", roles);
		}
		else if(roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			claims.put("role", roles);
		}
		else {
			claims.put("role", roles);
		}
				
		return doGenerateToken(claims, userDetails.getUsername());
	}

	public String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
//		List<SimpleGrantedAuthority> roles = getRolesFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		
	}
	
//	public List<SimpleGrantedAuthority> getRolesFromToken(String token){
//		System.out.println("in getRoleFromToken method");
//		Claims claims=getAllClaimsFromToken(token);
//		List<SimpleGrantedAuthority> roles = null;
//		
//		String isInstructor= (String) claims.get("hasRole");
//		
//		if(isInstructor!=null && isInstructor=="INSTRUCTOR") {
//			roles = Arrays.asList(new SimpleGrantedAuthority("INSTRUCTOR"));	
//		}
//		
//		return roles;
//	}
	
}
