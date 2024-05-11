package cat.uvic.xips.security.jwt;

import cat.uvic.xips.entities.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.*;

public class GenerateTokenForTestingPurposes {

    public static final String SECRET_KEY = "tBTeEle6IfDgxVXwH0s7bp0aPhQpW9Bw/tppsLTyMJ580KlH1g6ZULwpk5270frEhCtBoditMX9TlBhhZSrlSg==";

    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");
        claims.put("roles", roles);

        String token = getToken(claims, "afcasco@gmail.com");
        System.out.println(token);
    }

    private static String getToken(Map<String, Object> extraClaims, String username){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR,1);
        Date expirationDate = cal.getTime();



        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .signWith(generateKey())
                .compact();
    }

    public static SecretKey generateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
