package com.samta.ai.util;
import com.samta.ai.constants.ApiConstants;
import com.samta.ai.handler.CustomException;
import com.samta.ai.model.AuthRequest;
import com.samta.ai.repository.CustomerRepository;
import com.samta.ai.repository.VendorRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtUtil {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private String SECRET_KEY = "samta infotech5326353";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * It will expire in 6 hrs
     * @param authRequest
     * @return
     */
    public String generateAccessToken(AuthRequest authRequest) throws CustomException {
        if(!(authRequest.getUserType()==2 || authRequest.getUserType() == 1))
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid user type");
        Map<String, Object> claims = new HashMap<>();
        claims.put(ApiConstants.TOKEN_TYPE,ApiConstants.ACCESS_TOKEN);
        claims.put(ApiConstants.USERNAME,authRequest.getEmail());
        claims.put(ApiConstants.USER_TYPE,authRequest.getUserType()==1 ? ApiConstants.VENDOR : ApiConstants.CUSTOMER);
        return createToken(claims, authRequest.getEmail(),6 * 60 * 60 * 1000);
    }

    /**
     * It will expire in 7 days
     * @param authRequest
     * @return
     */
    public String generateRefreshToken(AuthRequest authRequest) throws CustomException {
        if(!(authRequest.getUserType()==2 || authRequest.getUserType() == 1))
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid user type");
        Map<String, Object> claims = new HashMap<>();
        claims.put(ApiConstants.TOKEN_TYPE,ApiConstants.REFRESH_TOKEN);
        claims.put(ApiConstants.USERNAME,authRequest.getEmail());
        claims.put(ApiConstants.USER_TYPE,authRequest.getUserType()==1 ? ApiConstants.VENDOR : ApiConstants.CUSTOMER);
        return createToken(claims, authRequest.getEmail(),7 * 24 * 60 * 60 * 1000);
    }

    private String createToken(Map<String, Object> claims, String subject,long time) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isValidAccessToken(String token) {
        var claims= extractAllClaims(token);
        String email= (String) claims.get(ApiConstants.USERNAME);
        String tokenType= (String) claims.get(ApiConstants.TOKEN_TYPE);
        return !isTokenExpired(token) && tokenType.equalsIgnoreCase(ApiConstants.ACCESS_TOKEN);
    }

    public boolean isValidRefreshToken(String token) throws CustomException {
        var claims= extractAllClaims(token);
        String email= (String) claims.get(ApiConstants.USERNAME);
        String tokenType= (String) claims.get(ApiConstants.TOKEN_TYPE);
        if(!tokenType.equalsIgnoreCase(ApiConstants.REFRESH_TOKEN))
            return false;
        var user = ((String)claims.get(ApiConstants.USER_TYPE)).equalsIgnoreCase(ApiConstants.VENDOR) ? vendorRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "User does not exists")) :
                customerRepository.findByEmail(email)
                        .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "User does not exists"));
        return !isTokenExpired(token);
    }
}
