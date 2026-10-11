package com.prism.common.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * JWT RSA 密钥配置
 */
@Configuration
public class JwtKeyConfig {

    @Value("classpath:keys/app.pub")
    private Resource publicKeyResource;

    @Value("classpath:keys/app.key")
    private Resource privateKeyResource;

    @Bean
    public RSAPublicKey rsaPublicKey() throws Exception {
        try (InputStream is = publicKeyResource.getInputStream()) {
            return RsaKeyConverters.x509().convert(is);
        }
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() throws Exception {
        try (InputStream is = privateKeyResource.getInputStream()) {
            return RsaKeyConverters.pkcs8().convert(is);
        }
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }
}
