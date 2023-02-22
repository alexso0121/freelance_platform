package com.springboot.sohinalex.java.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix="rsa")
public record RsaKeyProp(RSAPublicKey publicKey, RSAPrivateKey privateKey){

}

