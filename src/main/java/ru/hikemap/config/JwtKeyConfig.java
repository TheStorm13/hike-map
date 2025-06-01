package ru.hikemap.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtKeyConfig {
  //    @Bean
  //    public String jwtSecret() {
  //        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  //        return Encoders.BASE64.encode(secretKey.getEncoded());
  //    }
}
